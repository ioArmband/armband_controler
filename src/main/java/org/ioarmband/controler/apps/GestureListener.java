package org.ioarmband.controler.apps;

import org.ioarmband.net.message.enums.GestureType;

public interface GestureListener {
	public void onGesture(App app, GestureType gestureType, String source);
}
