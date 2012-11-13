package br.unioeste.GUI;

import static br.unioeste.global.SocketConstants.DISCOVERY_PORT;
import static br.unioeste.global.SocketConstants.DISCOVERY_REPLY_PORT;
import static br.unioeste.global.SocketConstants.GROUP;
import static br.unioeste.global.SocketConstants.MANAGER_ADDR;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import br.unioeste.client.video.ReceiveVideoHandler;
import br.unioeste.client.video.SendVideoHandler;
import br.unioeste.common.Solicitation;
import br.unioeste.server.file.transmission.UDPComunication;

/**
 * Classe que gerencia o Video no modulo do Cliente (Client)
 */
public class UCHandlerVideoManager {
	
	private ArrayList<ShowFramesListener> listeners;
	
	public void addEventListener(ShowFramesListener l) {
		if (listeners == null) {
			listeners = new ArrayList<ShowFramesListener>();
		}
		listeners.add(l);
	}
	
	public UCHandlerVideoManager() {
		
	}

	/**
	 * Metodo que efetua o upload do frame para os repositorios Envia frame
	 * passado como parametro para o modulo de Gerenciamento (Manager)
	 */
	public void sendFrames(String username, String touser) {
		System.out
		.println("[Modulo Client] - Enviando frame ao modulo de Gerenciamento");
		SendVideoHandler manager = new SendVideoHandler(username, touser);
		new Thread(manager, "frame_sender").start();
	}

	/**
	 * Metodo que efetua o download do frame dos os repositorios Solicita
	 * frame passado como parametro para o modulo de Gerenciamento (Manager)
	 */
	public void receiveFrames(String username, String touser) {
		System.out
		.println("[Modulo Client] - Solicitando frame ao modulo de Gerenciamento");
		ReceiveVideoHandler manager = new ReceiveVideoHandler(username, touser);
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
		new UCHandlerVideoManager().sendFrames("Deivide", "Kenner");
	}
}