package org.tse.pri.ioarmband.armband.apps;

public abstract class App implements IApp {

	
	protected boolean initialized;
	
	public void build(){
		
	}
	
	private void init(){
		if(initialized)
			return;
		
		initialized = true;
	}
	
	public void clear(){
		
	}
	
	public void addAppEventsListener(){
		
	}
	public void removeAppEventListener(){
		
	}
	
	protected void dispatchSceneReady(){
		
	}
}
