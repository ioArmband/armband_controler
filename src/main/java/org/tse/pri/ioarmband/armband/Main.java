package org.tse.pri.ioarmband.armband;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.apps.AppsManager;
import org.tse.pri.ioarmband.armband.ia.MenuClientInteligence;
import org.tse.pri.ioarmband.armband.ia.MenuData;
import org.tse.pri.ioarmband.armband.input.InputsManager;
import org.tse.pri.ioarmband.armband.input.LeapMotionInput;
import org.tse.pri.ioarmband.armband.io.ConnectionsManager;
import org.tse.pri.ioarmband.armband.io.bluetooth.BluetoothConnectionService;
import org.tse.pri.ioarmband.armband.io.internal.InternalConnectionService;
import org.tse.pri.ioarmband.armband.io.lan.LANConnectionService;


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
        //connectionsManager.registerService(RemotedConnectionService.class,true);
        InputsManager inputsManager = InputsManager.getInstance();
        inputsManager.registerInput(LeapMotionInput.class);
        

        AppsManager appsManager = AppsManager.getInstance();
        launchApps();
        //internalConnection.simulateCommandReception(new Command(new KeyboardAppMessage()));
    	//logger.info("Closing connections");
        //connectionsManager.stopAllServices();
    	//logger.info("Closing armband controler");
    }
    
    public static void launchApps(){
    	try{
    	MenuClientInteligence menu = new MenuClientInteligence();
    	MenuData d = new MenuData("Menu", MenuClientInteligence.class);
    	menu.registerApp(d);
    	menu.start();
    	}catch(Exception e){
    		logger.error(e);
    	}
    	/*
    	BufferedImage img = ImageIO.read(new File("src/main/resources/test_icon.jpg"));
        ConnectionsManager connectionsManager = ConnectionsManager.getInstance();
        AppsManager appsManager = AppsManager.getInstance();
        
        InternalConnectionService internalConnectionService = connectionsManager.getConnectionService(InternalConnectionService.class);
        Client internalClient1 = internalConnectionService.createClient();
        Client internalClient2 = internalConnectionService.createClient();
        InternalConnection conn1 = (InternalConnection) internalClient1.getConnection();
        InternalConnection conn2 = (InternalConnection) internalClient2.getConnection();
        
        System.out.println(ImageTools.encodeBase64(img, "jpg"));
        conn2.simulateCommandReception(new Command(new TextMessageAppMessage("Mail", "John", "Hi! How are you today?",ImageTools.encodeBase64(img, "jpg"))));
        */
        /*
        conn1.simulateCommandReception(new Command(new KeyboardAppMessage()));
        Thread.sleep(1000);
      	Thread.sleep(3000);
         */
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
