package org.tse.pri.ioarmband.armband.input;

import org.tse.pri.ioarmband.io.message.enums.GestureType;

import com.leapmotion.leap.Gesture.Type;

public class Gesture {
	private GestureType type;
	private Pointer pointer;
	
	public Gesture(GestureType type, Pointer pointer) {
		super();
		this.type = type;
		this.pointer = pointer;
	}
	
	public GestureType getType() {
		return type;
	}
	public void setType(GestureType type) {
		this.type = type;
	}
	public Pointer getPointer() {
		return pointer;
	}
	public void setPointer(Pointer pointer) {
		this.pointer = pointer;
	}

}
