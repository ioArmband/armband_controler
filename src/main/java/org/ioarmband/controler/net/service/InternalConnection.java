package org.ioarmband.controler.net.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ioarmband.net.connection.IConnection;
import org.ioarmband.net.connection.IConnectionListener;
import org.ioarmband.net.message.Command;

public class InternalConnection implements IConnection {

	
	private static final Logger logger = Logger.getLogger(InternalConnection.class);
	
	@Override
	public void close() {
		dispatchConnectionClose();
		dispatchInternalConnectionClose();
	}

	@Override
	public void sendCommand(Command command) {
		logger.info("Command sended: "+ command);
		dispatchInternalCommandReceived(command);
	}

	
	public void simulateConnectionClose(){
		dispatchConnectionClose();
	}
	
	public void simulateCommandReception(Command command){
		dispatchCommandReceived(command);
	}
	
	Set<IConnectionListener> connectionListeners = new HashSet<IConnectionListener>();
	public void addConnectionListener(IConnectionListener listener) {
		connectionListeners.add(listener);
	}
	public void removeConnectionListener(IConnectionListener listener) {
		connectionListeners.remove(listener);
	}
	private void dispatchConnectionClose(){
		for (IConnectionListener listener : connectionListeners) {
			listener.onConnectionClose();
		}
	}
	private void dispatchCommandReceived(Command command){
		for (IConnectionListener listener : connectionListeners) {
			listener.onCommandReiceved(command);
		}
	}

	Set<InternalConnectionListener> internalConnectionListeners = new HashSet<InternalConnectionListener>();
	public void addInternalConnectionListener(InternalConnectionListener listener) {
		internalConnectionListeners.add(listener);
	}
	public void removeInternalConnectionListener(InternalConnectionListener listener) {
		internalConnectionListeners.remove(listener);
	}
	private void dispatchInternalConnectionClose(){
		for (InternalConnectionListener listener : internalConnectionListeners) {
			listener.onInternalConnectionClose();
		}
	}
	private void dispatchInternalCommandReceived(Command command){
		for (InternalConnectionListener listener : internalConnectionListeners) {
			listener.onInternalCommandReiceved(command);
		}
	}

}
