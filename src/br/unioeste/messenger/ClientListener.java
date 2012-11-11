package br.unioeste.messenger;

import br.unioeste.client.User;

public interface ClientListener {
	
	//New client received
	public void clientreceived( User user);
	
	public void clientListReceiver( ClientsList clientList);
	

}
