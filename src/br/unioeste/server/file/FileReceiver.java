package br.unioeste.server.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import br.unioeste.common.Solicitation;
import br.unioeste.server.file.transmission.TCPComunication;
import br.unioeste.util.TemporaryFile;
import br.unioeste.util.TemporaryFileList;
import br.unioeste.common.Package;

import static br.unioeste.global.SocketConstants.*;

/**
 * Metodo que implementa uma Thread que gerencia o download de um arquivo dos
 * repositorios
 */
public class FileReceiver implements Runnable {

	String fileName;
	String path;
	Socket socket;
	TCPComunication comunicationTCP;
	TemporaryFileList tempFile = new TemporaryFileList();

	/**
	 * Construtor da Thread
	 * 
	 * @param fileName
	 *            - Nome do Arquivo a ser solicitado download
	 * @param path
	 *            - Caminho a ser salvo o arquivo efetuado o download
	 */
	public FileReceiver(String fileName, String path) {
		this.fileName = fileName;
		this.path = path;
	}

	/**
	 * Metodo com as funcionalidades de execução da Thread
	 */
	@Override
	public void run() {
		try {
			// Efetua conexao com o modulo de Gerenciamento
			this.connect();
			// Envia solicitação de downlaod
			this.sendSolicitation(this.fileName);
			// Recebe o arquivo de download do modulo de Gerenciamento
			this.receiveFile();
			// Salva o arquivo de download
			this.saveFile();
			System.out
					.println("[Modulo Client] - Completado download e arquivo salvo");
			System.out.println();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que efetua a conexao com o modulo de gerenciamento
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void connect() throws UnknownHostException, IOException {
		// Estabelece canais de comunicação
		this.socket = new Socket(MANAGER_ADDR,
				DOWNLOAD_PORT);
		this.comunicationTCP = new TCPComunication(socket);
	}

	/**
	 * Metodo que envia a solicitacao de download para o modulo de Gerenciamento
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	private void sendSolicitation(String fileName) throws IOException {
		// Instancia uma solicitação
		Solicitation solicitation = new Solicitation();
		solicitation.setCode(Solicitation.DOWNLOAD);
		solicitation.setArchiveName(fileName);
		// Encia a solicitação
		this.comunicationTCP.sendObject(solicitation);
	}

	/**
	 * Metodo que recebe o arquivo do modulo de gerenciamento
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void receiveFile() throws IOException, ClassNotFoundException {
		Package pack;

		// Recebe todos os pacotes e armazena no buffer
		System.out
				.println("[Modulo Client] - Recebendo arquivo do modulo de Gerenciamento");
		do {
			pack = (Package) this.comunicationTCP.readObject();
			System.out.println("[Modulo Client] - Nº pacote: "
					+ pack.getSequenceNumber());
			this.tempFile.add(pack);
		} while (pack.isNotLast());
	}

	/**
	 * Metodo que salva o arquivo de downlaod no caminho de destino
	 * 
	 * @throws IOException
	 */
	private void saveFile() throws IOException {
		// Instancia variaveis de buffer do arquivo
		TemporaryFile tempFile = this.tempFile.remove(this.fileName);
		LinkedList<Package> list = tempFile.getPackageList();

		// Cria o arquivo de download
		File file = new File(this.path + "/" + this.fileName);
		FileOutputStream fo = new FileOutputStream(file);
		for (Package pack : list) {
			fo.write(pack.getPayLoad());
		}
		fo.close();
	}
}