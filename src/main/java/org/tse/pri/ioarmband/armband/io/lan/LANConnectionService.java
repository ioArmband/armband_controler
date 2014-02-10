package org.tse.pri.ioarmband.armband.io.lan;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;













import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.io.ClientsManager;
import org.tse.pri.ioarmband.armband.io.IConnectionService;
import org.tse.pri.ioarmband.armband.io.IServiceStateChangeListener;
import org.tse.pri.ioarmband.armband.io.ServiceState;
import org.tse.pri.ioarmband.armband.tools.PropertiesManager;
import org.tse.pri.ioarmband.io.connection.StreamedConnection;

public class LANConnectionService implements IConnectionService, Runnable {


	private static final Logger logger = Logger.getLogger(LANConnectionService.class);
	
	private String bindAddr;
	private int port;
	
	boolean running;
	ServiceState state;
	Thread processThread;
	ServerSocket serverSocket;
	
	public LANConnectionService(){
		state = ServiceState.UNINITIALIZED;
		init();
	}
	
	private void init(){
		if(state != ServiceState.UNINITIALIZED)
			return;

		logger.info("Initialisation du service LAN");
		
		bindAddr = PropertiesManager.getString("connection_service.lan.hostname");
		port = PropertiesManager.getInt("connection_service.lan.port");
		setState(ServiceState.STOPPED);
	}
	
	public void run() {
		try {
			ClientsManager clientsManager = ClientsManager.getInstance();
			
			setState(ServiceState.STARTED);
			logger.info("Server LAN en attente de connexion");
			while(running){
				Socket s = serverSocket.accept();
				logger.info("Client LAN connecté");
				InputStream in = s.getInputStream();
				OutputStream out = s.getOutputStream();
				clientsManager.addClient(this, new StreamedConnection(in, out));
			}
			
			
		} catch (SocketException e) {
			logger.warn("Fin prématurée de la connection LAN");
		} catch (IOException e) {
			logger.error("L'execution du service LAN a échoué",e);
			setState(ServiceState.FAILED);
		} finally {
			try {
				if(serverSocket != null){
					serverSocket.close();
				}
				processThread = null;
			} catch (IOException e) {
				logger.error("La fermeture du service LAN a échoué",e);
				setState(ServiceState.FAILED);
			}
		}
	}
	
	public void start(){
		if(processThread != null){
			logger.error("Impossible de démarrer le service LAN car le thread n'a pas été réinitialisé: <" + this.toString() + ">");
			return;
		}
		logger.info("Démarage du service LAN : <" + this.toString() + ">");
		
		running = true;
		setState(ServiceState.STARTING);
		try {
			serverSocket = new ServerSocket(port, 128, Inet4Address.getByName(bindAddr));
		} catch (UnknownHostException e) {
			logger.error("LAN service unnable to connect to host "+bindAddr, e);
			setState(ServiceState.FAILED);
		} catch (IOException e) {
			logger.error("LAN service unnable launch", e);
			setState(ServiceState.FAILED);
		}
		processThread = new Thread(this);
		processThread.start();
	}
	
	public void stop(){
		setState(ServiceState.STOPING);
		logger.info("Stopping LAN service <" + this.toString() + ">");
		running = false;
		if(serverSocket != null){
			try {
				serverSocket.close();
			} catch (IOException e) {
				logger.error("stop(): La fermeture du service LAN a échoué",e);
				setState(ServiceState.FAILED);
				e.printStackTrace();
			}
		}
		setState(ServiceState.STOPPED);
	}
	
	public ServiceState getState(){
		return this.state;
	}
	
	private void setState(ServiceState state){
		if(state == this.state)
			return;
		this.state = state;
		logger.debug("The LAN service passed from state " + state.name() + " to state " + state.name());
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
	public String toString() {
		return "LANConnectionService [bindAddr=" + bindAddr + ", port=" + port
				+ ", running=" + running + ", state=" + state + "]";
	}
	@Override
	public String getName() {
		return "LAN";
	}

}
