package org.tse.pri.ioarmband.armband.input;

import java.util.Collection;

public interface Input {
	public Collection<Pointer> getPointers();
	public void addInputListener(InputListener input);
	public void removeInputListener(InputListener input);
	public Collection<Gesture> getGestures();
}
