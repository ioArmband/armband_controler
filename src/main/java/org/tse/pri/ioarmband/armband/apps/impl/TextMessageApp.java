package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.io.Client;

public class TextMessageApp extends GenericSwingApp{

	String author;
	String message;
	Image image;
	
	

	public TextMessageApp(Client client, String author, String message,
			Image image) {
		super(client);
		this.author = author;
		this.message = message;
		this.image = image;
	}


	@Override
	public void build(Container container){
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(Color.BLACK);
		
		GridBagConstraints c = new GridBagConstraints(); 
		JLabel label;

		c.fill = GridBagConstraints.VERTICAL;
		
		if(image != null){
			ImageIcon icon = new ImageIcon(image);
			label = new JLabel("SMS > " + author, icon, JLabel.LEFT);
		}else{
			label = new JLabel("SMS > " + author, JLabel.LEFT);
		}
		label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 100));
		label.setForeground(Color.WHITE);
		c.weighty = 0.3;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(label, c);

		label = new JLabel(message, JLabel.LEFT);
		label.setForeground(Color.WHITE);
		label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 100));
		c.weighty = 0.7;
		c.gridx = 0;
		c.gridy = 1;
		panel.add(label, c);
		
		container.add(panel);
	}

	@Override
	public void hide() {
	}
}
