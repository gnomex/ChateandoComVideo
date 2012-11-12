package br.unioeste.server;

public interface ServicesListener {
	
	public void packetsReceived( int counter);
	
	public void packetsSender( int counter);
	
	public void packetLost(int counter);
	
	public void registerLog(String status);
	
	public void clientsCounter( int counter);
	
	public void receiveData( float buffer);
	
	public void sentData( float buffer);
	
	public void fileDatareceiver( float bufferin);
	
	public void fileDatasent( float bufferout);

}
