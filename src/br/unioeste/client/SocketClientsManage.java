package br.unioeste.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.unioeste.client.clients.ClientListReceiver;
import br.unioeste.client.clients.ClientSender;
import br.unioeste.common.User;
import br.unioeste.messenger.ClientListener;
import br.unioeste.messenger.ManageClients;

public class SocketClientsManage implements ManageClients{

	private ClientListReceiver receiver; //Receives clients 
	private boolean connected = false;
	private ExecutorService serverExecutor;


	public SocketClientsManage(){

		serverExecutor = Executors.newCachedThreadPool();

	}

	public void addClient( User user) {

		if ( connected )
			return; // if already connected, return immediately

		try{
			
			ClientSender clientSender = new ClientSender( user );
			
			serverExecutor.execute(clientSender);
			connected = true;
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void getClientsList(ClientListener clientListener) {
		if ( !connected )
			return; // if already connected, return immediately

		try{

			receiver = new ClientListReceiver( clientListener);
			serverExecutor.execute(receiver);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
