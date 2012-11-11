package br.unioeste.messenger;

import br.unioeste.client.User;

public interface ManageClients {

	public void addClient( User user);
		
	public void getClientsList( ClientListener clientListener);
}
