package br.unioeste.client;

import br.unioeste.GUI.ClientGUI;
import br.unioeste.messenger.ManageMessages;


public class Messenger {

	public static void main( String args[] ) 
	   {
	     ManageMessages messageManager; // declare MessageManager
	      
	      if ( args.length == 0 )
	         // connect to localhost
	         messageManager = new SocketMessageManager( "localhost" );
	      else
	         // connect using command-line arg
	         messageManager = new SocketMessageManager( args[ 0 ] );  
	      
	      // create GUI for SocketMessageManager
	      ClientGUI clientGUI = new ClientGUI( messageManager );
	      clientGUI.setVisible( true ); // show window
	   }
	
}
