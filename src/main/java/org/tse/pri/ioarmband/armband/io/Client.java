package org.tse.pri.ioarmband.armband.io;

import org.tse.pri.ioarmband.armband.apps.IApp;
import org.tse.pri.ioarmband.io.connection.IConnection;

public class Client {
	private IConnection connection;
	private IConnectionService parentConnectionService; 
	private IApp currentApp;
	
	
	public Client(IConnectionService parentConnectionService, IConnection connection) {
		super();
		this.connection = connection;
		this.parentConnectionService = parentConnectionService;
	}
	
	public void close(){
		connection.close();
	}
	
	public IConnection getConnection() {
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
