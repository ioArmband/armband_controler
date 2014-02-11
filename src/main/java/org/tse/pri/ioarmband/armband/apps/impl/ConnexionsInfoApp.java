package org.tse.pri.ioarmband.armband.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.apps.GenericSwingApp;
import org.tse.pri.ioarmband.armband.apps.comp.BlackButton;
import org.tse.pri.ioarmband.armband.apps.comp.BlackLabel;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.io.ConnectionsManager;
import org.tse.pri.ioarmband.armband.io.IConnectionService;
import org.tse.pri.ioarmband.armband.io.IServiceStateChangeListener;
import org.tse.pri.ioarmband.armband.io.ServiceState;
import org.tse.pri.ioarmband.io.message.GestureMessage;
import org.tse.pri.ioarmband.io.message.Message;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public class ConnexionsInfoApp extends GenericSwingApp implements ActionListener, IServiceStateChangeListener{

	JPanel mainPanel;
	LinkedHashMap<IConnectionService, JButton> buttons;
	public ConnexionsInfoApp(Client client) {
		super(client);

		buttons = new LinkedHashMap<IConnectionService, JButton>();
		Collection<IConnectionService> services = ConnectionsManager.getInstance().getServices();
		for (IConnectionService service : services) {
			BlackButton btn = new BlackButton("Start", service.getName());
			btn.addActionListener(this);
			service.addStateChangeListener(this);
			buttons.put(service, btn);
		}
	}

	@Override
	public void build(Container container) {
		mainPanel = new JPanel();
		mountPanel();
		container.add(mainPanel);
	}

	private JPanel getServiceControlPanel(IConnectionService service) {
		JPanel panel = new JPanel(new GridLayout(1,3));
		JButton btn = buttons.get(service);
		switch (service.getState()) {
		case STARTED:
			btn.setText("Stop");
			btn.setEnabled(true);
			break;
		case STOPPED:
			btn.setText("Start");
			btn.setEnabled(true);
			break;

		default:
			btn.setEnabled(false);
			break;
		}
		panel.setBackground(Color.BLACK);
		panel.add(new BlackLabel(service.getName()));
		panel.add(new BlackLabel(service.getState().toString()));
		panel.add(btn);
		return panel;
	}

	private void mountPanel(){
		mainPanel.removeAll();
		mainPanel.setBackground(Color.BLACK);
		Collection<IConnectionService> services = ConnectionsManager.getInstance().getServices();
		mainPanel.setLayout(new GridLayout(services.size()+1,1));

		BlackButton button = new BlackButton("Quitter");
		button = new BlackButton("Quit");
		button.setActionCommand("quit");
		button.addActionListener(this);
		mainPanel.add(button);

		for (IConnectionService iConnectionService : services) {
			mainPanel.add(getServiceControlPanel(iConnectionService));
		}

		mainPanel.updateUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String c = e.getActionCommand();
		if(c.equals("quit")){
			Message msg = new GestureMessage(GestureType.TOUCH, "quit");
			client.sendCommand(msg);
			return;
		}

		Collection<IConnectionService> services = ConnectionsManager.getInstance().getServices();
		for (IConnectionService s : services) {
			if(s.getName().equals(c)){
				switch (s.getState()) {
				case STARTED:
					s.stop();
					break;
				case STOPPED:
					s.start();
					break;
				}
			}
		}
	}

	@Override
	public void onStateChange(IConnectionService element, ServiceState state) {
		mountPanel();
	}


}
