package org.tse.pri.ioarmband.armband.io;

public interface IConnection {
	public void close();
	public void sendCommand(Command command);
	public void addConnectionListener(IConnectionListener listener);
	public void removeConnectionListener(IConnectionListener listener);
}
