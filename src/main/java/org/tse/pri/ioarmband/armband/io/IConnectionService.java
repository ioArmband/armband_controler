package org.tse.pri.ioarmband.armband.io;

import java.util.EventListener;

public interface IConnectionService extends EventListener{
	public void start();
	public void stop();
	public ServiceState getState();
	public void addStateChangeListener(IServiceStateChangeListener listener);
	public void removeStateChangeListener(IServiceStateChangeListener listener);
}
