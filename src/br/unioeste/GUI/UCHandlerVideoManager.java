package br.unioeste.GUI;

import static br.unioeste.global.SocketConstants.DISCOVERY_PORT;
import static br.unioeste.global.SocketConstants.DISCOVERY_REPLY_PORT;
import static br.unioeste.global.SocketConstants.GROUP;
import static br.unioeste.global.SocketConstants.MANAGER_ADDR;
import static br.unioeste.global.SocketConstants.VIDEO_STREAMING_RECEIVE_PORT;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import br.unioeste.client.video.ReceiveVideoHandler;
import br.unioeste.client.video.SendVideoHandler;
import br.unioeste.common.Solicitation;
import br.unioeste.server.file.transmission.TCPComunication;
import br.unioeste.server.file.transmission.UDPComunication;

/**
 * Classe que gerencia o Video no modulo do Cliente (Client)
 */
public class UCHandlerVideoManager {
	
	public UCHandlerVideoManager() {
	}

	/**
	 * Metodo que efetua o upload do frame para os repositorios Envia frame
	 * passado como parametro para o modulo de Gerenciamento (Manager)
	 * @throws IOException 
	 */
	public void sendFrames(String username, String touser) throws IOException {
		// Cria Socket de comunicação com o modulo Manager
		Socket socket = new Socket(MANAGER_ADDR, VIDEO_STREAMING_RECEIVE_PORT);
		// Estabelece o canal TCP
		TCPComunication com = new TCPComunication(socket);
		
		System.out
		.println("[Modulo Client] - Enviando frame ao modulo de Gerenciamento");
		SendVideoHandler manager = new SendVideoHandler(com, username, touser);
		new Thread(manager, "frame_sender").start();
	}

	/**
	 * Metodo que efetua o download do frame dos os repositorios Solicita
	 * frame passado como parametro para o modulo de Gerenciamento (Manager)
	 * @throws IOException 
	 */
	public void receiveFrames(String username, String touser) throws IOException {
		// Cria Socket de comunicação com o modulo Manager
		Socket socket = new Socket(MANAGER_ADDR, VIDEO_STREAMING_RECEIVE_PORT);
		// Estabelece o canal TCP
		TCPComunication com = new TCPComunication(socket);
				
		System.out
		.println("[Modulo Client] - Solicitando frame ao modulo de Gerenciamento");
		ReceiveVideoHandler manager = new ReceiveVideoHandler(com, username, touser);
		new Thread(manager, "frame_receiver").start();
	}

	/**
	 * Metodo que localiza o modulo de Gerenciamento na rede
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void locateManager() throws IOException,
	ClassNotFoundException {

		UDPComunication com = new UDPComunication();
		Solicitation solicitation = new Solicitation();

		solicitation.setAddress(InetAddress.getLocalHost().getHostAddress());
		solicitation.setPort(DISCOVERY_REPLY_PORT);
		solicitation.setCode(Solicitation.DISCOVER);

		com.sendObject(GROUP, DISCOVERY_PORT,
				solicitation);

		Solicitation reply = (Solicitation) com
				.readObject(DISCOVERY_REPLY_PORT);

		MANAGER_ADDR = reply.getAddress();
	}
	
	public static void main(String[] args) {
		try {
			new UCHandlerVideoManager().sendFrames("Deivide", "Kenner");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}