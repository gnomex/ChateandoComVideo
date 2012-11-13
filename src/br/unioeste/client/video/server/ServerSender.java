package br.unioeste.client.video.server;

import static br.unioeste.global.SocketConstants.VIDEO_STREAMING_SEND_PORT;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.unioeste.client.video.SendVideoHandler;
import br.unioeste.server.file.transmission.TCPComunication;

/**
 * Classe que implementa o servidor de Download
 */
public class ServerSender extends Thread {

	private Socket clientSocket;
	private ServerSocket serverSocket;
	private boolean finish = false;
	private TCPComunication comunicationTCP;
	private String username;
	private String touser;

	/**
	 * Construtor da classe
	 * 
	 * @param threadName
	 */
	public ServerSender(String username, String touser, String threadName) {
		super(threadName);
		this.username = username;
		this.touser = touser;
	}

	/**
	 * Metodo que fica ouvindo a porta do servidor a espera de novas conexões
	 */
	@Override
	public void run() {
		try {
			// Cria canal de comunicação
			this.serverSocket = new ServerSocket(VIDEO_STREAMING_SEND_PORT);

			// Aguarda nova conexão
			while (!finish) {
				this.clientSocket = this.serverSocket.accept();
				this.comunicationTCP = new TCPComunication(this.clientSocket);
				new Thread(new SendVideoHandler(this.comunicationTCP, username, touser),
						"sender").start();
			}

			// Fecha canal de comunicação
			this.serverSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que finaliza o processo servidor
	 */
	public void finish() {
		this.finish = true;
	}
}