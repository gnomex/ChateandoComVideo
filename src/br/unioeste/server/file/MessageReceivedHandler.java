package br.unioeste.server.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;

import br.unioeste.common.Package;
import br.unioeste.common.Solicitation;
import br.unioeste.common.Util;
import br.unioeste.server.file.transmission.TCPComunication;
import br.unioeste.util.Archive;
import br.unioeste.util.TemporaryFile;

import static br.unioeste.global.SocketConstants.*;


/**
 * Classe que gerencia os objetos recebidos no servidor do repositorio atraves
 * de uma thread.
 */
public class MessageReceivedHandler implements Runnable {

	private Object object;

	/**
	 * Construtor da classe
	 * 
	 * @param object
	 */
	public MessageReceivedHandler(Object object) {
		this.object = object;

	}

	/**
	 * Metodo que executa as funcoes da thread
	 */
	@Override
	public void run() {
		// Verifica se é uma solicitação ou um pacote
		String className = object.getClass().getSimpleName();

		// Caso seja um pacote
		if (className.equals("Package")) {
			try {
				Package newPackage = (Package) object;
				
				this.packageHandler(newPackage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Caso seja uma solicitação
			if (className.equals("Solicitation")) {
				Solicitation solicitation = (Solicitation) object;
				try {
					this.solicitation(solicitation);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Metodo que executa caso seja uma solicitação, verificando qual o tipo da
	 * solicitação
	 * 
	 * @param solicitation
	 * @throws IOException
	 */
	private void solicitation(Solicitation solicitation) throws IOException {

		int code = solicitation.getCode();

		// Caso seja uma solicitação de listar arquivos
		if (code == Solicitation.LIST_FILE) {
			
			this.sendListFile(solicitation);
		} else {
			// Caso seja uma solicitação de download
			if (code == Solicitation.DOWNLOAD) {
				
				this.sendFile(solicitation);
			}
		}
	}

	/**
	 * Metodo que envia o arquivo ao modulo de gerenciamento
	 * 
	 * @param s
	 */
	private void sendFile(Solicitation s) {
		try {
			// Cria canais de comunicação
			Socket sock = new Socket(s.getAddress(), s.getPort());
			TCPComunication com = null;
			try {
				com = new TCPComunication(sock);
			} catch (Exception e) {
				
			}

			// Instancia arquivo do repositorio
			File file = new File(REPOSITORY_PATH + s.getArchiveName());
			FileInputStream fileIn = new FileInputStream(file);
			Package newPackage;
			int read;
			int packageNumber = 1;
			byte[] buf = new byte[BUFFER_SIZE];

			
			do {
				read = fileIn.read(buf);

				newPackage = new Package();
				newPackage.setFileName(file.getName());
				newPackage.setSequenceNumber(packageNumber++);
				newPackage.setNext(packageNumber);
				newPackage.setPayLoad(buf);

				if (read == BUFFER_SIZE) {
					newPackage.setNotLast(true);
				} else {
					newPackage.setNotLast(false);
					newPackage.setPayLoad(Util.copyBytes(buf, read));
				}

			
				com.sendObject(newPackage);

			} while (read == BUFFER_SIZE);
		

			fileIn.close();
			// Fecha canais de comunicação
			com.close();
			sock.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			
		}

	}

	/**
	 * Metodo que envia lista de arquivos ao modulo de gerenciamento
	 * 
	 * @param solicitation
	 * @throws IOException
	 */
	private void sendListFile(Solicitation solicitation) throws IOException {
		try {
			// Cria canais de comunicação
			Socket sock = new Socket(solicitation.getAddress(),
					solicitation.getPort());
			TCPComunication com = new TCPComunication(sock);

			// Envia todos os arquivos que estão no repositorio
			Archive archive;
			LinkedList<File> fileList = this.getFileList();
			if (!fileList.isEmpty()) {
				
				Iterator<File> it = fileList.iterator();
				File f;
				while (it.hasNext()) {
					f = it.next();
					if (f.isFile()) {
						archive = new Archive();
						archive.setName(f.getName());
						archive.setSize(f.length());
						if (it.hasNext()) {
							archive.setNotLast(true);
						} else {
							archive.setNotLast(false);
						}
						com.sendObject(archive);
					}
				}
			
			} else {
				archive = new Archive();
				archive.setName(null);
				archive.setNotLast(false);
				com.sendObject(archive);
				
			}

			// Fecha canais de comunicação
			com.close();
			sock.close();
		} catch (IOException e) {
			
		}
	}

	/**
	 * Metodo que retorna uma lista de arquivos que estão no diretorio do
	 * repositorio
	 * 
	 * @return
	 */
	private LinkedList<File> getFileList() {
		LinkedList<File> list = new LinkedList<File>();
		File directory = new File(
				REPOSITORY_PATH);

		File[] temp = directory.listFiles();
		for (File f : temp)
			list.add(f);

		return list;
	}

	/**
	 * Metodo que gerencia o recebimento de pacotes
	 * 
	 * @param newPackage
	 * @throws IOException
	 */
	private void packageHandler(Package newPackage) throws IOException {

		// Caso não seja o ultimo pacote
		if (newPackage.isNotLast()) {
			ServerTransmission.tmpFileList.add(newPackage);
		} else {
			int pos = ServerTransmission.tmpFileList.hasTmpFile(newPackage);
		

			// Se arquivos temporarios nao contem o arquivo do pacote, entao
			// cria arquivo pequeno
			if (pos == -1) {
				this.saveFile(newPackage);
			} else {

				TemporaryFile tmp = ServerTransmission.tmpFileList.remove(pos);
				tmp.add(newPackage);
				tmp.sort(); // ordena pacotes conforme o numero de sequencia
				this.checkBuffer(tmp);
				this.saveFile(tmp);
			}
		}
	}

	/**
	 * Verifica pacotes armazenados no buffer, se o buffer possui uma quantidade
	 * alta de pacotes faltantes e as iteraçoes nao conseguiram recuperar entao
	 * retorna false
	 * 
	 * @param tmp
	 * @return true se pacotes com erro nao ultrapassam o limite estabelecido
	 */
	private void checkBuffer(TemporaryFile tmp) {

		this.checkDuplicate(tmp);
		this.checkFault(tmp);

	}

	/**
	 * @return
	 */
	private void checkFault(TemporaryFile tmp) {

		LinkedList<Package> list = tmp.getPackageList();
		int count = 1;

		for (Package pack : list) {

			if (pack.getSequenceNumber() != count) {
				// TODO solicitar para modulo manager pacote com o numero de
				// sequencia count
				// ao receber adiciona-lo na pociçao correta da lista, e entao
				// continuar
			}
			count++;
		}
	}

	/**
	 * Verifica existencia de pacotes duplicados no buffer, na ocorrencia de
	 * algum remove-o do buffer
	 * 
	 * @param tmp
	 * @return lista de pacotes sem duplicaçoes
	 */
	private void checkDuplicate(TemporaryFile tmp) {

		LinkedList<Package> list = tmp.getPackageList();
		int[] count = new int[list.size() + 1];

		for (Package pack : list) {

			if (count[pack.getSequenceNumber()] == 0) {
				count[pack.getSequenceNumber()]++;
			} else {
				list.remove(pack);
			}
		}

	}

	/**
	 * Metodo que salva arquivo no repositorio
	 * 
	 * @param temp
	 * @throws IOException
	 */
	private void saveFile(TemporaryFile temp) throws IOException {
		// Instancia novo arquivo
		File file = new File(REPOSITORY_PATH + temp.getFileName());
		FileOutputStream fileOut = new FileOutputStream(file);
		LinkedList<Package> list = temp.getPackageList();

		byte[] buf;
		// Escreve arquivo
		for (Package newPackage : list) {
			buf = newPackage.getPayLoad();
			fileOut.write(buf);
		}
		fileOut.close();
	}

	/**
	 * Metodo que salva arquivo no repositorio
	 * 
	 * @param newPackage
	 * @throws IOException
	 */
	private void saveFile(Package newPackage) throws IOException {
		File file = new File(REPOSITORY_PATH + newPackage.getFileName());
		FileOutputStream fileOut = new FileOutputStream(file);
		fileOut.write(newPackage.getPayLoad());
		fileOut.close();
	}
}