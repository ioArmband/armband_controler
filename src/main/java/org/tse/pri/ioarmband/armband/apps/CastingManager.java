package org.tse.pri.ioarmband.armband.apps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.Timer;

import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.tools.ImageTools;
import org.tse.pri.ioarmband.io.message.CastingImageMessage;
import org.tse.pri.ioarmband.io.message.Message;

public class CastingManager implements ActionListener{

	Client client;
	Timer timer;
	public Integer width;
	public Integer height;
	boolean running;
	private CastingManager(Client client, Integer delay, Integer width, Integer height) {
		super();
		this.client = client;
		this.width = width;
		this.height = height;
		timer = new Timer(delay, this);
		timer.start();
		running = true;
	}
	
	public void clear(){
		running = false;
		timer.stop();
		timer.removeActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		castScreen();	
	}
	
	private void castScreen(){
		if(!running)
			return;
		BufferedImage image = AppsManager.getInstance().getScreenCapture();
		BufferedImage resizedImage = ImageTools.resize(image, width, height);
		String encodedImage = ImageTools.encodeBase64(resizedImage, "jpg");
		Message message = new CastingImageMessage(encodedImage);
		client.sendCommand(message);
	}
	
	

	private static HashMap<Client, CastingManager> castListeners = new HashMap<Client, CastingManager>();
	public static void registerCaster(Client client, Integer delay, Integer width, Integer height) {
		CastingManager casting = new CastingManager(client, delay, width, height);
		if(castListeners.containsValue(client))
			removeCaster(client);
		castListeners.put(client,casting);
	}
	public static void removeCaster(Client client){
		CastingManager casting = castListeners.get(client);
		if(casting != null)
			casting.clear();
		castListeners.remove(client);
	}
}
