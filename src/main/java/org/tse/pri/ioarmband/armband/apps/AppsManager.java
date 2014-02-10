package org.tse.pri.ioarmband.armband.apps;

import java.awt.AWTEvent;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.input.Gesture;
import org.tse.pri.ioarmband.armband.input.InputListener;
import org.tse.pri.ioarmband.armband.input.InputsManager;
import org.tse.pri.ioarmband.armband.input.Pointer;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.io.message.GestureMessage;
import org.tse.pri.ioarmband.io.message.enums.GestureType;


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

		listenKeyboard();
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
		for (Gesture gesture : gestures) {
			if(gesture.getType().equals(GestureType.SWIPE)){
				// TODO refactor
				String direction;
				Pointer p = gesture.getPointer();
				direction = p.getDirection();

				if(currentApp != null){
					Client client = currentApp.getClient();
					if(client != null)
						client.sendCommand(new GestureMessage(GestureType.SWIPE, direction));
				}

			}
		}
	}

	public void listenKeyboard(){
		KeyEventDispatcher listener = new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent evt) {
				if(evt.getID() == KeyEvent.KEY_PRESSED) {
					
					String direction = null;
					switch(evt.getKeyCode()){
					case KeyEvent.VK_K:
						direction = "bottom";
						break;
					case KeyEvent.VK_J:
						direction = "left";
						break;
					case KeyEvent.VK_L:
						direction = "right";
						break;
					case KeyEvent.VK_I:
						direction = "top";
						break;
					}
					
					if(direction != null){
						GestureMessage gm = new GestureMessage(GestureType.SWIPE, direction);
						if(currentApp != null)
							currentApp.getClient().sendCommand(gm);
					}
					
					
				}
				return false;
			}

		};
KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(listener);
	}
}

