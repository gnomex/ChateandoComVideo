package br.unioeste.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import br.unioeste.client.User;
import br.unioeste.messenger.ClientListener;
import static br.unioeste.global.SocketConstants.*;

public class ClientReceiver implements Runnable{

	private ObjectInputStream in;
	private ClientListener clientsListener;

	private boolean keepListening = true; // when false, ends runnable

	public ClientReceiver( ClientListener listener){

		clientsListener = listener;

	}

	public void run(){

		User user = new User();

		ServerSocket sServer = null;
		Socket socket = null;

		try {
			sServer = new ServerSocket(SERVER_CLIENTS_PORT);
				
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(keepListening){

			try {
				socket = sServer.accept();

				in = new ObjectInputStream(socket.getInputStream());

				user = (User) in.readObject();

				if(!user.getUserName().isEmpty()){
					
					clientsListener.clientreceived(user);
					// if received a new client, stop listening
					keepListening = false;
					
				}
			}catch ( SocketTimeoutException socketTimeoutException ){

				continue; // continue to next iteration to keep listening
			} 
			catch ( IOException ioException ){

				ioException.printStackTrace();            
				break;
			} // end catch
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try{
			in.close();
			socket.close();
			sServer.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
