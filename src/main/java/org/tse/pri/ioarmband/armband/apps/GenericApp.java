package org.tse.pri.ioarmband.armband.apps;

import java.util.HashSet;
import java.util.Set;

public abstract class GenericApp implements App {

	
	protected boolean initialized;
	
	public void build(){
		init();
	}
	
	private void init(){
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
}
