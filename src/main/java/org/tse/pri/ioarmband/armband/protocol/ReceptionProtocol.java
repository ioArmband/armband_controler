package org.tse.pri.ioarmband.armband.protocol;

import static org.tse.pri.ioarmband.io.message.AppMessage.AppStd.KEYBOARD;
import static org.tse.pri.ioarmband.io.message.AppMessage.AppStd.KEYBOARD_NUM;
import static org.tse.pri.ioarmband.io.message.AppMessage.AppStd.SLIDE_SWIPER;

import java.awt.Image;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.apps.App;
import org.tse.pri.ioarmband.armband.apps.AppsManager;
import org.tse.pri.ioarmband.armband.apps.impl.ImageApp;
import org.tse.pri.ioarmband.armband.apps.impl.KeyboardApp;
import org.tse.pri.ioarmband.armband.apps.impl.SlideSwiperApp;
import org.tse.pri.ioarmband.armband.apps.impl.TextMessageApp;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.protocol.ProtocolExecutor.CommandExecutor;
import org.tse.pri.ioarmband.armband.protocol.ProtocolExecutor.CommandParam;
import org.tse.pri.ioarmband.armband.tools.ImageTools;

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
}

