package br.unioeste.common.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import br.unioeste.common.Solicitation;
import br.unioeste.common.Util;
import br.unioeste.server.file.FileReceiver;
import br.unioeste.server.file.transmission.TCPComunication;
import br.unioeste.server.file.transmission.UDPComunication;
import br.unioeste.common.Package;

import static br.unioeste.global.SocketConstants.*;

/**
 * Classe que gerencia o Arquivo no modulo do Cliente (Client)
 */
public class UCHandlerArchiveManager {

	/**
	 * Metodo que efetua o upload do arquivo para os repositorios Envia arquivo
	 * passado como parametro para o modulo de Gerenciamento (Manager)
	 * 
	 * @param filePath
	 *            - Arquivo a ser enviado para repositorios
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void sendFile(String filePath) throws UnknownHostException,
			IOException {
		// Cria Socket de comunicação com o modulo Manager
		Socket socket = new Socket(MANAGER_ADDR,
				TCP_PORT);

		// Carrega arquivo
		File file = new File(filePath);
		FileInputStream fileInputStream = new FileInputStream(file);

		// Estabelece o canal TCP
		TCPComunication comunicationTCP = new TCPComunication(socket);

		// Instancia variaveis
		byte[] buffer = new byte[BUFFER_SIZE];
		int read;
		int pakageNumber = 1;
		Package pack;

		// Efetua toda a leitura do arquivo e envia todos os pacotes
		System.out
				.println("[Modulo Client] - Enviando arquivo ao modulo de Gerenciamento");
		do {
			read = fileInputStream.read(buffer);

			pack = new Package();
			pack.setFileName(file.getName());
			pack.setSequenceNumber(pakageNumber++);
			pack.setNext(pakageNumber);
			pack.setPayLoad(buffer);

			if (read == BUFFER_SIZE) {
				pack.setNotLast(true);
			} else {
				pack.setNotLast(false);
				pack.setPayLoad(Util.copyBytes(buffer, read));
			}

			System.out.println("[Modulo Client] - Nº pacote: "
					+ pack.getSequenceNumber());
			comunicationTCP.sendObject(pack);

		} while (read == BUFFER_SIZE);
		System.out
				.println("[Modulo Client] - Arquivo enviado ao modulo de Gerenciamento");
		System.out.println();

		// Fecha os canais de comunicação
		comunicationTCP.close();
		socket.close();
	}

	/**
	 * Metodo que efetua o download do arquivo dos os repositorios Solicita
	 * arquivo passado como parametro para o modulo de Gerenciamento (Manager)
	 * 
	 * @param fileName
	 *            - Nome do arquivo a ser requisitado
	 * @param path
	 *            - Caminho a ser gravado o arquivo
	 */
	public void receiveFile(String fileName, String path) {
		System.out
				.println("[Modulo Client] - Solicitando arquivo ao modulo de Gerenciamento");
		new Thread(new FileReceiver(fileName, path), "file_receiver").start();
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
}