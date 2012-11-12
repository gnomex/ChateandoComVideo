package br.unioeste.common;

import java.io.Serializable;

/**
 * Classe que implementa o objeto Solicitation que representa uma solicitação
 * a ser enviado
 */
public class Solicitation implements Serializable {
	private static final long serialVersionUID = 1L;

	public static int LIST_FILE = 1;
	public static int DOWNLOAD = 2;

	public static int DISCOVER = 93;
	public static int DISCOVER_REPLY = 94;

	private String archiveName;
	private int code;
	private String address;
	private int port;

	/**
	 * Metodo que retorna o nome do arquivo a ser efetuado o download
	 * 
	 * @return the archive
	 */
	public String getArchiveName() {
		return archiveName;
	}

	/**
	 * Metodo que seta o nome do arquivo a ser efetuado o download
	 * 
	 * @param archive
	 *            the archive to set
	 */
	public void setArchiveName(String archive) {
		this.archiveName = archive;
	}

	/**
	 * Metodo que retorna o endereço da solicitação
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Metodo que seta o endereço da solicitação
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Metodo que retorna o codigo da solicitação
	 * 
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Metodo que seta o codigo da solicitação
	 * 
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Metodo que retorna a porta da solicitação
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Metodo que seta a porta da solicitação
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
}