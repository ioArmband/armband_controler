package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.io.ClientsManager;
import org.tse.pri.ioarmband.io.message.Command;
import org.tse.pri.ioarmband.io.message.GestureMessage;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public class KeyboardApp extends GenericSwingApp implements MouseListener {
	
	private static final Logger logger = Logger.getLogger(KeyboardApp.class);
	
	boolean isAlpha;
	boolean isActive;
	ArrayList<Component> buttonsList;
	
	public KeyboardApp(Client client, boolean isAlpha) {
		super(client);
		this.isAlpha = isAlpha;
		isActive = false;
		buttonsList = new ArrayList<Component>();
		//init();
	}
	
	@Override
	public void build(Container container){
		isActive = true;
		
		JPanel panel = new JPanel();
		
		String[][] lettres;
		
		if(isAlpha){
			String[][] tmpLettres = {{"a","z","e","r","t","y","u","i","o","p"},
					{"q","s","d","f","g","h","j","k","l","m"},
					{"w","x","c","v","b","n"," "," ","<",">"}};
			lettres = tmpLettres;
		}else{
			String[][] tmpLettres = {{" ","1","2","3"},
							{" ","4","5","6"},
							{"0","7","8","9"}};
			lettres = tmpLettres;
		}
		
		
		GridLayout gridLayout = new GridLayout(lettres.length, lettres[0].length);
		panel.setLayout(gridLayout);
		panel.setBackground(Color.BLACK);
		
		for(int j = 0; j < lettres.length; j++){
			for(int i = 0; i < lettres[j].length; i++){
				JButton button = new BlackButton(lettres[j][i]);
				Font font = new Font(button.getFont().getName(), button.getFont().getStyle(), 100);
				button.setFont(font);
				button.setName(lettres[j][i]);
				panel.add(button);
				button.addMouseListener(this);
				buttonsList.add(button);
			}
		}
		

		container.add(panel);
		panel.repaint();
		System.out.println(panel.getComponentCount());
	}
	
	@Override
	public void hide() {
		isActive = false;
		for (Component btn : buttonsList) {
			btn.removeMouseListener(this);
		}
		buttonsList.clear();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(!isActive)
			return;
		
		GestureType gestureType = GestureType.TOUCH;
		String source = e.getComponent().getName();
		
		dispatchGesture(gestureType, source);		 
		
		GestureMessage message = new GestureMessage();
		message.setSourceName(source);
		message.setType(gestureType);
		Command command = new Command(message);
		//System.out.println(e+"----"+clients.toString());
		client.sendCommand(command);
		
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
