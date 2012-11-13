package br.unioeste.common.handler;

import java.io.IOException;
import java.util.LinkedList;

import br.unioeste.server.file.transmission.TCPComunication;
import br.unioeste.server.file.transmission.UDPComunication;
import br.unioeste.util.TemporaryFile;
import br.unioeste.util.TemporaryFileList;
import br.unioeste.common.Package;

import static br.unioeste.global.SocketConstants.*;
/**
 * Classe que gerencia a thread de Upload de um arquivo
 */
public class UploadHandler implements Runnable {
	
	private TCPComunication comunicationTCP;
	private TemporaryFileList buffer = new TemporaryFileList();

	
	/**
	 * Construtor da classe
	 * @param socketCommunication
	 */
	public UploadHandler(TCPComunication socketCommunication) {
		this.comunicationTCP = socketCommunication;
	}
	
	
	/**
	 * Metodo que executa as funcoes da thread
	 */
	@Override
	public void run() {
		//Instancia variavel de nome do arquivo a ser feito upload
		String fileName;
		
		try {
			//Recebe arquivo do cliente
			fileName = this.receiveArchive();
			
			//Envia arquivo para o repositorio
			this.sendFile(fileName);
			
			//Fecha canal de comunicação
			this.comunicationTCP.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * Metodo que recebe arquivo do modulo do cliente
	 * @return
	 */
	private String receiveArchive() {
		Package newPackage = new Package();
		
		try {
			//Recebe todos os pacotes do client

			do {
				newPackage = (Package) this.comunicationTCP.readObject();
				this.buffer.add(newPackage);
			} while (newPackage.isNotLast());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return new String(newPackage.getFileName());
	}
	
	
	/**
	 * Metodo que envia o arquivo para o modulo do Repositorio
	 * @param filePath
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void sendFile(String filePath) throws IOException, InterruptedException {
		//Instacia arquivo temporario
		TemporaryFile tempFile = this.buffer.remove(filePath);
		LinkedList<Package> list = tempFile.getPackageList();
		
		//Inicia canal de comunicacao
		UDPComunication com = new UDPComunication();
	
		//Envia pacotes para o modulo do repositorio
		for(Package newPackage : list) {
			com.sendObject(GROUP, UDP_PORT, newPackage);
			Thread.sleep(DELAY);
		}
	}
}