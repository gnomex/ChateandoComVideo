package br.unioeste.messenger;

import java.awt.image.BufferedImage;

public interface VideoListener {

	// receive new chat message
	public void messageReceived( String from, String to, BufferedImage message );

}
