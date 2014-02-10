package org.tse.pri.ioarmband.armband.io;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.protocol.ProtocolExecutor;
import org.tse.pri.ioarmband.armband.protocol.ReceptionProtocol;
import org.tse.pri.ioarmband.io.connection.IConnection;
import org.tse.pri.ioarmband.io.connection.IConnectionListener;
import org.tse.pri.ioarmband.io.message.Command;
import org.tse.pri.ioarmband.io.message.Message;

public class Client implements IConnectionListener{
	
	
	private static final Logger logger = Logger.getLogger(Client.class);
	
	private IConnection connection;
	private IConnectionService parentConnectionService; 
	private boolean loopbackCommand; 
	
	public Client(IConnectionService parentConnectionService, IConnection connection) {
		super();
		loopbackCommand = false;
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

	public void sendCommand( Command command ){
		connection.sendCommand(command); 
	}
	
	public void sendCommand( Message message ){
		this.sendCommand(new Command(message)); 
	}
	
	@Override
	public void onCommandReiceved(Command command) {
		Message message = command.getMessage();
		ProtocolExecutor.exec(new ReceptionProtocol(), this, message.getCommandName(), message.extractParams());
		
		logger.info(command);
		if(loopbackCommand)
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
	
	public void setLoopbackCommand(boolean loopbackCommand) {
		this.loopbackCommand = loopbackCommand;
	}
} 
