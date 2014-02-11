package org.ioarmband.controler.net;

import java.util.EventListener;

public interface ConnectionService extends EventListener{
	public void start();
	public void stop();
	public ServiceState getState();
	public void addStateChangeListener(ServiceStateChangeListener listener);
	public void removeStateChangeListener(ServiceStateChangeListener listener);
	public String getName();
}
