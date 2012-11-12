package br.unioeste.client;

import static br.unioeste.global.SocketConstants.SERVER_PORT;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.unioeste.common.User;
import br.unioeste.messenger.ManageVideo;
import br.unioeste.messenger.VideoListener;

public class SocketVideoManager implements ManageVideo {

	private Socket clientSocket; // Socket for outgoing messages
	private String serverAddress; // MessengerServer address
	private PacketVideoReceiver receiver; // receives multicast message
	private boolean connected = false; // connection status
	private ExecutorService serverExecutor; // executor for server

	public SocketVideoManager(String address) {
		serverAddress = address; // store server address
		serverExecutor = Executors.newCachedThreadPool();

	} // end SocketMessageManager constructor

	// connect to server and send messages to given MessageListener
	public void connect(VideoListener listener, User user) {

		if (connected)
			return; // if already connected, return immediately

		try // open Socket connection
		{
			clientSocket = new Socket(InetAddress.getByName(serverAddress),
					SERVER_PORT);

			// create Runnable for receiving incoming messages
			receiver = new PacketVideoReceiver(listener, user.getUserTag());
			serverExecutor.execute(receiver); // execute Runnable
			connected = true; // update connected flag

		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		} // end catch
	} // end method connect

	// disconnect from server and unregister given MessageListener
	public void disconnect(VideoListener listener) {
		if (!connected)
			return; // if not connected, return immediately

		try // stop listener and disconnect from server
		{
			// notify server that client is disconnecting
			receiver.stopListening(); // stop receiver
			clientSocket.close(); // close outgoing Socket
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		} // end catch

		connected = false; // update connected flag
	} // end method disconnect

	// send message to server
	public void sendMessage(String from, String to, BufferedImage message) {
		if (!connected)
			return; // if not connected, return immediately

		// create and start new MessageSender to deliver message
		serverExecutor
				.execute(new VideoSender(clientSocket, from, to, message));
	} // end method sendMessage

}