package org.tse.pri.ioarmband.armband.io;

import java.util.EventListener;

public interface IServiceStateChangeListener extends EventListener{
	public void onStateChange(IConnectionService element, ServiceState state);
}
