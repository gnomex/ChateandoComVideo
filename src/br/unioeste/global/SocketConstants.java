package br.unioeste.global;

public class SocketConstants {

	// address for multicast datagrams
	public static final String MULTICAST_ADDRESS = "224.0.0.0";

	// port for listening for multicast datagrams
	public static final int MULTICAST_LISTENING_PORT = 54321;

	// port for sending multicast datagrams
	public static final int MULTICAST_SENDING_PORT = 5432;

	// port for Socket connections to DeitelMessengerServer
	public static final int SERVER_PORT = 12345;   

	// String that indicates disconnect
	public static final String DISCONNECT_STRING = "DISCONNECT";

	// String that separates the user name from the message body
	public static final String MESSAGE_SEPARATOR = ">>";

	// message size (in bytes)
	public static final int MESSAGE_SIZE = 512;

}
