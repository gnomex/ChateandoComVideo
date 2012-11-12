package br.unioeste.global;

public class SocketConstants {
	
	public static final String VIDEO_STREAMING_ADDRESS = "224.0.0.23";
	public static final int VIDEO_STREAMING_RECEIVE_PORT = 60001;
	public static final int VIDEO_STREAMING_SEND_PORT = 60000;

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
	
	public static final String NO_FILES_FOUND = "NoFilesFound";

	// message size (in bytes)
	public static final int MESSAGE_SIZE = 512;
	//Data size (in bytes) For serealized Objects
	public static final int DATA_SIZE = 65508;
	
	public static final int MAX_DATA_READ = 8000;
	public static final int BUFFER_SIZE = 7000;
	public static final int NAME_MAX_SIZE = 500;
	public static final int TCP_PORT = 4000;
	public static final int UDP_PORT = 2000;
	public static final int DELAY = 100;
	public static final int UPLOAD_PORT = 9500;
	public static final int DOWNLOAD_PORT = 9505;
	public static final int DOWNLOAD_RECEIVE_PORT = 9510;
	public static final int DISCOVERY_PORT = 9555;
	public static final int DISCOVERY_REPLY_PORT = 9556;
	public static final int MAX_FAULT = 15;
	public static final String GROUP = "234.5.6.7";
	public static final String SOLICITATION_NAME = "Solicitation";
	public static String MANAGER_ADDR = "localhost";
	public static final String REPOSITORY_PATH = "download/";
	

}
