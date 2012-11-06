package br.unioeste.messenger;

public interface MessagesListener {

	// receive new chat message
	public void messageReceived( String from, String message );

}
