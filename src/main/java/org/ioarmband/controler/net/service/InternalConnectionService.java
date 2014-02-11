package org.ioarmband.controler.net.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ioarmband.controler.net.Client;
import org.ioarmband.controler.net.ClientsManager;
import org.ioarmband.controler.net.ConnectionService;
import org.ioarmband.controler.net.ServiceStateChangeListener;
import org.ioarmband.controler.net.ServiceState;

public class InternalConnectionService implements ConnectionService {

	
	private static final Logger logger = Logger.getLogger(InternalConnectionService.class);
	ServiceState state;
	
	public InternalConnectionService() {
		state = ServiceState.STOPPED; 
	}
	
	public void start() {
		setState(ServiceState.STARTED);
	}

	public void stop() {
		setState(ServiceState.STOPPED);
	}

	public ServiceState getState(){
		return this.state;
	}
	
	private void setState(ServiceState state){
		this.state = state;
		dispatcheStateChangeEnvent();
	}
	
	@Deprecated
	public Client createClient(InternalConnectionListener listener){
		logger.info("New InternalClient declared");
		InternalConnection connection = new InternalConnection();
		connection.addInternalConnectionListener(listener);
		
		ClientsManager clientsManager = ClientsManager.getInstance();
		return clientsManager.addClient(this, connection);
	}
	
	
	public Client createClient(){
		logger.info("New InternalClient declared");
		InternalConnection connection = new InternalConnection();
		
		ClientsManager clientsManager = ClientsManager.getInstance();
		return clientsManager.addClient(this, connection);
	}
	
	public void removeConnection(InternalConnection connection){
		connection.simulateConnectionClose();
	}
	
	public void removeClient(Client client){
		ClientsManager clientsManager = ClientsManager.getInstance();
		clientsManager.removeClient(client);
	}
	
	Set<ServiceStateChangeListener> serviceStateChangeListeners = new HashSet<ServiceStateChangeListener>();
	public void addStateChangeListener(ServiceStateChangeListener listener) {
		serviceStateChangeListeners.add(listener);
	}
	public void removeStateChangeListener(ServiceStateChangeListener listener) {
		serviceStateChangeListeners.remove(listener);
	}
	private void dispatcheStateChangeEnvent(){
		for (ServiceStateChangeListener listener : serviceStateChangeListeners) {
			listener.onStateChange(this, this.state);
		}
	}

	@Override
	public String getName() {
		return "Interne";
	}


}
