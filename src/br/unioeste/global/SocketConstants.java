package br.unioeste.global;

public class SocketConstants {

	// address for multicast datagrams
	public static final String MULTICAST_ADDRESS = "224.0.0.3";
	public static final String MULTICAS_CLIENTS_ADDRESS = "224.0.0.26" ;

	// port for listening for multicast datagrams
	public static final int MULTICAST_LISTENING_PORT = 54321;
	// port for sending multicast datagrams messages
	public static final int MULTICAST_SENDING_PORT = 5432;

	// port for listening for multicast datagrams clients
	public static final int MULTICAST_LISTENING_CLIENTS_PORT = 54323;
	// port for sending multicast datagram clients
	public static final int MULTICAST_SENDING_CLIENTS_PORT = 5435;
	
	//Server host name
	public static final String SERVER_ADDRESS = "localhost";
	
	// port for Socket Server connections 
	public static final int SERVER_PORT = 12345;  
	
	//port for Socket Server clients connecteds list
	public static final int SERVER_CLIENTS_PORT = 12347;

	// String that indicates disconnect
	public static final String DISCONNECT_STRING = "DISCONNECT";

	// String that separates the user name from the message body
	public static final String MESSAGE_SEPARATOR = ">>";

	// message size (in bytes)
	public static final int MESSAGE_SIZE = 512;
	//Data size (in bytes) For serealized Objects
	public static final int DATA_SIZE = 65508;

}
