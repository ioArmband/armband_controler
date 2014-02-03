package org.tse.pri.ioarmband.armband.input;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;

import org.tse.pri.ioarmband.armband.input.Gesture;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

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
	
	public Vector projectOnScreen(Vector pos){
		Vector p = new Vector();
		p.setX(pos.getX()*RAPORT_X);
		p.setY(pos.getY()*RAPORT_Y);
		p.setZ(pos.getZ());
		return p;
	}
	

    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        /*
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        */
    }

    public void onDisconnect(Controller controller) {
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
    	Frame frame = controller.frame();
        //System.out.println("Frame" + frame.id());
        for( Finger finger : frame.fingers()){
        	pointerUpdate = true;
        	Pointer p = buildPointer(finger);
        	pointers.put(p.getId(), p);
        }
        
        
        long diff = frame.timestamp() - timeLast;
        
        updateGestures(diff);
        
        
        if(pointerUpdate){
            dispatchPointersChangeEvent();
            pointerUpdate = false;
        }

        updatePointers(diff);
        
        timeLast = frame.timestamp();
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
		//System.out.println(touchCandidates);
		//System.out.println(touchCandidates.size());
    	for(Integer id : touchCandidates.keySet()){
    		Pointer p = pointers.get(id);
    		if(p == null){
    			toRemove.add(id);
    		}else{
	    		Float last = touchCandidates.get(id);
	    		//System.out.println(p.getLastUpdate());
	    		if((p.getDz() < -TOUCH_THRESHOLD) ){
	    			System.out.println("recul");
	    			gestures.add(new Gesture(GestureType.TOUCH, new Pointer(p)));
	    			gestureUpdate = true;
	    			toRemove.add(id);
	    		}else if((p.getLastUpdate() > TOUCH_DISAPEAR_TIMEOUT)){
	    			System.out.println("disapear");
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
    	
    	//logger.info(touchCandidates);
    	for(Pointer p : pointers.values()){
    		if(p.isVisibleNow() && p.getDz() > PRETOUCH_THRESHOLD){
        		touchCandidates.put(p.getId(), 0.0f);
    		}
    	}
    	


    	
    	if(gestureUpdate){
    		System.out.println(gestures);
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
