package org.tse.pri.ioarmband.armband.io;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.apps.App;
import org.tse.pri.ioarmband.armband.protocol.Protocol;
import org.tse.pri.ioarmband.armband.protocol.ProtocolManager;
import org.tse.pri.ioarmband.io.connection.IConnection;
import org.tse.pri.ioarmband.io.connection.IConnectionListener;
import org.tse.pri.ioarmband.io.message.Command;
import org.tse.pri.ioarmband.io.message.Message;

public class Client implements IConnectionListener{
	
	
	private static final Logger logger = Logger.getLogger(Client.class);
	
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
		Protocol protocol = ProtocolManager.getProtocol();
		Message message = command.getMessage();
		protocol.exec(this, message.getCommandName(), message.extractParams());
		
		// TODO Stop loop-back command
		logger.info(command);
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
