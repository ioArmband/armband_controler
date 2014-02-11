package org.ioarmband.controler.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import org.ioarmband.controler.apps.GenericSwingApp;
import org.ioarmband.controler.apps.comp.JTime;
import org.ioarmband.controler.net.Client;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.GestureMessage;

public class MenuApp extends GenericSwingApp implements MouseListener{
	Integer index;
	Boolean isLast;
	String appName;
	
	public MenuApp(Client client, Integer index, Boolean isLast,
			String appName) {
		super(client);
		this.index = index;
		this.isLast = isLast;
		this.appName = appName;
	}
	
	@Override
	public void build(Container panel){
		if(index == 0){
			buildHome(panel);
		}else{
			buildMenu(panel);
		}
	}
	
	private void buildHome(Container panel){
		panel.setBackground(Color.BLACK);
		panel.setLayout(new GridLayout(2,1));
		JLabel label = new JLabel("ioArmband", JLabel.CENTER);
		Font font = new Font(label.getFont().getName(), label.getFont().getStyle(), 100);
		label.setForeground(Color.WHITE);
		label.setFont(font);
		panel.add(label);
		panel.add(JTime.getInstance());
		
	}
	
	private void buildMenu(Container panel){
		panel.addMouseListener(this);
		panel.setLayout(new GridBagLayout());
		panel.setBackground(Color.BLACK);
		
		GridBagConstraints c = new GridBagConstraints(); 
		JLabel label;

		c.fill = GridBagConstraints.HORIZONTAL;
		
		label = new JLabel(appName);
		label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 130));
		label.setForeground(Color.WHITE);
		c.weighty = 1;
		c.gridy = 0;
		panel.add(label, c);
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
