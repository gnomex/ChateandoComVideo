package br.unioeste.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.unioeste.common.User;
import br.unioeste.messenger.ClientListener;
import br.unioeste.messenger.ClientsList;
import br.unioeste.messenger.MessagesListener;
import br.unioeste.server.client.ClientListSender;
import br.unioeste.server.client.ClientReceiver;
import br.unioeste.server.file.Manager;
import br.unioeste.server.file.ServerTransmission;
import br.unioeste.server.messages.MessageReceiver;
import br.unioeste.server.messages.MulticastSender;
import static br.unioeste.global.SocketConstants.*;

public class ServerMessenger  implements MessagesListener, ClientListener{

	private ExecutorService serverExecutor; // executor for server
	
	private ExecutorService serverClientExecutor;
	
	private ExecutorService serverFileServer;

	private ClientsList clientsConnecteds = new ClientsList();
	
	// start chat server
	public void startServer() 
	{
		// create executor for server runnables
		serverExecutor = Executors.newCachedThreadPool();
		
		serverClientExecutor = Executors.newCachedThreadPool();
		
		serverFileServer = Executors.newCachedThreadPool();
		
		
		//Start de server file
		serverFileServer.execute(new ServerTransmission());
		serverFileServer.execute(new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    new Manager();
                } catch (Exception ex) {
                    
                }
            }
        }));
	

		try // create server and manage new clients
		{
			// create ServerSocket for incoming connections
			ServerSocket serverSocket = new ServerSocket( SERVER_PORT, 100 );
			
			InetAddress inet = serverSocket.getInetAddress();

			System.out.println( "Server Messenger listening on: " + inet.getHostName()+ " : " + SERVER_PORT);

			// listen for clients constantly
			while ( true ) 
			{
				// accept new client connection
				Socket clientSocket = serverSocket.accept();

				// add a new client in clientList
				serverClientExecutor.execute(new ClientReceiver(this));
				
				// create MessageReceiver for receiving messages from client
				serverExecutor.execute( 
						new MessageReceiver( this, clientSocket ) );

				// print connection information
				System.out.println( "Connection received from: " +
						clientSocket.getInetAddress() +" Port: " +clientSocket.getPort() );
			
				
				
			} // end while     
		} // end try
		catch ( IOException ioException )
		{
			ioException.printStackTrace();
		} // end catch
	} // end method startServer
	
	// when new message is received, broadcast message to clients
	@Override
	public void messageReceived( String from, String to, String message ) 
	{          
		// create String containing entire message
		String completeMessage = from + MESSAGE_SEPARATOR + to + MESSAGE_SEPARATOR + message;
		if(message.equals(DISCONNECT_STRING)){
			removeUser(from);
		}
		// create and start MulticastSender to broadcast messages
		serverExecutor.execute( 
				new MulticastSender( completeMessage.getBytes() ) );
	} // end method messageReceived

	public void removeUser(String name){
		try{
			
			for(User olduser : clientsConnecteds.getClients()){
				if(olduser.getUserName().equals(name)){
					clientsConnecteds.getClients().remove(olduser);
					System.out.println("Client: " + name + "  " + DISCONNECT_STRING);
					break;
				}
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void clientListReceiver(ClientsList clientList) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void clientreceived( User user) {
		
		try{
			
			System.out.println("New client: " + user.getUserName());
			
			clientsConnecteds.addClient(user);
			
			System.out.println("  Quantidade de clientes conectados: " + clientsConnecteds.getClients().size());
			
			serverClientExecutor.execute(new ClientListSender(clientsConnecteds));
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


} // end class DeitelMessengerServer