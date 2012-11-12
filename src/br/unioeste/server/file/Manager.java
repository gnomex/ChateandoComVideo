package br.unioeste.server.file;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Classe que gerencia o modulo de Gerenciamento
 */
public class Manager {

	/**
	 * Construtor da classe
	 * 
	 * @throws RemoteException
	 */
	public Manager() throws Exception {

		ExecutorService serverExecutor;
		serverExecutor = Executors.newCachedThreadPool();
		
		// Inicia Serviço de Upload
		serverExecutor.execute(new ServerUpload("upload_server"));
		// Inicia Serviço de Download
		serverExecutor.execute(new ServerDownload("download_server"));
		// Inicia Serviço de Descobrimento dinamico de modulo Manager
		serverExecutor.execute(new ServerDiscovery("discovery_server"));
	}
	
	
}