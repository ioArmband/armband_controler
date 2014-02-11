package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.apps.comp.BlackButton;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.io.message.GestureMessage;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public class SlideSwiperApp extends GenericSwingApp implements ActionListener{

	public SlideSwiperApp(Client client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	public void build(Container container){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));

		BlackButton button;
		button = new BlackButton("<<", "left", this);
		button.setFontSize(200);
		panel.add(button);
		button = new BlackButton(">>","right", this);
		button.setFontSize(200);
		panel.add(button);
		
		container.add(panel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GestureType gestureType = GestureType.TOUCH;
		String source = e.getActionCommand();
		
		dispatchGesture(gestureType, source);
		
		GestureMessage message = new GestureMessage();
		message.setSourceName(source);
		message.setType(gestureType);
		client.sendCommand(message);
	}
}
