package org.ioarmband.controler.input;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.GestureMessage;

public class KeyboardSwipeEmulatorInput implements Input, KeyEventDispatcher {

	java.util.Vector<Gesture> gestures;
	
	public KeyboardSwipeEmulatorInput() {
		gestures = new Vector<Gesture>();
		listenKeyboard();
	}
	
	public void listenKeyboard(){
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
	}

	
	@Override
	public boolean dispatchKeyEvent(KeyEvent evt) {
		gestures.clear();
		if(evt.getID() == KeyEvent.KEY_PRESSED) {
			String direction = null;
			switch(evt.getKeyCode()){
				case KeyEvent.VK_K:
					direction = "bottom";
					break;
				case KeyEvent.VK_J:
					direction = "left";
					break;
				case KeyEvent.VK_L:
					direction = "right";
					break;
				case KeyEvent.VK_I:
					direction = "top";
					break;
			}
			
			if(direction != null){
				gestures.add(createSwipeGesture(direction));
				dispatchGestureEvent();
			}
		}
		return false;
	}

	private Gesture createSwipeGesture(String direction){
		Pointer pointer = new Pointer();
		pointer.setDirection(direction);
        
		return new Gesture(GestureType.SWIPE, pointer);
	}
	
	Set<InputListener> inputListeners = new HashSet<InputListener>();
	@Override
	public void addInputListener(InputListener listener) {
		inputListeners.add(listener);
	}
	@Override
	public void removeInputListener(InputListener listener) {
		inputListeners.remove(listener);
	}

	private void dispatchPointersChangeEvent(){
		for (InputListener listener : inputListeners) {
			listener.onPointersChange(getPointers());
		}
	}
	private void dispatchGestureEvent(){
		for (InputListener listener : inputListeners) {
			listener.onGesture(this.gestures);
		}
	}

	@Override
	public Collection<Pointer> getPointers() {
		return new ArrayList<Pointer>();
	}

	@Override
	public Collection<Gesture> getGestures() {
		return this.gestures;
	}

}
