package br.unioeste.util;

public class PacketCounter {
	
	private static final int PAYLOAD_SIZE = 1500;
	

	public static int getQuantityPacksNecessary(int tamBuffer){
		
		return tamBuffer % PAYLOAD_SIZE;
		
	}
	
	
}
