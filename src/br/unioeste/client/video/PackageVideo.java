package br.unioeste.client.video;

import java.io.Serializable;

import br.unioeste.global.SocketConstants;

/**
 * Classe que implementa o objeto Package que representa um pacote a ser enviado
 */
public class PackageVideo implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte[] username = new byte[SocketConstants.NAME_MAX_SIZE];
	private byte[] touser = new byte[SocketConstants.NAME_MAX_SIZE];
	private int sequenceNumber;
	private byte[] payLoad = new byte[SocketConstants.BUFFER_SIZE];
	private int type;
	private int next;
	private boolean notLast;

	/**
	 * Metodo que retorna o conteudo do pacote
	 * 
	 * @return the payLoad
	 */
	public byte[] getPayLoad() {
		return payLoad;
	}

	/**
	 * Meotod que seta o conteudo do pacote
	 * 
	 * @param payLoad
	 *            the payLoad to set
	 */
	public void setPayLoad(byte[] payLoad) {
		this.payLoad = payLoad.clone();
	}

	/**
	 * Metodo que retorna o nome do usuário.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return new String(this.username);
	}

	/**
	 * Metodo que seta o nome do usuário
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username.getBytes();
	}

	/**
	 * Metodo que retorna o numero de sequencia do pacote
	 * 
	 * @return the sequencieNumber
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Metodo que seta o numero de sequencia do pacote
	 * 
	 * @param sequencieNumber
	 *            the sequencieNumber to set
	 */
	public void setSequenceNumber(int sequencieNumber) {
		this.sequenceNumber = sequencieNumber;
	}

	/**
	 * Metodo que retorna o tipo do pacote
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Metodo que seta o tipo do pacote
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Metodo que retorna o numero do proximo pacote
	 * 
	 * @return the next
	 */
	public int getNext() {
		return next;
	}

	/**
	 * Metodo que seta o numero do proximo pacote
	 * 
	 * @param next
	 *            the next to set
	 */
	public void setNext(int next) {
		this.next = next;
	}

	/**
	 * Metodo que retorna se o pacote Ã© o ultimo
	 * 
	 * @return the last
	 */
	public boolean isNotLast() {
		return notLast;
	}

	/**
	 * Metodo que seta se o pacote Ã© o ultimo
	 * 
	 * @param notLast
	 */
	public void setNotLast(boolean notLast) {
		this.notLast = notLast;
	}

	public String getTouser() {
		return new String(touser);
	}

	public void setTouser(String touser) {
		this.touser = touser.getBytes();
	}
}