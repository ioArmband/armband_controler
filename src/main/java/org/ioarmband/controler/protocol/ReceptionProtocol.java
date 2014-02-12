package org.ioarmband.controler.protocol;

import java.awt.Image;
import java.lang.reflect.Constructor;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ioarmband.controler.apps.App;
import org.ioarmband.controler.apps.AppAnnotations.AppDeclaration;
import org.ioarmband.controler.apps.AppsManager;
import org.ioarmband.controler.apps.CastingManager;
import org.ioarmband.controler.apps.impl.ImageApp;
import org.ioarmband.controler.apps.impl.MenuApp;
import org.ioarmband.controler.apps.impl.TextMessageApp;
import org.ioarmband.controler.net.Client;
import org.ioarmband.controler.protocol.ProtocolExecutor.CommandExecutor;
import org.ioarmband.controler.protocol.ProtocolExecutor.CommandParam;
import org.ioarmband.controler.tools.ImageTools;
import org.reflections.Reflections;

public class ReceptionProtocol implements Protocol {

	static final String APP_PACKAGE = "org.ioarmband.controler.apps.impl";
	static final Logger logger = Logger.getLogger(ReceptionProtocol.class);
	public ReceptionProtocol() {

	}




	@CommandExecutor("open_app")
	public void onOpenApp(Client client, 
			@CommandParam("appName") String appName, 
			@CommandParam("params") String params)
	{
		AppsManager appsManager = AppsManager.getInstance();

		App app;
		Class<?> clazz = getAppClass(appName,APP_PACKAGE);
		if(clazz != null){
			try {
				Constructor<?> constructor = clazz.getConstructor(new Class[]{Client.class});
				app = (App) constructor.newInstance(client);
				app.setParams(params);
				appsManager.addApp(app, true);
			} catch (Exception e) {
				logger.error("Cannot find constructor " + clazz.getName()+"(Client client)" );
			}
		}
		else{
			logger.warn("Cannot inititialise application named \"" + appName + "\" with params \"" + params + "\"");
			return;
		}

	}


	@CommandExecutor("image_viewer_app")
	public void onOpenImageApp(Client client, 
			@CommandParam("image") Image image)
	{
		AppsManager appsManager = AppsManager.getInstance();

		App app = new ImageApp(client, image);

		appsManager.addApp(app, true);
	}	
	

	@CommandExecutor("menu_app")
	public void onOpenMenuApp(Client client, 
			@CommandParam("appIndex") Integer appIndex,
			@CommandParam("isLast") Boolean isLast,
			@CommandParam("appName") String appName)
	{
		AppsManager appsManager = AppsManager.getInstance();

		App app = new MenuApp(client, appIndex, isLast, appName);

		appsManager.addApp(app, true);
	}	
	
	@CommandExecutor("text_message_app")
	public void onOpenImageApp(Client client,
			@CommandParam("source") String source, 
			@CommandParam("message") String message, 
			@CommandParam("author") String author,
			@CommandParam(value="encodedImage", required=false) String encodedImage){
		
		AppsManager appsManager = AppsManager.getInstance();
		Image image = null;
		if(encodedImage != null){
			image = ImageTools.decodeBase64(encodedImage);
		}
		App app = new TextMessageApp(client, source, author, message, image);

		appsManager.addApp(app, true);
	}	

	@CommandExecutor("close_app")
	public void onCloseApp(Client client){
		AppsManager appsManager = AppsManager.getInstance();
		appsManager.removeClient(client);
	}
	
	
	@CommandExecutor("casting_request")
	public void onCastingRequest(Client client, 
			@CommandParam("delay") Integer delay,
			@CommandParam("width") Integer width,
			@CommandParam("height") Integer height){
		if(delay>0){
			CastingManager.registerCaster(client, delay, width, height);
		}
		else{
			CastingManager.removeCaster(client);
		}
	}
	
	private Set<Class<?>> getAllApps(String packageName){
		 Reflections reflections = new Reflections(packageName);
		 return reflections.getTypesAnnotatedWith(AppDeclaration.class);
	}
	
	private Class<?> getAppClass(String appName, String packageName){
		
		getAllApps(packageName);
		Set<Class<?>> classes = getAllApps(packageName);
		System.out.println(classes);
		for (Class<?> clazz : classes) {
			AppDeclaration declaration = clazz.getAnnotation(AppDeclaration.class);
			if(declaration.value().equals(appName)){
				return clazz;
			}
		}
		logger.error("Cannot find annotation AppDeclaration(\"" + appName + "\") in package " +packageName  );
		return null;
	}
}

