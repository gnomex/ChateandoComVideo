package br.unioeste.server;

import java.io.IOException;
import java.util.ArrayList;

import br.unioeste.server.file.transmission.UDPComunication;
import br.unioeste.util.TemporaryFileList;

import static br.unioeste.global.SocketConstants.*;

/**
 * Classe que gerencia a thread de servidor do repositorio
 */
public class Server implements Runnable {

	public static TemporaryFileList tmpFileList = new TemporaryFileList();
	private boolean finish = false;
	private ArrayList<Listener> listeners = new ArrayList<Listener>();

	public void addEventListener(Listener l) {
		listeners.add(l);
	}
	
	/**
	 * Metodo que executa as funcoes da thread
	 */
	public void run() {
		// Cria canal de comunicação
		UDPComunication comunicationUDP = new UDPComunication();
		Object object;
		while (!finish) {
			try {
				// Fica ouvindo o grupo
				object = comunicationUDP.readGroupObject(GROUP,
						UDP_PORT);
				// Cria uma nova thread assim que chega um objeto no grupo
				new Thread(new MessageReceivedHandler(object, listeners.get(0)), "RECEPTORREP")
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
		//Server server = new Server();
		System.out
				.println("Iniciando modulo de Repositorios (REPOSITORY)          [OK]");
		
		//server.run();
	}
}