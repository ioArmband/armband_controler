package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;

import javax.swing.JButton;

public class BlackButton extends JButton{
	private static final long serialVersionUID = 1027286116937147087L;

	public BlackButton() {
		super();
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
	}

	public BlackButton(String string) {
		super(string);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
	}
}
