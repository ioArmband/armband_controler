package org.ioarmband.controler.protocol;

import static org.ioarmband.net.message.impl.AppMessage.AppStd.KEYBOARD;
import static org.ioarmband.net.message.impl.AppMessage.AppStd.KEYBOARD_NUM;
import static org.ioarmband.net.message.impl.AppMessage.AppStd.SLIDE_SWIPER;

import java.awt.Image;

import org.apache.log4j.Logger;
import org.ioarmband.controler.apps.App;
import org.ioarmband.controler.apps.AppsManager;
import org.ioarmband.controler.apps.CastingManager;
import org.ioarmband.controler.apps.impl.ChronoApp;
import org.ioarmband.controler.apps.impl.ConnexionsInfoApp;
import org.ioarmband.controler.apps.impl.ImageApp;
import org.ioarmband.controler.apps.impl.KeyboardApp;
import org.ioarmband.controler.apps.impl.MenuApp;
import org.ioarmband.controler.apps.impl.SlideSwiperApp;
import org.ioarmband.controler.apps.impl.TextMessageApp;
import org.ioarmband.controler.net.Client;
import org.ioarmband.controler.protocol.ProtocolExecutor.CommandExecutor;
import org.ioarmband.controler.protocol.ProtocolExecutor.CommandParam;
import org.ioarmband.controler.tools.ImageTools;

public class ReceptionProtocol implements Protocol {

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
		if(appName.equals(KEYBOARD.getName())){
			boolean isNum = (params.equals(KEYBOARD_NUM.getParam()));
			app = new KeyboardApp(client, !isNum);
		}
		else if(appName.equals(SLIDE_SWIPER.getName())){
			app = new SlideSwiperApp(client);
		}else if(appName.equals("chrono")){
			app = new ChronoApp(client);
		}else if(appName.equals("cnx")){
			app = new ConnexionsInfoApp(client);
		}
		else{
			logger.warn("Cannot inititialise application named \"" + appName + "\" with params \"" + params + "\"");
			return;
		}

		appsManager.addApp(app, true);
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
}

