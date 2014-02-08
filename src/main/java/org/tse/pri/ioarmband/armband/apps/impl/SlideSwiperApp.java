package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.io.Client;

public class SlideSwiperApp extends GenericSwingApp{

	public SlideSwiperApp(Client client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	public void build(Container container){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));
		panel.setBackground(Color.BLACK);
		
		JLabel label = new JLabel("<<    >>", JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		Font font = new Font(label.getFont().getName(), label.getFont().getStyle(), 100);
		label.setForeground(Color.WHITE);
		label.setFont(font);
		label.setSize(panel.getSize());
		panel.add(label);
		
		container.add(panel);
	}
	
	public void hide() {
	}
}
