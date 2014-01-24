package org.tse.pri.ioarmband.armband;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.io.ConnectionsManager;
import org.tse.pri.ioarmband.armband.io.bluetooth.BluetoothConnectionService;
import org.tse.pri.ioarmband.armband.io.internal.InternalConnectionService;
import org.tse.pri.ioarmband.armband.io.lan.LANConnectionService;


public class Main 
{
	private static final Logger logger = Logger.getLogger(Main.class);
    public static void main( String[] args )
    {
    	logger.info("Launching armband controler");

    	logger.info("Setting on connections");
        ConnectionsManager connectionsManager = ConnectionsManager.getInstance();
        connectionsManager.registerService(InternalConnectionService.class,true);
        connectionsManager.registerService(BluetoothConnectionService.class,true);
        connectionsManager.registerService(LANConnectionService.class,true);
        //connectionsManager.registerService(RemotedConnectionService.class,true);
        

    	//logger.info("Closing connections");
        //connectionsManager.stopAllServices();
    	//logger.info("Closing armband controler");
    }
}
