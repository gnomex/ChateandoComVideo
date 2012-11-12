package br.unioeste.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import br.unioeste.common.Frame;

public class VideoSender implements Runnable {
	private Socket clientSocket; // Socket over which to send message
	private Frame message2Send;

	public VideoSender(Socket socket, String userName, String toUser,
			BufferedImage message) {
		clientSocket = socket; // store Socket for client
		
		message2Send = new Frame(userName, toUser, message);
	}

	// send message and end
	public void run() {
		try // send message and flush formatter
		{
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();
			out.writeObject(message2Send); // send
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
