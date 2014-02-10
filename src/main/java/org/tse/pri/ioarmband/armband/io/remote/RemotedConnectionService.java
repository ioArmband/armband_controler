package org.tse.pri.ioarmband.armband.io.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.io.IConnectionService;
import org.tse.pri.ioarmband.armband.io.IServiceStateChangeListener;
import org.tse.pri.ioarmband.armband.io.ServiceState;
import org.tse.pri.ioarmband.armband.tools.PropertiesManager;

public class RemotedConnectionService implements IConnectionService, Runnable{

	
	private static final Logger logger = Logger.getLogger(RemotedConnectionService.class);
	
	Socket socket;
	Thread processThread;
	boolean running;
	String hostname;
	int port;
	ServiceState state;
	
	public RemotedConnectionService() {
		running = false;
		state = ServiceState.UNAVAILABLE;
		init();
	}
	
	private void init(){
		
		hostname = PropertiesManager.getString("connection_service.remote.dest_address");
		port  = PropertiesManager.getInt("connection_service.remote.dest_port");

		logger.info("Initialization of Remoted ConnectionService : <" + this.toString() + ">");
	}
	
	
	public void run() {
		PrintWriter out;
		// TODO : Remote server connection protocol
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			// TODO : Remote client adding
			out.println("coucou");
			setState(ServiceState.STOPPED);
		} catch (IOException e) {
			logger.error("Error while connected to the remote server", e);
		}
	}
	
	
	public void start() {
		if(processThread != null){
			logger.error("Cannot start Remoted Connection due to undeleted previous thread <" + this.toString() + ">");
			return;
		}
		setState(ServiceState.STARTING);
		running = true;
        try {
			socket = new Socket(hostname, port);
			processThread = new Thread(this);
			processThread.start();
		} catch (UnknownHostException e) {
			logger.error("Cannot start Remoted Connection, invalid host " + hostname,e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Remoted Connection failed to start",e);
			e.printStackTrace();
		}
	}

	public void stop() {
		setState(ServiceState.STOPPED);
	}

	
	public ServiceState getState() {
		return this.state;
	}
	
	private void setState(ServiceState state){
		if(state == this.state)
			return;
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
	@Override
	public String getName() {
		return "Remoted";
	}
}
