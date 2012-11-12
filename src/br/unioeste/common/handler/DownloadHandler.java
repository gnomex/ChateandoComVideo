package br.unioeste.common.handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import br.unioeste.common.Solicitation;
import br.unioeste.server.file.transmission.TCPComunication;
import br.unioeste.server.file.transmission.UDPComunication;
import br.unioeste.util.TemporaryFile;
import br.unioeste.util.TemporaryFileList;
import br.unioeste.common.Package;

import static br.unioeste.global.SocketConstants.*;


/**
 * Classe que gerencia a thread de Download de um arquivo
 */
public class DownloadHandler implements Runnable {
	
	private TCPComunication comunicationClient;
	private TCPComunication comunicationRepository;
	private TemporaryFileList buffer = new TemporaryFileList();
	
	
	/**
	 * Construtor da Classe
	 * @param socketCommunication
	 */
	public DownloadHandler(TCPComunication socketCommunication) {
		this.comunicationClient = socketCommunication;
	}
	
	
	/**
	 * Metodo que executa as funcoes da thread
	 */
	@Override
	public void run() {
            try {
                //Recebe a solicitacao do modulo do cliente
                Solicitation sol = (Solicitation) this.comunicationClient.readObject();

                //Conecta com o repositorio e envia solicitacao de download
                this.connectToRepository(sol);

                //Recebe arquivo do repositorio
                this.receiveArchive();

                //Fecha canal de comunicacao
                this.comunicationRepository.close();

                //Envia arquivo para o cliente
                this.sendArchive();

                //Fecha canal de comunicacao
                this.comunicationClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
		
	}

	
	/**
	 * Metodo que efetua a conexao com o repositorio
	 * @param sol
	 * @throws IOException
	 */
	private void connectToRepository(Solicitation sol) throws IOException {
		//Canal de comunicacao Socket com o repositorio
		ServerSocket serverSocket = new ServerSocket(DOWNLOAD_RECEIVE_PORT);
		Socket clientSockt;
		
		//Atribui endereco e porta do modulo do gerenciador na solicitacao
		sol.setAddress(InetAddress.getLocalHost().getHostAddress());
		sol.setPort(DOWNLOAD_RECEIVE_PORT);
		
		//Envia solicitacao
		this.sendSolicitation(sol);
		
		//Aguarda conexao de um repositorio
		clientSockt = serverSocket.accept();
		this.comunicationRepository = new TCPComunication(clientSockt);
		
		//Fecha canal de comunicacao
		serverSocket.close();
	}


	/**
	 * Metodo que envia uma solicicacao UDP
	 * @param sol
	 * @throws IOException
	 */
	private void sendSolicitation(Solicitation sol) throws IOException {
		System.out.println("[Modulo Manager] - Enviando solicitação de download ao modulo de Repositorio");
		UDPComunication uDPComunication = new UDPComunication();
		uDPComunication.sendObject(GROUP, UDP_PORT, sol);
	}

	
	/**
	 * Metodo que envia o arquivo ao modulo do Cliente
	 * @throws IOException
	 */
	private void sendArchive() throws IOException {
		//Instacia arquivo temporario
		TemporaryFile tempFile = this.buffer.remove(0);
		LinkedList<Package> list = tempFile.getPackageList();
		
		//Envia pacotes para o modulo do cliente
		System.out.println("[Modulo Manager] - Enviando arquivo ao modulo do Cliente");
		for (Package newPackage : list) {
			System.out.println("[Modulo Manager] - Nº pacote: " + newPackage.getSequenceNumber());
			this.comunicationClient.sendObject(newPackage);
		}
		System.out.println("[Modulo Manager] - Arquivo enviado ao modulo do Cliente");
		System.out.println();
	}


	/**
	 * Metodo que recebe arquivo do modulo de repositorio
	 */
	private void receiveArchive() {
		Package newPackage;
		
		try {
			//Recebe todos os pacotes do repositorio
			System.out.println("[Modulo Manager] - Recebendo arquivo do modulo de Repositorio");
			do {
				newPackage = (Package) this.comunicationRepository.readObject();
				System.out.println("[Modulo Manager] - Nº pacote: " + newPackage.getSequenceNumber());
				this.buffer.add(newPackage);
			} while (newPackage.isNotLast());
			System.out.println("[Modulo Manager] - Arquivo recebido do modulo de Repositorio");
			System.out.println();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}