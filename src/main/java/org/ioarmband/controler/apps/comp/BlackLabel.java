package org.ioarmband.controler.apps.comp;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;

public class BlackLabel extends JLabel{

	private static final long serialVersionUID = -4470997624594806422L;


	public BlackLabel() {
		super();
		init();
	}
	
	
	public BlackLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		init();
	}


	public BlackLabel(Icon image) {
		super(image);
		init();	}


	public BlackLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		init();	}


	public BlackLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		init();	}


	public BlackLabel(String text) {
		super(text);
		init();
	}

	public void setFontSize(int size){
		Font font = new Font(getFont().getName(), getFont().getStyle(), size);
		setFont(font);
	}

	void init(){
		setForeground(Color.WHITE);
		setFontSize(100);
	}
}
