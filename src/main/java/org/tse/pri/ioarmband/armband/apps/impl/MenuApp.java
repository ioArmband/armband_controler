package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.io.Client;

public class MenuApp extends GenericSwingApp{

	public MenuApp(Client client) {
		super(client);
		//init();
	}
	
	@Override
	public void build(Container container){
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));
		panel.setBackground(Color.BLACK);
		
		JLabel label = new JLabel("ioArmband", JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		Font font = new Font(label.getFont().getName(), label.getFont().getStyle(), 100);
		label.setForeground(Color.WHITE);
		label.setFont(font);
		label.setSize(panel.getSize());
		panel.add(label);
		
		container.add(panel);
	}

	@Override
	public void hide() {
	}
}
