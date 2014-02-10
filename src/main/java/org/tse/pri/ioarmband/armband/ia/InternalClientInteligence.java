package org.tse.pri.ioarmband.armband.ia;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.io.ConnectionsManager;
import org.tse.pri.ioarmband.armband.io.internal.InternalConnection;
import org.tse.pri.ioarmband.armband.io.internal.InternalConnectionListener;
import org.tse.pri.ioarmband.armband.io.internal.InternalConnectionService;
import org.tse.pri.ioarmband.armband.protocol.Protocol;
import org.tse.pri.ioarmband.armband.protocol.ProtocolExecutor;
import org.tse.pri.ioarmband.io.message.Command;
import org.tse.pri.ioarmband.io.message.Message;

public abstract class InternalClientInteligence implements InternalConnectionListener, Protocol {

	private static final Logger logger = Logger.getLogger(InternalClientInteligence.class);
	protected Client client;
	public InternalClientInteligence(){
		Client cl = internalConnectionService.createClient();
		setClient(cl);
	}

	public InternalClientInteligence(Client cl){
		setClient(cl);
	}
	
	private void setClient(Client client){
		if(this.client != null){
			InternalConnection connection = (InternalConnection) this.client.getConnection();
			connection.removeInternalConnectionListener(this);
		}
		this.client = client;
		InternalConnection connection = (InternalConnection) client.getConnection();
		connection.addInternalConnectionListener(this);
		
	}
	public abstract void start();
	
	@Override
	public void onInternalCommandReiceved(Command command) {
		Message msg = command.getMessage();
		logger.info(this);
		ProtocolExecutor.exec(this, client, msg.getCommandName(), msg.extractParams());
	}

	@Override
	public void onInternalConnectionClose() {
		client = null;
	}

	protected static InternalConnectionService internalConnectionService = getInternalConnectionService();
	protected static InternalConnectionService getInternalConnectionService(){
		return ConnectionsManager.getInstance().getConnectionService(InternalConnectionService.class);
	}
	
	protected void sendMessage(Message message){
		if(client == null){
			logger.warn("sendMessage(): No client found");
		}
		InternalConnection conn =  (InternalConnection) client.getConnection();
		conn.simulateCommandReception(new Command(message));
	}
	
	public void close(){
		if(client == null){
			logger.warn("sendMessage(): No client found");
		}
		InternalConnection conn =  (InternalConnection) client.getConnection();
		conn.simulateConnectionClose();
	}
}
