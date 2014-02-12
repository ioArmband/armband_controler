package org.ioarmband.controler.apps;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import org.ioarmband.controler.input.Gesture;
import org.ioarmband.controler.input.Pointer;
import org.ioarmband.controler.net.Client;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.GestureMessage;

public abstract class GenericSwingApp implements App {

	protected JPanel panel;
	protected Client client;
	protected String params;
	protected Collection<Gesture> gestures;
	protected Collection<Pointer> pointers;
	protected boolean swipeEnabled;

	public GenericSwingApp(Client client){
		this.client = client;
		this.gestures = new ArrayList<Gesture>();
		this.swipeEnabled = false;
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
	public void setParams(String params) {
		this.params = params;
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

	
	public Collection<Pointer> getPointers() {
		return pointers;
	}
	public void setPointers(java.util.Collection<Pointer> pointers) {
		this.pointers = pointers;
	}
	
	public Collection<Gesture> getGestures() {
		return gestures;
	}
	public void setGestures(java.util.Collection<Gesture> gestures) {
		this.gestures = gestures;
		for (Gesture gesture : gestures) {
			if(swipeEnabled && gesture.getType().equals(GestureType.SWIPE)){
				String direction = gesture.getDirection();
				client.sendCommand(new GestureMessage(GestureType.SWIPE, direction));
			}
		}
	}
	
	public boolean isSwipeEnabled() {
		return swipeEnabled;
	}
	public void setSwipeEnabled(boolean swipeEnabled) {
		this.swipeEnabled = swipeEnabled;
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
