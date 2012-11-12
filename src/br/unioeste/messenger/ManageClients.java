package br.unioeste.messenger;

import br.unioeste.common.User;

public interface ManageClients {

	public void addClient( User user);
		
	public void getClientsList( ClientListener clientListener);
}
