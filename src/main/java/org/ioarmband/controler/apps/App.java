package org.ioarmband.controler.apps;

import java.awt.Container;

import org.ioarmband.controler.net.Client;

public interface App {
	public void addAppEventsListener(AppListener listener);
	public void removeAppEventListener(AppListener listener);
	public void addGestureEventsListener(GestureListener listener);
	public void removeGestureEventListener(GestureListener listener);
	public Container getPanel();
	public Client getClient();
	public void setParams(String params);
	public void build(Container container);
	public void hide();
}
