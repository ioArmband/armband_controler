package org.ioarmband.controler.net;

import java.util.EventListener;

public interface ServiceStateChangeListener extends EventListener{
	public void onStateChange(ConnectionService element, ServiceState state);
}
