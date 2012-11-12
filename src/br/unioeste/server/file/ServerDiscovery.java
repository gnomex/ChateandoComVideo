package br.unioeste.server.file;

import java.io.IOException;
import br.unioeste.common.handler.DiscoveryHandler;
import br.unioeste.server.file.transmission.UDPComunication;

import static br.unioeste.global.SocketConstants.*;

public class ServerDiscovery extends Thread {

	private boolean finish = false;

	/**
	 * Construtor da classe
	 * 
	 * @param threadName
	 */
	public ServerDiscovery(String threadName) {
		super(threadName);
	}

	/**
	 * Metodo que fica ouvindo a porta do servidor a espera de novas conexões
	 */
	@Override
	public void run() {

		UDPComunication com = new UDPComunication();
		Object object;

		while (!this.finish) {

			try {
				// Fica ouvindo o grupo
				object = com.readGroupObject(GROUP,
						DISCOVERY_PORT);

				// Cria uma nova thread assim que chega um objeto no grupo
				new Thread(new DiscoveryHandler(object), "DISCOVERY").start();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
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
}
