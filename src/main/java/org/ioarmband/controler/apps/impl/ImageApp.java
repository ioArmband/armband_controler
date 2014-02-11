package org.ioarmband.controler.apps.impl;

import java.awt.Container;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.ioarmband.controler.apps.GenericSwingApp;
import org.ioarmband.controler.net.Client;

public class ImageApp extends GenericSwingApp {

	Image image;

	public ImageApp(Client client, Image image) {
		super(client);
		this.image = image;
	}

	@Override
	public void build(Container container) {
		if(image != null){
			ImageIcon icon = new ImageIcon(image);
			JLabel label = new JLabel(icon);
			container.add(label);
		}
		else{
			JLabel label = new JLabel("No-Image :(");
			container.add(label);
		}
	}

}
