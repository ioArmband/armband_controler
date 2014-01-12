package org.tse.pri.ioarmband.armband.io.internal;

import java.util.HashSet;
import java.util.Set;

import org.tse.pri.ioarmband.armband.io.IConnectionService;
import org.tse.pri.ioarmband.armband.io.IServiceStateChangeListener;
import org.tse.pri.ioarmband.armband.io.ServiceState;

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
