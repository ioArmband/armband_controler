package org.tse.pri.ioarmband.armband.protocol;

public class ProtocolManager {
	
	private static Protocol _protocol;
	

	public static Protocol getProtocol() {
		if (_protocol == null) {
			_protocol = new ReceptionProtocol();
		}
		return _protocol;
	}
	
}
