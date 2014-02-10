package org.tse.pri.ioarmband.armband.apps.comp;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

public class BlackButton extends JButton{
	private static final long serialVersionUID = 1027286116937147087L;

	public BlackButton() {
		super();
		init();
	}

	public BlackButton(String string) {
		super(string);
		init();
	}
	
	public BlackButton(String string, String action) {
		this(string);
		setName(action);
		setActionCommand(action);
	}
	private void init(){
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		Font font = getFont();
		setFont(new Font(font.getName(), font.getStyle(), 100));
	}
}
