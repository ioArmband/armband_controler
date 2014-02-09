package org.tse.pri.ioarmband.armband;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.apps.App;
import org.tse.pri.ioarmband.armband.apps.AppsManager;
import org.tse.pri.ioarmband.armband.apps.impl.KeyboardApp;
import org.tse.pri.ioarmband.armband.apps.impl.MenuApp;
import org.tse.pri.ioarmband.armband.apps.impl.TextMessageApp;
import org.tse.pri.ioarmband.armband.input.InputsManager;
import org.tse.pri.ioarmband.armband.input.LeapMotionInput;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.io.ConnectionsManager;
import org.tse.pri.ioarmband.armband.io.bluetooth.BluetoothConnectionService;
import org.tse.pri.ioarmband.armband.io.internal.InternalConnection;
import org.tse.pri.ioarmband.armband.io.internal.InternalConnectionService;
import org.tse.pri.ioarmband.armband.io.lan.LANConnectionService;
import org.tse.pri.ioarmband.io.message.CloseAppMessage;
import org.tse.pri.ioarmband.io.message.Command;
import org.tse.pri.ioarmband.io.message.KeyboardAppMessage;
import org.tse.pri.ioarmband.io.message.TextMessageAppMessage;


public class Main 
{
	private static final Logger logger = Logger.getLogger(Main.class);
    public static void main( String[] args ) throws InterruptedException
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
        //launchApps();
        //internalConnection.simulateCommandReception(new Command(new KeyboardAppMessage()));
    	//logger.info("Closing connections");
        //connectionsManager.stopAllServices();
    	//logger.info("Closing armband controler");
    }
    
    public static void launchApps() throws InterruptedException{
        ConnectionsManager connectionsManager = ConnectionsManager.getInstance();
        AppsManager appsManager = AppsManager.getInstance();
        
        InternalConnectionService internalConnectionService = connectionsManager.getConnectionService(InternalConnectionService.class);
        Client internalClient1 = internalConnectionService.createClient();
        Client internalClient2 = internalConnectionService.createClient();
        InternalConnection conn1 = (InternalConnection) internalClient1.getConnection();
        InternalConnection conn2 = (InternalConnection) internalClient2.getConnection();
        
        conn1.simulateCommandReception(new Command(new KeyboardAppMessage()));
        Thread.sleep(1000);
        conn2.simulateCommandReception(new Command(new TextMessageAppMessage("John", "Hi! How are you today?")));
        Thread.sleep(3000);
        conn2.simulateCommandReception(new Command(new CloseAppMessage()));
        Thread.sleep(1000);
        appsManager.removeClient(internalClient2);
        Thread.sleep(1000);
        appsManager.removeClient(internalClient1);
    }
}
