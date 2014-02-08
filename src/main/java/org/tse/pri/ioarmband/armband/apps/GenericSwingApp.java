package org.tse.pri.ioarmband.armband.apps;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public abstract class GenericSwingApp implements App {

	protected JPanel panel;
	protected Client client;
	protected boolean initialized;

	public GenericSwingApp(Client client){
		this.initialized = false;
		this.client = client;
	}
	
	public Client getClient(){
		return client;
	}

	public JPanel getPanel(){
		return panel;
	}

	public void build(){
		init();
	}

	protected void init(){
		if(initialized)
			return;

		initialized = true;
	}

	public void clear(){

	}

	Set<AppListener> appListeners = new HashSet<AppListener>();
	public void addAppEventsListener(AppListener appListener){
		appListeners.add(appListener);
	}
	public void removeAppEventListener(AppListener appListener){
		appListeners.remove(appListener);
	}
	protected void dispatchClose(){
		for (AppListener listener : appListeners) {
			listener.onApplicationClose(this);
		}
	}	
	protected void dispatchSceneReady(){
		for (AppListener listener : appListeners) {
			listener.onApplicationSceneReady(this);
		}
	}

	Set<GestureListener> gestureListeners = new HashSet<GestureListener>();
	public void addGestureEventsListener(GestureListener gestureListener){
		gestureListeners.add(gestureListener);
	}
	public void removeGestureEventListener(GestureListener gestureListener){
		gestureListeners.remove(gestureListener);
	}
	protected void dispatchGesture(GestureType gestureType, String source){
		for (GestureListener listener : gestureListeners) {
			listener.onGesture(this, gestureType, source);
		}
	}
}
