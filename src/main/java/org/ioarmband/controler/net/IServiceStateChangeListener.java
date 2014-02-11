package org.ioarmband.controler.net;

import java.util.EventListener;

public interface IServiceStateChangeListener extends EventListener{
	public void onStateChange(IConnectionService element, ServiceState state);
}
