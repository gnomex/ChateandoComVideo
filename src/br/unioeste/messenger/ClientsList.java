package br.unioeste.messenger;

import java.io.Serializable;
import java.util.ArrayList;

import br.unioeste.common.User;

public class ClientsList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<User> clients;

	public ClientsList(){
		clients = new ArrayList<User>();
	}
	
	public ArrayList<User> getClients() {
		return clients;
	}

	public void setClients(ArrayList<User> clients) {
		this.clients = clients;
	}

	public void addClient(User user){
		clients.add(user);
	}

}
