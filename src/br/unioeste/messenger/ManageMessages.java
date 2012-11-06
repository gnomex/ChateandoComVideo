package br.unioeste.messenger;

public interface ManageMessages {

	// connect to message server and route incoming messages
	// to given MessageListener
	public void connect( MessagesListener listener );

	// disconnect from message server and stop routing
	// incoming messages to given MessageListener
	public void disconnect( MessagesListener listener );

	// send message to message server
	public void sendMessage( String from, String message );  

}
