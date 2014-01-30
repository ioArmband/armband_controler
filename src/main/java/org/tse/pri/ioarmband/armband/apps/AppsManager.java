package org.tse.pri.ioarmband.armband.apps;

import java.util.LinkedList;

public class AppsManager implements AppListener{
	
	DisplayEngine displayEngine;
	
	App currentApp;
	LinkedList<App> apps;
	public AppsManager() {
		init();
	}
	public void init(){
		displayEngine = SwingDisplayEngine.getInstance();
		displayEngine.start();
		apps = new LinkedList<App>(); 
		currentApp = null;
	}
	
	public void addApp(App app){
		apps.add(app);
		app.addAppEventsListener(this);
	}
	
	public void setCurrentApp(App app){
		currentApp = app;
	}
	
	public void removeApp(App app){
		if(currentApp == app){
			currentApp = apps.getLast();
			showApp(currentApp);
		}
	}
	
	private void showApp(App app){
		
	}
	
	@Override
	public void onApplicationClose(App app) {
		removeApp(app);
	}
	
	@Override
	public void onApplicationSceneReady(App app) {
		
	}
	
	//TODO: Implementation d'une véritable logique de tri des apps.

}
