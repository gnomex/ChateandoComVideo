package br.unioeste.messenger;

import br.unioeste.common.User;

public interface ClientListener {
	
	//New client received
	public void clientreceived( User user);
	
	public void clientListReceiver( ClientsList clientList);
	

}
