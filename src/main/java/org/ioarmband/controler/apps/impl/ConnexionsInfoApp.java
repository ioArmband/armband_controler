package org.ioarmband.controler.apps.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.ioarmband.controler.apps.GenericSwingApp;
import org.ioarmband.controler.apps.comp.BlackButton;
import org.ioarmband.controler.apps.comp.BlackLabel;
import org.ioarmband.controler.net.Client;
import org.ioarmband.controler.net.ConnectionsManager;
import org.ioarmband.controler.net.IConnectionService;
import org.ioarmband.controler.net.IServiceStateChangeListener;
import org.ioarmband.controler.net.ServiceState;
import org.ioarmband.net.message.Message;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.GestureMessage;

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
				default:
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
