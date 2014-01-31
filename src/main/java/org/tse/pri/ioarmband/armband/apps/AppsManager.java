package org.tse.pri.ioarmband.armband.apps;

import java.util.Collection;
import java.util.LinkedList;

import org.tse.pri.ioarmband.armband.input.Input;
import org.tse.pri.ioarmband.armband.input.InputListener;
import org.tse.pri.ioarmband.armband.input.InputsManager;
import org.tse.pri.ioarmband.armband.input.Pointer;

import org.tse.pri.ioarmband.armband.input.Gesture;

public class AppsManager implements AppListener, InputListener{
	
	DisplayEngine displayEngine;
	
	App currentApp;
	LinkedList<App> apps;
	public AppsManager() {
		init();
	}
	public void init(){
		displayEngine = SwingDisplayEngine.getInstance();
		displayEngine.start();
		InputsManager inputs = InputsManager.getInstance();
		inputs.addInputListener(this);
		
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
	@Override
	public void onPointersChange(Collection<Pointer> pointers) {
		InputsManager in = InputsManager.getInstance();
		displayEngine.setPointers(in.getPointers());
	}
	@Override
	public void onGesture(Collection<Gesture> gestures) {
		InputsManager in = InputsManager.getInstance();
		displayEngine.setGestures(in.getGestures());
	}
	
	//TODO: Implementation d'une véritable logique de tri des apps.

}
