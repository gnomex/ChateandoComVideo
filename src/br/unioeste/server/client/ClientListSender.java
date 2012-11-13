package br.unioeste.server.client;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import br.unioeste.messenger.ClientsList;

import static br.unioeste.global.SocketConstants.*;

public class ClientListSender implements Runnable{

	private ClientsList cList;

	
	public ClientListSender( ClientsList clients){
		cList = clients;
	}
	
	@Override
	public void run() {
		while(true){
			try{
				DatagramSocket socket = 
						new DatagramSocket( MULTICAST_SENDING_CLIENTS_PORT);

				// use InetAddress reserved for multicast group
				InetAddress group = InetAddress.getByName( MULTICAST_ADDRESS );

				// Serializa o objeto
				ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
				ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut);
				objectOut.flush();
				objectOut.writeObject(cList);
				objectOut.close();

				// Obtém os bytes do objeto serializado
				byte[] clients = byteArrayOut.toByteArray();

				//System.out.println("Sending new clients list");
				//	System.out.println("Tam do buffer: " + clients.length);
				// Envia o objeto
				DatagramPacket sendPacket = new DatagramPacket(clients, clients.length,
						group, MULTICAST_LISTENING_CLIENTS_PORT );
				socket.send(sendPacket);

				// Close stream
				socket.close();
				Thread.sleep(1000);
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}

}
