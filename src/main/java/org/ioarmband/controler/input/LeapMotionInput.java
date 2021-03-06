package org.ioarmband.controler.input;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ioarmband.controler.input.Gesture;
import org.ioarmband.net.message.enums.GestureType;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Matrix;
import com.leapmotion.leap.Vector;

public class LeapMotionInput extends Listener implements Input{


	private static final Logger logger = Logger.getLogger(LeapMotionInput.class);

	java.util.HashMap<Integer, Pointer> pointers;
	java.util.HashMap<Integer,Float> touchCandidates;
	java.util.Vector<Gesture> gestures;
	Controller controller;
	boolean pointerUpdate;
	long timeLast;
	Integer POINTER_TIMEOUT = 200000;
	final float RAPORT_X = 0.025f;
	final float RAPORT_Y = 0.05f;
	final float PRETOUCH_THRESHOLD = 300;
	final float TOUCH_THRESHOLD = 100;
	final float TOUCH_TIMEOUT = 90000;
	final float TOUCH_DISAPEAR_TIMEOUT = 60000;

	public Matrix leap2proj = new Matrix(
			new Vector(0,1,0),
			new Vector(1,0,0),
			new Vector(0,0,-1),
			new Vector(-52,0,59f)
			);


	public LeapMotionInput() {
		pointerUpdate = false;
		pointers =  new HashMap<Integer, Pointer>();
		touchCandidates = new HashMap<Integer, Float>();
		gestures = new java.util.Vector<Gesture>();
		// Create a sample listener and controller
		controller = new Controller();

		// Have the sample listener receive events from the controller
		controller.addListener(this);


	}

	public Vector leapToProjectorCoords(Vector in){
		return leap2proj.transformPoint(in);
	}
	
	public Vector leapToProjectorCoordsDirection(Vector in){
		return leap2proj.transformDirection(in);
	}

	public Vector projectOnScreen(Vector pos){
		Vector p = new Vector();
		p.setX(pos.getX()*RAPORT_X);
		p.setY(pos.getY()*RAPORT_Y);
		p.setZ(pos.getZ());
		return p;
	}


	public void onInit(Controller controller) {
		logger.debug("Initialized");
	}

	public void onConnect(Controller controller) {
		logger.debug("Connected");
		controller.enableGesture(com.leapmotion.leap.Gesture.Type.TYPE_SWIPE);
		//controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
       controller.enableGesture(com.leapmotion.leap.Gesture.Type.TYPE_SCREEN_TAP);
        //controller.enableGesture(com.leapmotion.leap.Gesture.Type.TYPE_KEY_TAP);
       
	}

	public void onDisconnect(Controller controller) {
		logger.debug("Disconnected");
	}

	public void onExit(Controller controller) {
		logger.debug("Exited");
	}

	boolean framedone = true;
	public void onFrame(Controller controller) {
		if(!framedone)
			return;
		framedone = false;
		Frame frame = controller.frame();
		for( Finger finger : frame.fingers()){
			pointerUpdate = true;
			Pointer p = buildPointer(finger);
			pointers.put(p.getId(), p);
		}
		if(pointerUpdate){
			dispatchPointersChangeEvent();
			//pointerUpdate = false;
		}
		gestures.clear();
		for(com.leapmotion.leap.Gesture g : frame.gestures()){
			logger.debug(g.type() +" - " + g.state());

            Pointer pointer;
            Vector pos;
            
			switch(g.type()){
				case TYPE_SWIPE:
		            if(!g.state().equals(com.leapmotion.leap.Gesture.State.STATE_STOP)){
		            	continue;
		            }
					SwipeGesture swipe = new SwipeGesture(g);
		            logger.debug("Swipe id: " + swipe.id()
		                       + ", " + swipe.state()
		                       + ", position: " + swipe.position()
		                       + ", direction: " + swipe.direction()
		                       + ", speed: " + swipe.speed());
		            pointer = new Pointer();
		            pos = projectOnScreen(leapToProjectorCoords(swipe.position()));
		            pointer.setX(pos.getX());
		            pointer.setY(pos.getY());
		            pointer.setSize(pos.getZ());
		            Vector dir = (leapToProjectorCoordsDirection(swipe.direction()));
		            pointer.setDx(dir.getX());
		            pointer.setDy(dir.getY());
		            pointer.setDz(dir.getZ());
		            gestures.add(new Gesture(GestureType.SWIPE, pointer));
		            break;
				case TYPE_SCREEN_TAP:
		            ScreenTapGesture screenTap = new ScreenTapGesture(g);
		            logger.debug("Screen Tap id: " + screenTap.id()
		                       + ", " + screenTap.state()
		                       + ", position: " + screenTap.position()
		                       + ", direction: " + screenTap.direction());
		            pointer = new Pointer();
		            pos = projectOnScreen(leapToProjectorCoords(screenTap.position()));
		            pointer.setX(pos.getX());
		            pointer.setY(pos.getY());
		            pointer.setSize(pos.getZ());
		            logger.debug(pos);
		            gestures.add(new Gesture(GestureType.TOUCH, pointer));
		            break;
		         case TYPE_KEY_TAP:
			            KeyTapGesture keyTap = new KeyTapGesture(g);
			            logger.debug("Screen Tap id: " + keyTap.id()
			                       + ", " + keyTap.state()
			                       + ", position: " + keyTap.position()
			                       + ", direction: " + keyTap.direction());
			            pointer = new Pointer();
			            pos = projectOnScreen(leapToProjectorCoords(keyTap.position()));
			            pointer.setX(pos.getX());
			            pointer.setY(pos.getY());
			            pointer.setSize(pos.getZ());
			            logger.debug(pos);
			            gestures.add(new Gesture(GestureType.TOUCH, pointer));
			            break;
		            default:
		            	logger.warn("WTF");
			}
		}


		long diff = frame.timestamp() - timeLast;


		if(!gestures.isEmpty()){
			logger.debug(gestures);
			dispatchGestureEvent();
		}


		updatePointers(diff);
		timeLast = frame.timestamp();
		
		framedone = true;
	}


