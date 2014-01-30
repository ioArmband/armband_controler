package org.tse.pri.ioarmband.armband.apps;

public interface App {
	public void addAppEventsListener(AppListener listener);
	public void removeAppEventListener(AppListener listener);
}
