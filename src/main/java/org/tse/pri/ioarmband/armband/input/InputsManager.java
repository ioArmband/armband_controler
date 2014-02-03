package org.tse.pri.ioarmband.armband.input;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.input.Gesture;

public class InputsManager implements InputListener{

	
	private static final Logger logger = Logger.getLogger(InputsManager.class);
	
	private HashMap<Class<?>, Input> inputs;
	
	private static InputsManager __instance;

	private InputsManager() {
		inputs = new HashMap<Class<?>, Input>();
	}

	public static InputsManager getInstance() {
		if (__instance == null) {
			__instance = new InputsManager();
		}
		return __instance;
	}
	
	public void registerInput(Class<?> inputClass){
		if(inputs.get(inputClass) != null){
			logger.warn("registerInput(): Duplication de l'enregistrement de la classe "+ inputClass.getCanonicalName() +". La classe n'a pas été ré-enregistré.");
			return;
		}
		
		try {
			Input input =  (Input) inputClass.newInstance();
			inputs.put(inputClass, input);
			input.addInputListener(this);
		} catch (InstantiationException e) {
			logger.error("registerInput(): La tentative d'ajout de la classe "+ inputClass.getCanonicalName() +" a échoué", e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error("registerInput(): La tentative d'ajout de la classe "+ inputClass.getCanonicalName() +" a échoué", e);
			e.printStackTrace();
		}
	}

	@Override
	public void onPointersChange(Collection<Pointer> pointers) {
		dispatchPointersChangeEvent(pointers);
	}

	@Override
	public void onGesture(Collection<Gesture> gestures) {
		dispatchGestureEvent(gestures);
	}
	
	
	public Collection<Pointer> getPointers(){
		Vector<Pointer> pointers = new Vector<Pointer>();
		for(Input i : inputs.values()){
			pointers.addAll(i.getPointers());
		}
		return pointers;
	}
	Set<InputListener> inputListeners = new HashSet<InputListener>();
	public void addInputListener(InputListener listener) {
		inputListeners.add(listener);
	}
	public void removeInputListener(InputListener listener) {
		inputListeners.remove(listener);
	}
	
	private void dispatchPointersChangeEvent(Collection<Pointer> pointers){
		for (InputListener listener : inputListeners) {
			listener.onPointersChange(pointers);
		}
	}
	private void dispatchGestureEvent(Collection<Gesture> gestures){
		for (InputListener listener : inputListeners) {
			listener.onGesture(gestures);
		}
	}

	public Collection<Gesture> getGestures() {
		Vector<Gesture> gestures = new Vector<Gesture>();
		for(Input i : inputs.values()){
			gestures.addAll(i.getGestures());
		}
		return gestures;
	}
}
