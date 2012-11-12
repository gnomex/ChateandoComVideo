package br.unioeste.messenger;

import java.awt.image.BufferedImage;

import br.unioeste.common.User;

public interface ManageVideo {

	// connect to message server and route incoming messages
	// to given MessageListener
	public void connect( VideoListener listener , User user );

	// disconnect from message server and stop routing
	// incoming messages to given MessageListener
	public void disconnect(VideoListener listener );

	// send message to message server
	public void sendMessage( String from, String to, BufferedImage message );		
	
}
