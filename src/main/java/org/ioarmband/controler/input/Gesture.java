package org.ioarmband.controler.input;

import org.ioarmband.net.message.enums.GestureType;

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

	@Override
	public String toString() {
		return "Gesture [type=" + type + ", pointer=" + pointer + "]";
	}

}
