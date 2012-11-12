package br.unioeste.server.file;

import java.io.IOException;

import br.unioeste.server.file.transmission.UDPComunication;
import br.unioeste.util.TemporaryFileList;

import static br.unioeste.global.SocketConstants.*;

/**
 * Classe que gerencia a thread de servidor do repositorio
 */
public class ServerTransmission implements Runnable {

	public static TemporaryFileList tmpFileList = new TemporaryFileList();
	private boolean finish = false;
	
	/**
	 * Metodo que executa as funcoes da thread
	 */
	public void run() {
		// Cria canal de comunicação
		UDPComunication comunicationUDP = new UDPComunication();
		Object object;
		
		System.out.println("Server Transmission works in: " + GROUP +" : "+ UDP_PORT);
		
		while (!finish) {
			try {
				// Fica ouvindo o grupo
				object = comunicationUDP.readGroupObject(GROUP,
						UDP_PORT);
				// Cria uma nova thread assim que chega um objeto no grupo
				new Thread(new MessageReceivedHandler(object), "RECEPTORREP")
						.start();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Método que finaliza o processo servidor
	 */
	public void finish() {
		this.finish = true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerTransmission server = new ServerTransmission();
		System.out
				.println("Iniciando modulo de Repositorios (REPOSITORY)          [OK]");
		
		server.run();
	}
}