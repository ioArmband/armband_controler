package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.apps.comp.BlackButton;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.io.message.Command;
import org.tse.pri.ioarmband.io.message.GestureMessage;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public class SlideSwiperApp extends GenericSwingApp implements MouseListener{

	public SlideSwiperApp(Client client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	public void build(Container container){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.setBackground(Color.BLACK);

		BlackButton button;
		button = new BlackButton("<<");
		button.setName("left");
		button.addMouseListener(this);

		Font font = new Font(button.getFont().getName(), button.getFont().getStyle(), 200);
		button.setFont(font);
		panel.add(button);
		button = new BlackButton(">>");
		button.setName("right");
		button.addMouseListener(this);
		button.setFont(font);
		panel.add(button);
		/*
		JLabel label = new JLabel("<<        >>", JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		Font font = new Font(label.getFont().getName(), label.getFont().getStyle(), 200);
		label.setForeground(Color.WHITE);
		label.setFont(font);
		label.setSize(panel.getSize());
		panel.add(label);
		*/
		
		container.add(panel);
	}
	
	public void hide() {
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		GestureType gestureType = GestureType.TOUCH;
		String source = e.getComponent().getName();
		
		dispatchGesture(gestureType, source);
		
		GestureMessage message = new GestureMessage();
		message.setSourceName(source);
		message.setType(gestureType);
		client.sendCommand(message);
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
