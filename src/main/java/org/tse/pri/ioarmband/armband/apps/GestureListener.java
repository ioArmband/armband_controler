package org.tse.pri.ioarmband.armband.apps;

import org.tse.pri.ioarmband.io.message.enums.GestureType;

public interface GestureListener {
	public void onGesture(App app, GestureType gestureType, String source);
}
