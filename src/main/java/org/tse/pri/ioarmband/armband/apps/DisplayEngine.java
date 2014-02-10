package org.tse.pri.ioarmband.armband.apps;

import java.awt.image.BufferedImage;
import java.util.Collection;

import org.tse.pri.ioarmband.armband.input.Gesture;
import org.tse.pri.ioarmband.armband.input.Pointer;

public interface DisplayEngine {
	public void start();
	public void setPointers(Collection<Pointer> pointers);
	public void setGestures(Collection<Gesture> gestures);
	public void setApp(App app);
	public BufferedImage getScreenCapture();
}
