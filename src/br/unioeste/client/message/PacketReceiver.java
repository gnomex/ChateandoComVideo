package br.unioeste.client.message;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

import br.unioeste.messenger.MessagesListener;
import static br.unioeste.global.SocketConstants.*;


public class PacketReceiver implements Runnable 
{
	   private MessagesListener messageListener; // receives messages
	   private MulticastSocket multicastSocket; // receive broadcast messages
	   private InetAddress multicastGroup; // InetAddress of multicast group
	   private boolean keepListening = true; // terminates PacketReceiver
	   
	   private String userTag;
	   	   
	   public PacketReceiver( MessagesListener listener , String userTag ) 
	   {
	      messageListener = listener; // set MessageListener
	      
	      this.userTag = userTag;
	      
	      try // connect MulticastSocket to multicast address and port 
	      {
	         // create new MulticastSocket
	         multicastSocket = new MulticastSocket(
	            MULTICAST_LISTENING_PORT );
	         
	         // use InetAddress to get multicast group
	         multicastGroup = InetAddress.getByName( MULTICAST_ADDRESS );
	         
	         // join multicast group to receive messages
	         multicastSocket.joinGroup( multicastGroup ); 
	         
	         // set 5 second timeout when waiting for new packets
	         multicastSocket.setSoTimeout( 5000 );
	      } // end try
	      catch ( IOException ioException ) 
	      {
	         ioException.printStackTrace();
	      } // end catch
	   } // end PacketReceiver constructor
	   
	   // listen for messages from multicast group 
	   public void run() 
	   {          
	      // listen for messages until stopped
	      while ( keepListening ) 
	      {
	         // create buffer for incoming message
	         byte[] buffer = new byte[ MESSAGE_SIZE ];

	         // create DatagramPacket for incoming message
	         DatagramPacket packet = new DatagramPacket( buffer, 
	            MESSAGE_SIZE );

	         try // receive new DatagramPacket (blocking call)
	         {            
	            multicastSocket.receive( packet ); 
	         } // end try
	         catch ( SocketTimeoutException socketTimeoutException ) 
	         {
	            continue; // continue to next iteration to keep listening
	         } // end catch
	         catch ( IOException ioException ) 
	         {
	            ioException.printStackTrace();
	            break;
	         } // end catch

	         // put message data in a String
	         String message = new String( packet.getData() );

	         message = message.trim(); // trim whitespace from message

	         // tokenize message to retrieve user name and message body
	         StringTokenizer tokenizer = new StringTokenizer( 
	            message, MESSAGE_SEPARATOR );

	         // ignore messages that do not contain a user 
	         // name and message body
	         if ( tokenizer.countTokens() == 3 ){       	 
	        	 
	        	 String username, touser, dataMessage;
	        	 username = tokenizer.nextToken();
	        	 touser = tokenizer.nextToken();
	        	 dataMessage = tokenizer.nextToken();
	        	 
	        	 
	        	 if(userTag.equalsIgnoreCase(touser) || "all".equals(touser)){
	 	            // send message to MessageListener
	 	            messageListener.messageReceived( 
	 	               username, // user name
	 	               touser, //To user
	 	               dataMessage ); // message body
	        	 }
	        	 

	         } // end if
	      } // end while

	      try 
	      {
	         multicastSocket.leaveGroup( multicastGroup ); // leave group
	         multicastSocket.close(); // close MulticastSocket
	      } // end try
	      catch ( IOException ioException )
	      { 
	         ioException.printStackTrace();
	      } // end catch
	   } // end method run
	   
	   // stop listening for new messages
	   public void stopListening() 
	   {
	      keepListening = false;
	   } // end method stopListening
	}