package org.ioarmband.controler.net;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.log4j.Logger;

public class ConnectionsManager implements IServiceStateChangeListener {
	private static final Logger logger = Logger.getLogger(ConnectionsManager.class);
	private static ConnectionsManager __instance;
	
	private LinkedHashMap<Class<? extends IConnectionService>, IConnectionService> services;

	private ConnectionsManager() {
		services = new LinkedHashMap<Class<? extends IConnectionService>, IConnectionService>();
	}

	public static ConnectionsManager getInstance() {
		if (__instance == null) {
			__instance = new ConnectionsManager();
		}
		return __instance;
	}
	
	public void registerService(Class<? extends IConnectionService> serviceClass){
		registerService(serviceClass, false);
	}	
	
	public void registerService(Class<? extends IConnectionService> serviceClass, boolean enable){

		if(services.get(serviceClass) != null){
			logger.warn("ConnectionsManager.register(): Duplication de l'enregistrement de la classe "+ serviceClass.getCanonicalName() +". La classe n'a pas été ré-enregistré.");
			return;
		}
		
		try {
			IConnectionService service =  (IConnectionService) serviceClass.newInstance();
			services.put(serviceClass, service);
			service.addStateChangeListener(this);
			if(enable){
				startService(serviceClass);
			}
		} catch (InstantiationException e) {
			logger.error("ConnectionsManager.register(): La tentative d'ajout de la classe "+ serviceClass.getCanonicalName() +" a échoué", e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error("ConnectionsManager.register(): La tentative d'ajout de la classe "+ serviceClass.getCanonicalName() +" a échoué", e);
			e.printStackTrace();
		}
		
	}

	public void startService(Class<? extends IConnectionService> serviceClass){
		IConnectionService service = services.get(serviceClass);
		if(service == null){
			logger.error("ConnectionsManager.ennable(): classe "+ serviceClass.getCanonicalName() +" non enregistrée");
			return;
		}
		service.start();
	}
	
	public void stopService(Class<? extends IConnectionService> serviceClass){
		IConnectionService service = services.get(serviceClass);
		if(service == null){
			logger.error("ConnectionsManager.ennable(): classe "+ serviceClass.getCanonicalName() +" non enregistrée");
			return;
		}
		service.stop();
	}
	
	public void stopAllServices(){
		for(Class<?> key : services.keySet()){
			IConnectionService service = services.get(key);
			service.stop();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IConnectionService> T getConnectionService(Class<T> serviceClass){
		return (T) services.get(serviceClass);
	}
	
	public ArrayList<IConnectionService> getServices(){
		return new ArrayList<IConnectionService>(services.values());
	}
	
	Set<IServiceStateChangeListener> serviceStateChangeListeners = new HashSet<IServiceStateChangeListener>();
	public void addStateChangeListener(IServiceStateChangeListener listener) {
		serviceStateChangeListeners.add(listener);
	}
	public void removeStateChangeListener(IServiceStateChangeListener listener) {
		serviceStateChangeListeners.remove(listener);
	}
	public void onStateChange(IConnectionService element, ServiceState state) {
		for (IServiceStateChangeListener listener : serviceStateChangeListeners) {
			listener.onStateChange(element, element.getState());
		}
	}
	
	
}