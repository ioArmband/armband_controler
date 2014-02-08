package org.tse.pri.ioarmband.armband.protocol;

import static org.tse.pri.ioarmband.io.message.AppMessage.AppStd.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.apps.App;
import org.tse.pri.ioarmband.armband.apps.AppsManager;
import org.tse.pri.ioarmband.armband.apps.KeyboardApp;
import org.tse.pri.ioarmband.armband.apps.SlideSwiperApp;
import org.tse.pri.ioarmband.armband.io.Client;

public class AnnotatedProtocol implements Protocol {

	private static final Logger logger = Logger.getLogger(AnnotatedProtocol.class);
	public AnnotatedProtocol() {

	}
	

	
	
	@CommandExecutor("open_app")
	public void onOpenApp(Client client, @CommandParam("appName") String appName, @CommandParam("params") String params){
		AppsManager appsManager = AppsManager.getInstance();
		
		App app;
		if(appName.equals(KEYBOARD.getName())){
			boolean isNum = (params.equals(KEYBOARD_NUM.getParam()));
			app = new KeyboardApp(client, !isNum);
		}
		else if(appName.equals(SLIDE_SWIPER.getName())){
			app = new SlideSwiperApp(client);
		}
		else{
			logger.warn("Cannot inititialise application named \"" + appName + "\" with params \"" + params + "\"");
			return;
		}
		
		appsManager.addApp(app, true);
	}
	
	
	
	@CommandExecutor("close_app")
	public void onCloseApp(Client client){
		AppsManager appsManager = AppsManager.getInstance();
		appsManager.removeClient(client);
	}
	

		
	
	
	
	
	
	public void exec(Client client, String commandName, Map<String, String> inputParams){

		logger.info("exec() : entered with parameters client: [" + client + "], commandName [" + commandName +
				"], inputParams[" + inputParams + "]");
		
		
		Method[] methods = this.getClass().getDeclaredMethods();
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
								Class<?> paramType = commandAnnotation.type();
								String paramString = inputParams.get(commandAnnotation.value());
								paramValue = paramType.cast(paramString);
								if(paramValue == null){
									logger.info("Param " + commandAnnotation.value() + " not found in command " + method.getName());
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
					method.invoke(this, parameters.toArray());
				} catch (IllegalArgumentException e) {
					logger.error("Method " + method.getName() + " linked to command " + commandExecutor.value() +
								 " does not implement the correct parameters");
					e.printStackTrace();
				} catch (Exception e) {
					logger.fatal("Cannot acces to called Method " + method);
					e.printStackTrace();
				}
				logger.error("exec() : Done");
				return;
			}
		}

		logger.error("exec() : No method found to treat command :" + commandName);
	}
	
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface CommandExecutor
	{
		String value();
	}

	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface CommandParam
	{
		String value();
		Class<?> type() default String.class;
		
	}
}

