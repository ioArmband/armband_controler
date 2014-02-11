package org.ioarmband.controler.net.service;

import java.util.EventListener;

import org.ioarmband.net.message.Command;

public interface InternalConnectionListener extends EventListener{
	public void onInternalCommandReiceved(Command command);
	public void onInternalConnectionClose();
}
