package org.tse.pri.ioarmband.armband.io.internal;

import java.util.EventListener;

import org.tse.pri.ioarmband.io.message.Command;

public interface InternalConnectionListener extends EventListener{
	public void onInternalCommandReiceved(Command command);
	public void onInternalConnectionClose();
}
