package br.unioeste.client;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import br.unioeste.messenger.ClientListener;
import br.unioeste.messenger.ClientsList;
import static br.unioeste.global.SocketConstants.*;

public class ClientListReceiver implements Runnable{

	private ClientListener clientListener;
	
	public ClientListReceiver( ClientListener listener){
		
		clientListener = listener;
		
	}
	
	@Override
	public void run() {		
		
		try{
			
			InetAddress groupAddress = InetAddress.getByName(MULTICAST_ADDRESS);
			MulticastSocket multicastSocket = new MulticastSocket(MULTICAST_LISTENING_CLIENTS_PORT);
			multicastSocket.joinGroup(groupAddress);

			// Receive data
			byte[] receiveData = new byte[DATA_SIZE];
			DatagramPacket datagramPacket = new DatagramPacket(receiveData,
					receiveData.length);
			multicastSocket.receive(datagramPacket);
			receiveData = datagramPacket.getData();

			// Unpack data
			
			ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(receiveData);
			ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn);
			ClientsList cList = (ClientsList) objectIn.readObject();

			System.out.println(" -- Client list received from " + multicastSocket.getInetAddress() +" Port: " + multicastSocket.getLocalPort());
			System.out.println("Tam do buffer " + receiveData.length);
			// Close streams
			objectIn.close();
			byteArrayIn.close();
			multicastSocket.leaveGroup(groupAddress);
			

			//Refresh client list on GUI
			clientListener.clientListReceiver(cList);
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
