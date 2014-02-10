package org.tse.pri.ioarmband.armband.protocol;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.io.Client;

public class ProtocolExecutor {

	
	private static final Logger logger = Logger.getLogger(ProtocolExecutor.class);
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface CommandExecutor
	{
		String value();
	}
	
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface CommandParam
	{
		String value();
		boolean required() default true;
	}

	public static void exec( Protocol protocol, Client client, String commandName, Map<String, Object> inputParams) {
	
		logger.info("exec() : entered with protocol ["+ protocol +"]  parameters client: [" + client + "], commandName [" + commandName +
				"], inputParams[" + inputParams + "]");
	
	
		Method[] methods = protocol.getClass().getDeclaredMethods();
		for (Method method : methods) {
			CommandExecutor commandExecutor = method.getAnnotation(CommandExecutor.class);
			logger.debug("exec() : found : Method [" + method.getName() + "] with CommandExecutor " + commandExecutor );
			
			if(commandExecutor != null && commandName.equals(commandExecutor.value())){
				logger.debug("exec() : command [" + commandName + "] treated by method [" + method.getName() + "]" );
				Class<?>[] paramClasses = method.getParameterTypes();
				Annotation[][] paramAnnotations = method.getParameterAnnotations();
				ArrayList<Object> parameters = new ArrayList<Object>();
				for (int paramIndex = 0; paramIndex < paramClasses.length; paramIndex++) {
					Class<?> paramClazz = paramClasses[paramIndex]; 
					if(paramClazz.equals(Client.class))
					{
						parameters.add(client);
					}else{
						boolean annotationFound = false;
						Object paramValue = null;
						for( Annotation annotation : paramAnnotations[paramIndex] ){
							if( annotation.annotationType().equals( CommandParam.class ) ){
								CommandParam commandAnnotation = (CommandParam) annotation;
								try{
									Object paramObject = inputParams.get(commandAnnotation.value());
									paramValue = paramClazz.cast(paramObject);
								}catch(NullPointerException e){
									logger.error("Requiered param " + commandAnnotation.value() + " not found for command " + method.getName());
								}catch(ClassCastException e){
									logger.error("Cannot cast param " + commandAnnotation.value() + " for command " + method.getName(),e);
								}
								annotationFound = true;
								break;
							}
						}
						if(!annotationFound){
							logger.error("Annotation CommandParam not found on parameters " + paramIndex +
									" of method " + method.getName());
						}
						parameters.add(paramValue);
					}
				}
				try {
					method.invoke(protocol, parameters.toArray());
				} catch (IllegalArgumentException e) {
					logger.error("Method " + method.getName() + " linked to command " + commandExecutor.value() +
							" does not implement the correct parameters");
					e.printStackTrace();
				} catch (Exception e) {
					logger.fatal("Cannot acces to called Method " + method);
					e.printStackTrace();
				}
				logger.info("exec() : Done");
				return;
			}
		}
	
		logger.error("exec() : No method found to treat command :" + commandName);
	}
}