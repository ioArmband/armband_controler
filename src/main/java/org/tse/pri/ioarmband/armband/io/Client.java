package org.tse.pri.ioarmband.armband.io;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.apps.App;
import org.tse.pri.ioarmband.io.connection.IConnection;
import org.tse.pri.ioarmband.io.connection.IConnectionListener;
import org.tse.pri.ioarmband.io.message.Command;

public class Client implements IConnectionListener{
	
	private IConnection connection;
	private IConnectionService parentConnectionService; 
	
	
	public Client(IConnectionService parentConnectionService, IConnection connection) {
		super();
		this.connection = connection;
		this.parentConnectionService = parentConnectionService;
		this.connection.addConnectionListener(this);
	}
	
	public void close(){
		connection.removeConnectionListener(this);
		connection.close();
	}
	
	public IConnection getConnection() {
		return connection;
	}

	public IConnectionService getParentConnectionService() {
		return parentConnectionService;
	}

	public void sendCommand( Command command){
		connection.sendCommand(command); 
		
	}
	@Override
	public void onCommandReiceved(Command command) {
		// TODO Replace by command Analysis
		System.out.println(command);
		sendCommand(command);
	}

	@Override
	public void onConnectionClose() {
		dispatchConnectionClose();
	}
	
	Set<IClientConnectionCloseListener> clientConnectionCloseListeners = new HashSet<IClientConnectionCloseListener>();
	public void addConnectionCloseListener(IClientConnectionCloseListener listener) {
		clientConnectionCloseListeners.add(listener);
	}
	public void removeConnectionCloseListener(IClientConnectionCloseListener listener) {
		clientConnectionCloseListeners.remove(listener);
	}
	private void dispatchConnectionClose(){
		for (IClientConnectionCloseListener listener : clientConnectionCloseListeners) {
			listener.onClientClose(this);
		}
	}
} 
