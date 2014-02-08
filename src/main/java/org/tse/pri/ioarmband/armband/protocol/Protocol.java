package org.tse.pri.ioarmband.armband.protocol;

import java.util.Map;

import org.tse.pri.ioarmband.armband.io.Client;

public interface Protocol {
	public void exec(Client client, String commandName, Map<String, String> inputParams);
}
