package org.tse.pri.ioarmband.armband.io;

import java.util.ArrayList;
import java.util.List;

import org.tse.pri.ioarmband.io.connection.IConnection;

public class ClientsManager implements IClientConnectionCloseListener{
	private Client mainClient;
	private List<Client> clients;
	
	private static ClientsManager __instance;

	private ClientsManager(){
		this.clients = new ArrayList<Client>();
		this.mainClient = null;
	}
	
	public static ClientsManager getInstance(){
		if(__instance == null){
			__instance = new ClientsManager();
		}
		return __instance;
	}
	
	public void switchClient(Client newClient){
		this.mainClient = newClient; 
	}
	public List<Client> getClients(){
		return clients;
	}
	public Client addClient(IConnectionService service, IConnection connection){
		Client client = new Client(service, connection);
		client.addConnectionCloseListener(this);
		if( mainClient == null ){
			mainClient = client;
		}
		clients.add(client);
		return client;
	}
	
	public void removeClient(Client client){
		client.removeConnectionCloseListener(this);
		client.close();
		clients.remove(client);
	}
	
	public void removeClients(IConnectionService connection){
		for (Client client : clients) {
			if(client.getParentConnectionService() == connection){
				removeClient(client);
			}
		}

	}

	@Override
	public void onClientClose(Client client) {
		removeClient(client);
	}
}
