package org.ioarmband.controler.apps.impl;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.ioarmband.controler.apps.GenericSwingApp;
import org.ioarmband.controler.apps.AppAnnotations.AppDeclaration;
import org.ioarmband.controler.apps.comp.BlackButton;
import org.ioarmband.controler.net.Client;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.GestureMessage;

@AppDeclaration("slider")
public class SlideSwiperApp extends GenericSwingApp implements ActionListener{

	public SlideSwiperApp(Client client) {
		super(client);
		setSwipeEnabled(true);
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
