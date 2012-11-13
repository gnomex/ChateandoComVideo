package br.unioeste.client.video.server;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Classe que gerencia o modulo de Gerenciamento
 */
public class Manager {

	/**
	 * Construtor da classe
	 * 
	 * @throws RemoteException
	 */
	public Manager() throws RemoteException {
		// Inicia serviço RMI
//		new RemoteService();
		// Inicia Serviço de envio
		new ServerSender("sender_server").start();
		// Inicia Serviço de recebimento
		new ServerReceiver("receiver_server").start();
		// Inicia Serviço de Descobrimento dinamico de modulo Manager
		new ServerDiscovery("discovery_server").start();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out
					.println("Iniciando modulo de Gerenciamento (MANAGER)          [OK]");
			new Manager();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}