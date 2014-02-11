package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.apps.comp.BlackButton;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.io.message.Command;
import org.tse.pri.ioarmband.io.message.GestureMessage;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public class KeyboardApp extends GenericSwingApp implements ActionListener{
	
	boolean isAlpha;
	boolean isActive;
	ArrayList<Component> buttonsList;
	
	public KeyboardApp(Client client, boolean isAlpha) {
		super(client);
		this.isAlpha = isAlpha;
		isActive = false;
		buttonsList = new ArrayList<Component>(); 
	}

	@Override
	public void build(Container container){
		String[][] lettres = getLetterTemplate();
		container.setLayout(new GridLayout(lettres.length, lettres[0].length));
		
		for(int j = 0; j < lettres.length; j++){
			for(int i = 0; i < lettres[j].length; i++){
				JButton button = new BlackButton(lettres[j][i], lettres[j][i], this);
				container.add(button);
				buttonsList.add(button);
			}
		}
	}
	
	private String[][] getLetterTemplate(){
		
		if(isAlpha){
			String[][] tmpLettres = {{"a","z","e","r","t","y","u","i","o","p"},
					{"q","s","d","f","g","h","j","k","l","m"},
					{"w","x","c","v","b","n"," "," ","<",">"}};
			return tmpLettres;
		}else{
			String[][] tmpLettres = {{" ","1","2","3"},
							{" ","4","5","6"},
							{"0","7","8","9"}};
			return tmpLettres;
		}
	}
	
	
	public void actionPerformed(ActionEvent e) {		
		GestureType gestureType = GestureType.TOUCH;
		String source = e.getActionCommand();
		
		dispatchGesture(gestureType, source);		 
		
		GestureMessage message = new GestureMessage(gestureType, source);
		client.sendCommand(message);
	}
}
