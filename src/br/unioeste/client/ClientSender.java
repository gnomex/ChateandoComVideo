package br.unioeste.client;

import static br.unioeste.global.SocketConstants.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSender implements Runnable{

	private Socket cSocket;
	private ObjectOutputStream out;
	
	private User newClient;
	
	public ClientSender( User user) throws UnknownHostException{
		
		try {
			cSocket = new Socket(InetAddress.getByName( SERVER_ADDRESS ), SERVER_CLIENTS_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newClient = user;
		
	}
	
	@Override
	public void run() {
		
		try{
			
			out = new ObjectOutputStream(cSocket.getOutputStream());
			out.flush();
			
			out.writeObject(newClient); //Send client
			out.flush();
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try{
			
			out.close();
			cSocket.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
