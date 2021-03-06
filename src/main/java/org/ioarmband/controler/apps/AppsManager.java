package org.ioarmband.controler.apps;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ioarmband.controler.input.Gesture;
import org.ioarmband.controler.input.InputListener;
import org.ioarmband.controler.input.InputsManager;
import org.ioarmband.controler.input.Pointer;
import org.ioarmband.controler.net.Client;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.GestureMessage;


public class AppsManager implements AppListener, InputListener{

	private static final Logger logger = Logger.getLogger(AppsManager.class);

	DisplayEngine displayEngine;

	App currentApp;
	LinkedList<App> apps;
	Map<Client, App> clientsApp;
	private static AppsManager __instance;

	public AppsManager() {
		init();
	}

	public static AppsManager getInstance() {
		if (__instance == null) {
			__instance = new AppsManager();
		}
		return __instance;
	}

	public void init(){
		displayEngine = SwingDisplayEngine.getInstance();
		displayEngine.start();
		InputsManager inputs = InputsManager.getInstance();
		inputs.addInputListener(this);

		apps = new LinkedList<App>(); 
		clientsApp = new HashMap<Client, App>();
		currentApp = null;

	}

	public void addApp(App app){
		this.addApp(app, true);
	}
	public void addApp(App app, boolean setCurrent){
		Client client = app.getClient();
		App clientApp = clientsApp.get(client);

		if(clientApp != null){
			apps.remove(clientApp);
		}

		apps.add(app);
		clientsApp.put(client, app);

		app.addAppEventsListener(this);
		if(setCurrent)
			setCurrentApp(app);
	}


	private void setCurrentApp(App app){
		currentApp = app;
		displayEngine.setApp(app);
	}

	public void removeApp(App app){
		if(app == null)
			return;
		apps.remove(app);
		if(app.equals(clientsApp.get(app.getClient()))){
			clientsApp.remove(app.getClient());
		}

		if(currentApp == app){
			try{
				currentApp = apps.getLast();
			}
			catch(NoSuchElementException e){
				currentApp = null;
				logger.info("Back to the menu");
			}
			setCurrentApp(currentApp);
		}
	}

	public void removeClient(Client client){
		App app = clientsApp.get(client);
		removeApp(app);
		CastingManager.removeCaster(client);
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
		logger.info("onGesture : " + gestures);
		InputsManager in = InputsManager.getInstance();
		displayEngine.setGestures(in.getGestures());
		currentApp.setGestures(gestures);
	}

	
	public BufferedImage getScreenCapture(){
		return displayEngine.getScreenCapture();
	}
	

}

