package org.tse.pri.ioarmband.armband.apps;

import java.awt.Container;

import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.io.Client;

public interface App {
	public void addAppEventsListener(AppListener listener);
	public void removeAppEventListener(AppListener listener);
	public void addGestureEventsListener(GestureListener listener);
	public void removeGestureEventListener(GestureListener listener);
	public JPanel getPanel();
	public Client getClient();
	public void build(Container container);
	public void hide();
}