	public Pointer buildPointer(Finger f){
		for(Pointer p : pointers.values()){
			p.setVisibleNow(false);
		}
		Pointer p = pointers.get(f.id());
		if(p == null){
			p = new Pointer();
		}
		p.setVisibleNow(true);
		p.setId(f.id());

		Vector pos = projectOnScreen(leapToProjectorCoords(f.tipPosition()));
		p.setX(pos.getX());
		p.setY(pos.getY());
		p.setDist(pos.getZ());

		Vector v = leapToProjectorCoords(f.tipVelocity());
		p.setDx(v.getX());
		p.setDy(v.getY());
		p.setDz(v.getZ());

		p.setLastUpdate(0.0f);
		return p;
	}

	public void updatePointers(long diff){
		java.util.Vector<Pointer> toRemove = new java.util.Vector<Pointer>();

		for(Pointer p : pointers.values()){
			p.setLastUpdate(p.getLastUpdate()+diff);
			if( p.getLastUpdate() > POINTER_TIMEOUT ){
				pointerUpdate = true;
				toRemove.add(p);
			}
		}
		for(Pointer p: toRemove){
			pointers.remove(p.getId());
		}
	}

	public void updateGestures(long diff){
		gestures.clear();
		boolean gestureUpdate = false;


		java.util.Vector<Integer> toRemove = new java.util.Vector<Integer>();
		//logger.debug(touchCandidates);
		//logger.debug(touchCandidates.size());
		for(Integer id : touchCandidates.keySet()){
			Pointer p = pointers.get(id);
			if(p == null){
				toRemove.add(id);
			}else{
				Float last = touchCandidates.get(id);
				//logger.debug(p.getLastUpdate());
				if((p.getDz() < -TOUCH_THRESHOLD) ){
					//logger.debug("recul");
					gestures.add(new Gesture(GestureType.TOUCH, new Pointer(p)));
					gestureUpdate = true;
					toRemove.add(id);
				}else if((p.getLastUpdate() > TOUCH_DISAPEAR_TIMEOUT)){
					//logger.debug("disapear");
					gestures.add(new Gesture(GestureType.TOUCH, new Pointer(p)));
					gestureUpdate = true;
					toRemove.add(id);
				}else if(last > TOUCH_TIMEOUT){
					toRemove.add(id);
				}
				touchCandidates.put(id, last + diff);
			}
		}
		for(Integer p: toRemove){
			touchCandidates.remove(p);
		}

		//logger.debug(touchCandidates);
		for(Pointer p : pointers.values()){
			if(p.isVisibleNow() && p.getDz() > PRETOUCH_THRESHOLD){
				touchCandidates.put(p.getId(), 0.0f);
			}
		}




		if(gestureUpdate){
			logger.debug(gestures);
			dispatchGestureEvent();
		}

	}

	Set<InputListener> inputListeners = new HashSet<InputListener>();
	public void addInputListener(InputListener listener) {
		inputListeners.add(listener);
	}
	public void removeInputListener(InputListener listener) {
		inputListeners.remove(listener);
	}

	private void dispatchPointersChangeEvent(){
		for (InputListener listener : inputListeners) {
			listener.onPointersChange(this.pointers.values());
		}
	}
	private void dispatchGestureEvent(){
		for (InputListener listener : inputListeners) {
			listener.onGesture(this.gestures);
		}
	}

	@Override
	public Collection<Pointer> getPointers() {
		return pointers.values();
	}

	public java.util.Vector<Gesture> getGestures() {
		return gestures;
	}

}
