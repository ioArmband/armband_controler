package org.ioarmband.controler.net.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import org.apache.log4j.Logger;
import org.ioarmband.controler.net.ClientsManager;
import org.ioarmband.controler.net.IConnectionService;
import org.ioarmband.controler.net.IServiceStateChangeListener;
import org.ioarmband.controler.net.ServiceState;
import org.ioarmband.controler.tools.PropertiesManager;
import org.ioarmband.net.connection.StreamedConnection;

public class BluetoothConnectionService implements IConnectionService, Runnable{

	private static final Logger logger = Logger.getLogger(BluetoothConnectionService.class);
	
	String serviceName;
	String serviceUUID;
	String connectionURL;
	boolean running;
	ServiceState state;
	Thread processThread;
	StreamConnectionNotifier scn;
	
	public BluetoothConnectionService(){
		scn = null;
		state = ServiceState.UNINITIALIZED;
		init();
	}
	
	private void init(){
		
		if(state != ServiceState.UNINITIALIZED)
			return;

		logger.info("Initialisation du service Bluetooth : <" + this.toString() + ">");
		
		serviceName = PropertiesManager.getString("connection_service.bluetooth.name");
		serviceUUID = PropertiesManager.getString("connection_service.bluetooth.uuid");
		
		UUID SERVICEUUID_UUID = new UUID(serviceUUID, true);
		connectionURL = "btspp://localhost:"+SERVICEUUID_UUID.toString()+";name="+serviceName;	

		
		setState(ServiceState.STOPPED);
	}
	
	public void run() {
		try {
			ClientsManager clientsManager = ClientsManager.getInstance();
			scn = (StreamConnectionNotifier) Connector.open(connectionURL);
			
			setState(ServiceState.STARTED);
			logger.info("Client bluetooth en attente de connexion");
			while(running){
				StreamConnection connection = scn.acceptAndOpen();
				logger.info("Client bluetooth connecté");
				InputStream in = connection.openInputStream();
				OutputStream out = connection.openOutputStream();
				clientsManager.addClient(this, new StreamedConnection(in,out));
			}
			
			
		} catch (IOException e) {
			logger.info("Fermeture");
			
		} finally {
			try {
				if(scn != null){
					scn.close();
				}
				processThread = null;
			} catch (IOException e) {
				logger.error("La fermeture du service Bluetooth a échoué",e);
				setState(ServiceState.FAILED);
			}
		}
	}
	
	public void start(){
		if(processThread != null){
			logger.error("Impossible de démarrer le service Bluetooth car le thread n'a pas été réinitialisé: <" + this.toString() + ">");
			return;
		}
		logger.info("Démarage du service Bluetooth : <" + this.toString() + ">");
		
		running = true;
		setState(ServiceState.STARTING);
		try {
			LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
			processThread = new Thread(this);
			processThread.start();
			System.out.println("Creation d'un service " + connectionURL);
		} catch (BluetoothStateException e) {
			logger.error("Le démarage du service Bluetooth a échoué, est ce que le module internet est connécté et activé?",e);
			setState(ServiceState.FAILED);
		}
	}
	
	public void stop(){
		setState(ServiceState.STOPING);
		logger.info("Stopping Bluetooth service <" + this.toString() + ">");
		running = false;
		if(scn != null){
			try {
				scn.close();
			} catch (IOException e) {
				logger.info("stop(): Fermeture de l'ecoute bluetooth",e);
				e.printStackTrace();
			}
		}
		try {
			LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.CACHED);
		} catch (BluetoothStateException e) {
			setState(ServiceState.FAILED);
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
		logger.debug("The Bluetooth service passed from state " + state.name() + " to state " + state.name());
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
		return "BluetoothConnectionService [serviceName=" + serviceName
				+ ", serviceUUID=" + serviceUUID + ", connectionURL="
				+ connectionURL + "]";
	}
	@Override
	public String getName() {
		return "Bluetooth";
	}
}
