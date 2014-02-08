package org.tse.pri.ioarmband.armband.io.internal;

import java.util.HashSet;
import java.util.Set;

import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.io.ClientsManager;
import org.tse.pri.ioarmband.armband.io.ConnectionsManager;
import org.tse.pri.ioarmband.armband.io.IConnectionService;
import org.tse.pri.ioarmband.armband.io.IServiceStateChangeListener;
import org.tse.pri.ioarmband.armband.io.ServiceState;
import org.tse.pri.ioarmband.io.connection.IConnection;

public class InternalConnectionService implements IConnectionService {

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
	
	public Client createClient(){
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
	
	Set<IServiceStateChangeListener> serviceStateChangeListeners = new HashSet<IServiceStateChangeListener>();
	public void addStateChangeListener(IServiceStateChangeListener listener) {
		serviceStateChangeListeners.add(listener);
	}
	public void removeStateChangeListener(IServiceStateChangeListener listener) {
		serviceStateChangeListeners.remove(listener);
	}
	private void dispatcheStateChangeEnvent(){
		for (IServiceStateChangeListener listener : serviceStateChangeListeners) {
			listener.onStateChange(this, this.state);
		}
	}


}
