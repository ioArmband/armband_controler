package org.ioarmband.controler.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.ioarmband.controler.apps.GenericSwingApp;
import org.ioarmband.controler.apps.AppAnnotations.AppDeclaration;
import org.ioarmband.controler.apps.comp.BlackButton;
import org.ioarmband.controler.apps.comp.JChrono;
import org.ioarmband.controler.net.Client;
import org.ioarmband.net.message.Message;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.GestureMessage;

@AppDeclaration("chrono")
public class ChronoApp extends GenericSwingApp implements ActionListener{
	JChrono chrono;
	public ChronoApp(Client client) {
		super(client);
	}

	@Override
	public void build(Container panel) {

		panel.setLayout(new GridLayout(1,2));

		chrono = new JChrono();
		chrono.setForeground(Color.WHITE);
		chrono.setHorizontalAlignment(JChrono.CENTER);
		chrono.setFontSize(120);
		panel.add(chrono);


		JPanel buttonPanel = new JPanel();
			
			buttonPanel.setBackground(Color.DARK_GRAY);
			buttonPanel.setLayout(new GridLayout(4,1));
			BlackButton button;
			
			
			button = new BlackButton("Start","start",this);
			buttonPanel.add(button);
			
			button = new BlackButton("Stop","stop",this);
			buttonPanel.add(button);
			
			button = new BlackButton("Clear","clear",this);
			buttonPanel.add(button);
			
			button = new BlackButton("Quit","quit",this);
			buttonPanel.add(button);
		
		panel.add(buttonPanel);
		
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
