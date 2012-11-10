package br.unioeste.messenger;

import java.util.ArrayList;

import br.unioeste.client.User;

public interface MessagesListener {

	// receive new chat message
	public void messageReceived( String from, String to, String message );

}
