package org.ioarmband.controler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.ioarmband.controler.apps.AppsManager;
import org.ioarmband.controler.ia.MenuClientInteligence;
import org.ioarmband.controler.ia.MenuData;
import org.ioarmband.controler.input.InputsManager;
import org.ioarmband.controler.input.LeapMotionInput;
import org.ioarmband.controler.net.Client;
import org.ioarmband.controler.net.ConnectionsManager;
import org.ioarmband.controler.net.service.BluetoothConnectionService;
import org.ioarmband.controler.net.service.InternalConnection;
import org.ioarmband.controler.net.service.InternalConnectionService;
import org.ioarmband.controler.net.service.LANConnectionService;
import org.ioarmband.controler.net.service.RemotedConnectionService;
import org.ioarmband.controler.tools.ImageTools;
import org.ioarmband.net.message.Command;
import org.ioarmband.net.message.impl.KeyboardAppMessage;
import org.ioarmband.net.message.impl.TextMessageAppMessage;


public class Main 
{
	private static final Logger logger = Logger.getLogger(Main.class);
    public static void main( String[] args ) throws InterruptedException, IOException
    {
    	logger.info("Launching armband controler");

    	logger.info("Setting on connections");
        ConnectionsManager connectionsManager = ConnectionsManager.getInstance();
        connectionsManager.registerService(InternalConnectionService.class,true);
        connectionsManager.registerService(BluetoothConnectionService.class,true);
        connectionsManager.registerService(LANConnectionService.class,true);
        connectionsManager.registerService(RemotedConnectionService.class,false);
        InputsManager inputsManager = InputsManager.getInstance();
        inputsManager.registerInput(LeapMotionInput.class);
        

        AppsManager.getInstance();
        launchApps();
        //internalConnection.simulateCommandReception(new Command(new KeyboardAppMessage()));
    	//logger.info("Closing connections");
        //connectionsManager.stopAllServices();
    	//logger.info("Closing armband controler");
    }
    
    public static void launchApps() throws InterruptedException, IOException{
    	try{
    	MenuClientInteligence menu = new MenuClientInteligence();
    	menu.registerApp(new MenuData("Chronometer", "chrono"));
    	menu.registerApp(new MenuData("Connections", "cnx"));
    	menu.start();
    	}catch(Exception e){
    		logger.error(e);
    	}
    	
    	/*
    	BufferedImage img = ImageIO.read(new File("src/main/resources/test_icon.jpg"));
        ConnectionsManager connectionsManager = ConnectionsManager.getInstance();
        
        InternalConnectionService internalConnectionService = connectionsManager.getConnectionService(InternalConnectionService.class);
        Client internalClient1 = internalConnectionService.createClient();
        Client internalClient2 = internalConnectionService.createClient();
        InternalConnection conn1 = (InternalConnection) internalClient1.getConnection();
        InternalConnection conn2 = (InternalConnection) internalClient2.getConnection();
        
        System.out.println(ImageTools.encodeBase64(img, "jpg"));
        conn2.simulateCommandReception(new Command(new TextMessageAppMessage("Mail", "John", "Hi! How are you today?",ImageTools.encodeBase64(img, "jpg"))));
        
        conn1.simulateCommandReception(new Command(new KeyboardAppMessage()));
        Thread.sleep(1000);
        conn2.simulateCommandReception(new Command(new TextMessageAppMessage("source", "author", "message")));
      	Thread.sleep(1000);
        //conn2.simulateConnectionClose();
        //conn2.simulateCommandReception(new Command(new AppMessage(AppStd.SLIDE_SWIPER)));

        /*
        conn2.simulateCommandReception(new Command(new CloseAppMessage()));
        Thread.sleep(1000);
        appsManager.removeClient(internalClient2);
        Thread.sleep(1000);
        appsManager.removeClient(internalClient1);
        */
    }
}
