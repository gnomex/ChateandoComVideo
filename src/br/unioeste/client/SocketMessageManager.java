package br.unioeste.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.unioeste.messenger.ManageMessages;
import br.unioeste.messenger.MessagesListener;
import static br.unioeste.global.SocketConstants.*;

public class SocketMessageManager implements ManageMessages{
	
	   private Socket clientSocket; // Socket for outgoing messages
	   private String serverAddress; // MessengerServer address
	   private PacketReceiver receiver; // receives multicast message
	   private boolean connected = false; // connection status
	   private ExecutorService serverExecutor; // executor for server
	   	   
	   
	   public SocketMessageManager( String address )
	   {
	      serverAddress = address; // store server address
	      serverExecutor = Executors.newCachedThreadPool();
	      
	   } // end SocketMessageManager constructor
	   
	   // connect to server and send messages to given MessageListener
	   public void connect( MessagesListener listener, User user ) 
	   {
		   
	      if ( connected )
	         return; // if already connected, return immediately

	      try // open Socket connection
	      {
	         clientSocket = new Socket( 
	            InetAddress.getByName( serverAddress ), SERVER_PORT );
	         
	         // create Runnable for receiving incoming messages
	         receiver = new PacketReceiver( listener , user.getUserTag() );
	         serverExecutor.execute( receiver ); // execute Runnable
	         connected = true; // update connected flag
	         
	      } // end try
	      catch ( IOException ioException ) 
	      {
	         ioException.printStackTrace();
	      } // end catch
	   } // end method connect
	   
	   // disconnect from server and unregister given MessageListener
	   public void disconnect( MessagesListener listener ) 
	   {
	      if ( !connected )
	         return; // if not connected, return immediately
	      
	      try // stop listener and disconnect from server
	      {     
	         // notify server that client is disconnecting
	         Runnable disconnecter = new MessageSender( clientSocket, "", "",
	            DISCONNECT_STRING );         
	         Future disconnecting = serverExecutor.submit( disconnecter );         
	         disconnecting.get(); // wait for disconnect message to be sent
	         receiver.stopListening(); // stop receiver
	         clientSocket.close(); // close outgoing Socket
	      } // end try
	      catch ( ExecutionException exception ) 
	      {
	         exception.printStackTrace();
	      } // end catch
	      catch ( InterruptedException exception ) 
	      {
	         exception.printStackTrace();
	      } // end catch
	      catch ( IOException ioException ) 
	      {
	         ioException.printStackTrace();
	      } // end catch
	     
	      connected = false; // update connected flag
	   } // end method disconnect
	   
	   // send message to server
	   public void sendMessage( String from, String to, String message ) 
	   {
	      if ( !connected )
	         return; // if not connected, return immediately
	      
	      // create and start new MessageSender to deliver message
	      serverExecutor.execute( 
	         new MessageSender( clientSocket, from,to , message) );
	   } // end method sendMessage
	

	} 