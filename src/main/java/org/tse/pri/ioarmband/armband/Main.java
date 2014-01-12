package org.tse.pri.ioarmband.armband;

import org.tse.pri.ioarmband.armband.io.ConnectionsManager;
import org.tse.pri.ioarmband.armband.io.bluetooth.BluetoothConnectionService;
import org.tse.pri.ioarmband.armband.io.internal.InternalConnectionService;
import org.tse.pri.ioarmband.armband.io.remote.RemotedConnectionService;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
        ConnectionsManager connectionsManager = ConnectionsManager.getInstance();
        connectionsManager.registerService(InternalConnectionService.class,true);
        connectionsManager.registerService(BluetoothConnectionService.class,true);
        connectionsManager.registerService(RemotedConnectionService.class,true);
        
        connectionsManager.stopService(BluetoothConnectionService.class);
    }
}
