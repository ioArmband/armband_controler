package org.ioarmband.controler.apps;

import java.awt.image.BufferedImage;
import java.util.Collection;

import org.ioarmband.controler.input.Gesture;
import org.ioarmband.controler.input.Pointer;

public interface DisplayEngine {
	public void start();
	public void setPointers(Collection<Pointer> pointers);
	public void setGestures(Collection<Gesture> gestures);
	public void setApp(App app);
	public BufferedImage getScreenCapture();
}
