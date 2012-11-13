package br.unioeste.server.messages;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import br.unioeste.server.ServicesListener;
import br.unioeste.util.PacketCounter;

import static br.unioeste.global.SocketConstants.*;

public class MulticastSender implements Runnable
{   
	private byte[] messageBytes; // message data
	
	private ServicesListener listener;
	
	private int counter ;

	public MulticastSender( byte[] bytes , ServicesListener serviceListener) 
	{ 
		messageBytes = bytes; // create the message
		listener = serviceListener;
		
		try{
			counter = PacketCounter.getQuantityPacksNecessary(messageBytes.length);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	} // end MulticastSender constructor

	// deliver message to MULTICAST_ADDRESS over DatagramSocket
	public void run() 
	{
		try // deliver message
		{         
			// create DatagramSocket for sending message
			DatagramSocket socket = 
					new DatagramSocket( MULTICAST_SENDING_PORT );

			// use InetAddress reserved for multicast group
			InetAddress group = InetAddress.getByName( MULTICAST_ADDRESS );

			// create DatagramPacket containing message
			DatagramPacket packet = new DatagramPacket( messageBytes, 
					messageBytes.length, group, MULTICAST_LISTENING_PORT );

			socket.send( packet ); // send packet to multicast group
			socket.close(); // close socket
			listener.sentData( messageBytes.length);
			listener.packetsSender(counter);
			
		} // end try
		catch ( IOException ioException ) 
		{ 
			ioException.printStackTrace();
			listener.packetLost(counter);
		} // end catch
	} // end method run
}