package br.unioeste.client;

import static br.unioeste.global.SocketConstants.MESSAGE_SIZE;
import static br.unioeste.global.SocketConstants.MULTICAST_ADDRESS;
import static br.unioeste.global.SocketConstants.MULTICAST_LISTENING_PORT;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import br.unioeste.common.Frame;
import br.unioeste.messenger.VideoListener;

public class PacketVideoReceiver implements Runnable {
	private VideoListener videoListener; // receives frames
	private MulticastSocket multicastSocket; // receive broadcast messages
	private InetAddress multicastGroup; // InetAddress of multicast group
	private boolean keepListening = true; // terminates PacketReceiver

	private String userTag;

	public PacketVideoReceiver(VideoListener listener, String userTag) {
		videoListener = listener; // set MessageListener

		this.userTag = userTag;

		try // connect MulticastSocket to multicast address and port
		{
			// create new MulticastSocket
			multicastSocket = new MulticastSocket(MULTICAST_LISTENING_PORT);

			// use InetAddress to get multicast group
			multicastGroup = InetAddress.getByName(MULTICAST_ADDRESS);

			// join multicast group to receive messages
			multicastSocket.joinGroup(multicastGroup);

			// set 5 second timeout when waiting for new packets
			multicastSocket.setSoTimeout(5000);
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		} // end catch
	} // end PacketReceiver constructor

	// listen for messages from multicast group
	public void run() {
		// listen for messages until stopped
		while (keepListening) {
			// create buffer for incoming message
			byte[] buffer = new byte[MESSAGE_SIZE];

			// create DatagramPacket for incoming message
			DatagramPacket packet = new DatagramPacket(buffer, MESSAGE_SIZE);

			try // receive new DatagramPacket (blocking call)
			{
				multicastSocket.receive(packet);
			} // end try
			catch (SocketTimeoutException socketTimeoutException) {
				continue; // continue to next iteration to keep listening
			} // end catch
			catch (IOException ioException) {
				ioException.printStackTrace();
				break;
			} // end catch

			// put message data in a String
			Frame message = null;
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
			ObjectInput in = null;
			try {
			  in = new ObjectInputStream(bis);
			  Object o = in.readObject();
			  message = (Frame) o;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bis.close();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			  
			  String touser = (String) message.getTouser();
			  String username = (String) message.getUsername();
			  BufferedImage dataMessage = (BufferedImage) message.getMessage();
			  
			  if (userTag.equalsIgnoreCase(touser)) {
				// send message to MessageListener
				videoListener.messageReceived(username, touser, dataMessage); // message body
				} else {
					System.out.println("This message is not for me. " + userTag
							+ " says.");
				}
			}
		} // end while

		try {
			multicastSocket.leaveGroup(multicastGroup); // leave group
			multicastSocket.close(); // close MulticastSocket
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		} // end catch
	} // end method run

	// stop listening for new messages
	public void stopListening() {
		keepListening = false;
	} // end method stopListening
}