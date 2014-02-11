package org.ioarmband.controler.ia;

import org.ioarmband.controler.net.Client;
import org.ioarmband.controler.protocol.ProtocolExecutor.CommandExecutor;
import org.ioarmband.controler.protocol.ProtocolExecutor.CommandParam;
import org.ioarmband.net.message.Message;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.AppMessage;

public class SimpleAppClientInteligence extends InternalClientInteligence{
	
	private String appName = "";
	
	
	public SimpleAppClientInteligence() {
		super();
	}
	
	public SimpleAppClientInteligence(String appName) {
		super();
		this.appName = appName;
	}

	@Override
	public void start() {
		Message msg = new AppMessage(appName);
		sendMessage(msg);
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppName() {
		return appName;
	}
	
	@CommandExecutor("gesture")
	public void onAppGesture(Client client, @CommandParam("type") GestureType type, @CommandParam("sourceName") String sourceName)
	{
		if(type.equals(GestureType.TOUCH) && sourceName.equals("quit")){
			close();
		}
	}
	
}
