package org.ioarmband.controler.apps;

import java.awt.Container;
import java.util.Collection;

import org.ioarmband.controler.input.Gesture;
import org.ioarmband.controler.input.Pointer;
import org.ioarmband.controler.net.Client;

public interface App {
	public void addAppEventsListener(AppListener listener);
	public void removeAppEventListener(AppListener listener);
	public void addGestureEventsListener(GestureListener listener);
	public void removeGestureEventListener(GestureListener listener);
	public Container getPanel();
	public Client getClient();
	public void setParams(String params);
	public void setPointers(Collection<Pointer> pointers);
	public void setGestures(Collection<Gesture> gestures);
	public void build(Container container);
	public void hide();
}
