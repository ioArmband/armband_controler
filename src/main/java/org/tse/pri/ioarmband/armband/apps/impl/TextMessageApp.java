package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.tools.ImageTools;

public class TextMessageApp extends GenericSwingApp{

	String author;
	String message;
	String source;
	Image image;
	
	

	public TextMessageApp(Client client, String source, String author, String message,
			Image image) {
		super(client);
		this.source = source;
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

		c.fill = GridBagConstraints.HORIZONTAL;
		

		JPanel headPanel = new JPanel(new GridBagLayout());
		headPanel.setBackground(Color.BLACK);
		GridBagConstraints hc = new GridBagConstraints(); 
		

		
		label = new JLabel("<"+ source +">", JLabel.LEFT);
		label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 50));
		label.setForeground(Color.WHITE);
		hc.weighty = 1;
		hc.gridy = 0;
		headPanel.add(label, hc);
		
		
		if(image != null){
			ImageIcon icon = new ImageIcon(ImageTools.resize(image, 150, 150));
			label = new JLabel(author, icon, JLabel.LEFT);
			label.setIconTextGap(40);
		}else{
			label = new JLabel(author, JLabel.LEFT);
		}

		label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 100));
		label.setForeground(Color.WHITE);
		label.setAlignmentX(0);
		hc.weighty = 3;
		hc.gridy = 1;
		
		headPanel.add(label, hc);


		c.weightx = 1;
		c.weighty = 0.3;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(headPanel, c);
		JPanel msgPanel = new JPanel(new GridLayout(1, 1));
		msgPanel.setBackground(Color.DARK_GRAY);
		label = new JLabel("<html><center>" + message + "</center></html>", JLabel.LEFT);
		label.setBackground(Color.DARK_GRAY);
		label.setForeground(Color.WHITE);
		label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 100));
		msgPanel.add(label);
		c.weightx = 1;
		c.weighty = 0.7;
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		panel.add(msgPanel, c);
		
		container.add(panel);
	}

	@Override
	public void hide() {
	}
}
