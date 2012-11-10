package br.unioeste.messenger;

import java.util.ArrayList;

import br.unioeste.client.User;

public interface ClientListener {
	
	public void clientList(ArrayList<User> users);

}
