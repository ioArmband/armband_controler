package org.tse.pri.ioarmband.armband.input;

import java.util.Collection;

import org.tse.pri.ioarmband.armband.input.Gesture;

public interface InputListener {
	public void onPointersChange(Collection<Pointer> pointers);
	public void onGesture(Collection<Gesture> gestures);
}
