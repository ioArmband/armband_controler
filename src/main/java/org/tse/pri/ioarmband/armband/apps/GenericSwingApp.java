package org.tse.pri.ioarmband.armband.apps;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public abstract class GenericSwingApp implements App {

	protected JPanel panel;
	protected Client client;

	public GenericSwingApp(Client client){
		this.client = client;
	}
	
	public Client getClient(){
		return client;
	}

	public Container getPanel(){
		if(panel == null){
			panel = new JPanel();
			panel.setBackground(Color.BLACK);
			panel.setLayout(new GridLayout(1,1));
			build(panel);
		}
		return (Container) panel;
	}

	@Override
	public void hide() {	
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
