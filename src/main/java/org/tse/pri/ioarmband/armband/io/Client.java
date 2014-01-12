package org.tse.pri.ioarmband.armband.io;

import javax.microedition.io.StreamConnection;

import org.tse.pri.ioarmband.armband.apps.IApp;

public class Client {
	private StreamConnection connection;
	private IConnectionService parentConnectionService; 
	private IApp currentApp;
	
	
	public Client(IConnectionService parentConnectionService, StreamConnection connection) {
		super();
		this.connection = connection;
		this.parentConnectionService = parentConnectionService;
	}
	
	public void close(){
		
	}
	
	public StreamConnection getConnection() {
		return connection;
	}

	public IConnectionService getParentConnectionService() {
		return parentConnectionService;
	}
	
	public IApp getCurrentApp() {
		return currentApp;
	}
	public void setCurrentApp(IApp currentApp) {
		this.currentApp = currentApp;
	}
	
	
} 
