package org.ioarmband.controler.apps.impl;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import org.ioarmband.controler.apps.GenericSwingApp;
import org.ioarmband.controler.apps.AppAnnotations.AppDeclaration;
import org.ioarmband.controler.apps.comp.BlackButton;
import org.ioarmband.controler.net.Client;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.enums.KeyboardType;
import org.ioarmband.net.message.impl.GestureMessage;

@AppDeclaration("keyboard")
public class KeyboardApp extends GenericSwingApp implements ActionListener{
	
	ArrayList<Component> buttonsList;
	
	public KeyboardApp(Client client) {
		super(client);
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
		
		if(params.equals("alpha")){
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
