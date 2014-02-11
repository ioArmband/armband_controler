package org.ioarmband.controler.input;

import java.util.Collection;

import org.ioarmband.controler.input.Gesture;

public interface InputListener {
	public void onPointersChange(Collection<Pointer> pointers);
	public void onGesture(Collection<Gesture> gestures);
}
