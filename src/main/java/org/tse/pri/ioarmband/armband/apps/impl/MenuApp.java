package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.tools.ImageTools;
import org.tse.pri.ioarmband.io.message.GestureMessage;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

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
	public void build(Container container){
		
		JPanel panel;
		if(index == 0){
			panel = getHome();
		}else{
			panel = getMenu();
		}
			
		/*
		panel.setPreferredSize(appPanel.getSize());
		panel.setVisible(true);
		container.add(panel);
		*/
		container.add(panel);
	}
	
	private JPanel getHome(){
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		panel.setLayout(new GridLayout(2,1));
		JLabel label = new JLabel("ioArmband", JLabel.CENTER);
		Font font = new Font(label.getFont().getName(), label.getFont().getStyle(), 100);
		label.setForeground(Color.WHITE);
		label.setFont(font);
		panel.add(label);
		panel.add(JTime.getInstance());
		
		return panel;
		
	}
	
	private JPanel getMenu(){
		
		JPanel panel = new JPanel();
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
		
		return panel;
	}

	@Override
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
