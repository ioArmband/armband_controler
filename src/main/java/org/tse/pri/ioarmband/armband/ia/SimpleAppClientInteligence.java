package org.tse.pri.ioarmband.armband.ia;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.protocol.ProtocolExecutor.CommandExecutor;
import org.tse.pri.ioarmband.armband.protocol.ProtocolExecutor.CommandParam;
import org.tse.pri.ioarmband.io.message.AppMessage;
import org.tse.pri.ioarmband.io.message.Message;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public class SimpleAppClientInteligence extends InternalClientInteligence{
	
	private static final Logger logger = Logger
			.getLogger(SimpleAppClientInteligence.class);
	
	private String appName = "";
	
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
		if(type.equals(GestureType.TOUCH) && sourceName.equals("stop")){
			close();
		}
	}
	
}
