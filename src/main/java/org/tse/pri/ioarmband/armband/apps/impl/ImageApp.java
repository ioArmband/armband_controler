package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.io.Client;

public class ImageApp extends GenericSwingApp {

	Image image;
	
	public ImageApp(Client client, Image image) {
		super(client);
		this.image = image;
	}

	@Override
	public void build(Container container) {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));
		panel.setBackground(Color.BLACK);
		System.out.println(image);
		if(image != null){
		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);
		panel.add(label);
		}
		else{
			JLabel label = new JLabel("No-Image :(");
			panel.add(label);
		}
		container.add(panel);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

}
