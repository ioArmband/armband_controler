package org.ioarmband.controler.test;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.ioarmband.controler.tools.ImageTools;
import org.ioarmband.controler.tools.PropertiesManager;
import org.ioarmband.net.connection.IConnection;
import org.ioarmband.net.connection.IConnectionListener;
import org.ioarmband.net.connection.StreamedConnection;
import org.ioarmband.net.message.Command;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.CastingImageMessage;
import org.ioarmband.net.message.impl.CastingRequestMessage;
import org.ioarmband.net.message.impl.GestureMessage;

public class LANClientTester implements IConnectionListener{

	private static final Logger logger = Logger.getLogger(LANClientTester.class);
	
	
	public static void main(String[] args) {

		String host = "127.0.0.1"; //PropertiesManager.getString("connection_service.lan.host");
		int port = PropertiesManager.getInt("connection_service.lan.port");
		LANClientTester client = new LANClientTester(host, port); 
		client.runSocket();
		GestureMessage msg = new GestureMessage();
		//AppMessage kmsg = new AppMessage(AppStd.KEYBOARD_NUM);
		msg.setType(GestureType.TOUCH);
		
		msg.setSourceName("LANClientTester");
		client.sendMessage(new Command(new CastingRequestMessage(200,300,400)));
	}

	JFrame jf;
	JPanel jp;
	private String host;
	private int port;
	private IConnection connection;
	public LANClientTester(String host, int port) {
		this.host = host;
		this.port = port;
		
		jf = new JFrame("ioArmband");
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(200,200);
		jf.setAlwaysOnTop(true);
		jf.setVisible(true);
		jp = new JPanel();
		jf.getContentPane().add(jp);
	}

	public void runSocket(){
		Socket socket;
		
		try {
			socket = new Socket(host, port);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			connection = new StreamedConnection(in, out);
			
			connection.addConnectionListener(this);
		} catch (UnknownHostException e) {
			logger.error("Cannot reach the host " + host + ".", e);
		} catch (IOException e) {
			logger.error("Lan Client error", e);
		}
	}
	public void sendMessage(Command command){
		if(connection == null){
			logger.error("SendMessage: Try to send command but no connection is engaged");
			return;
		}
		connection.sendCommand(command);
	}
	@Override
	public void onCommandReiceved(Command command) {
		logger.info("Command received : " + command);
			CastingImageMessage msg = (CastingImageMessage) command.getMessage();
			Image im = ImageTools.decodeBase64(msg.getEncodedImage());
			jp.removeAll();
			jp.add(new JLabel(new ImageIcon(im)));
			jp.updateUI();
			logger.info(im);
	}


	@Override
	public void onConnectionClose() {
		// TODO Auto-generated method stub
		
	}
}
