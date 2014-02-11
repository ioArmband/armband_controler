package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.apps.comp.BlackButton;
import org.tse.pri.ioarmband.armband.apps.comp.JChrono;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.io.message.GestureMessage;
import org.tse.pri.ioarmband.io.message.Message;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public class ChronoApp extends GenericSwingApp implements ActionListener{
	JChrono chrono;
	public ChronoApp(Client client) {
		super(client);
	}

	@Override
	public void build(Container container) {

		Font font;

		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		panel.setLayout(new GridLayout(1,2));


		chrono = new JChrono();
		chrono.setForeground(Color.WHITE);
		chrono.setHorizontalAlignment(JChrono.CENTER);
		font = new Font(chrono.getFont().getName(), chrono.getFont().getStyle(), 120);
		chrono.setFont(font);
		panel.add(chrono);


		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.DARK_GRAY);
		buttonPanel.setLayout(new GridLayout(4,1));
		BlackButton button;
		
		button = new BlackButton("Start");
		button.setActionCommand("start");
		button.addActionListener(this);
		buttonPanel.add(button);
		
		button = new BlackButton("Stop");
		button.setActionCommand("stop");
		button.addActionListener(this);
		buttonPanel.add(button);
		
		button = new BlackButton("Clear");
		button.setActionCommand("clear");
		button.addActionListener(this);
		buttonPanel.add(button);
		
		button = new BlackButton("Quit");
		button.setActionCommand("quit");
		button.addActionListener(this);
		buttonPanel.add(button);
		
		panel.add(buttonPanel);
		
		container.add(panel);
	}

	@Override
	public void hide() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		if(c.equals("start")){
			chrono.start();
		}
		else if(c.equals("stop")){
			chrono.stop();
		}
		else if(c.equals("clear")){
			chrono.clear();
		}
		else if(c.equals("quit")){
			Message msg = new GestureMessage(GestureType.TOUCH, "quit");
			client.sendCommand(msg);
		}
	}


}
