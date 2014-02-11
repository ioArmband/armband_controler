package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.apps.comp.BlackLabel;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.tools.ImageTools;
import org.tse.pri.ioarmband.io.message.GestureMessage;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public class TextMessageApp extends GenericSwingApp implements MouseListener{

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
		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(); 

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		
		c.weighty = 0.3; 
		c.gridy = 0;
		container.add(getHeaderPanel(), c);
		
		c.weighty = 0.7; 
		c.gridy = 1;
		container.add(getMessagePanel(), c);
		
		container.addMouseListener(this);
	}

	
	public JPanel getHeaderPanel(){
		BlackLabel label;
		JPanel headPanel = new JPanel(new GridBagLayout());
		headPanel.setBackground(Color.BLACK);
		GridBagConstraints hc = new GridBagConstraints(); 
		
		label = new BlackLabel("<"+ source +">", JLabel.LEFT);
		label.setFontSize(50);
		hc.weighty = 1;
		hc.gridy = 0;
		headPanel.add(label, hc);
		
		
		if(image != null){
			ImageIcon icon = new ImageIcon(ImageTools.resize(image, 150, 150));
			label = new BlackLabel(author, icon, JLabel.LEFT);
			label.setIconTextGap(40);
		}else{
			label = new BlackLabel(author, JLabel.LEFT);
		}
		hc.weighty = 3;
		hc.gridy = 1;
		headPanel.add(label, hc);
		
		
		return headPanel;
	}
	

	public JPanel getMessagePanel(){
		JPanel msgPanel = new JPanel(new GridLayout(1, 1));
		msgPanel.setBackground(Color.DARK_GRAY);
		
		BlackLabel label = new BlackLabel("<html><center>" + message + "</center></html>", JLabel.LEFT);
		label.setBackground(Color.DARK_GRAY);
		
		msgPanel.add(label);
		
		return msgPanel;
	}
	
	
	@Override
	public void mousePressed(MouseEvent e){
		GestureType gestureType = GestureType.TOUCH;
		
		dispatchGesture(gestureType, "msg");
		
		GestureMessage message = new GestureMessage();
		message.setSourceName("msg");
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
