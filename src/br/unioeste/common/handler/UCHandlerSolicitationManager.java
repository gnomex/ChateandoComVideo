package br.unioeste.common.handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import br.unioeste.common.Solicitation;
import br.unioeste.server.file.transmission.TCPComunication;
import br.unioeste.server.file.transmission.UDPComunication;
import br.unioeste.util.Archive;

import static br.unioeste.global.SocketConstants.*;

/**
 * Metodo que gerencia as solicitações ao modulo de Gerenciamento
 */
public class UCHandlerSolicitationManager {

	/**
	 * Metodo que retorna uma lista com todos os arquivos do repositorio
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public List<Archive> listArchives() throws IOException,
			ClassNotFoundException {
		// Cria canais de comunicacao
		ServerSocket serverSocket;
		Socket socket;
		UDPComunication comunicationUDP = new UDPComunication();

		// Cria uma solicitacao
		Solicitation solicitation = new Solicitation();
		solicitation.setCode(Solicitation.LIST_FILE);
		solicitation.setAddress(InetAddress.getLocalHost().getHostAddress());
		solicitation.setPort(UPLOAD_PORT);

		// Envia solicitacao ao modulo de Repositorio
		comunicationUDP.sendObject(GROUP,
				UDP_PORT, solicitation);

		// Cria canal de comunicacao para esperar a resposta
		serverSocket = new ServerSocket(UPLOAD_PORT);
		socket = serverSocket.accept();

		// Recebe a lista do modulo do repositorio
		List<Archive> listArchives = this.receiveArchiveList(socket);

		// Fecha canal de comunicacao
		serverSocket.close();

		return listArchives;
	}

	/**
	 * Metodo que recebe os objetos do modulo de repositorio
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private List<Archive> receiveArchiveList(Socket socket) throws IOException,
			ClassNotFoundException {
		// Instancia variaveis
		Archive archive;
		LinkedList<Archive> list = new LinkedList<Archive>();
		TCPComunication com = new TCPComunication(socket);

		// Recebe arquivos do modulo de repositorio
		do {
			archive = (Archive) com.readObject();
			list.add(archive);
		} while (archive.isNotLast());

		// Fecha canal de comunicação
		com.close();
		
		return list;
	}
}