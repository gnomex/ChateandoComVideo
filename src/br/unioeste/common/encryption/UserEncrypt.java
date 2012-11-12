package br.unioeste.common.encryption;

import java.io.Serializable;

/**
 * Classe que implementa o objeto User que representa uma pessoa
 */
public class UserEncrypt implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String user;
	private String password;


	/**
	 * Metodo construtor do objeto User
	 */
	public UserEncrypt() {
	}

	/**
	 * Metodo construtor do objeto User
	 * 
	 * @param name
	 * @param user
	 * @param password
	 */
	public UserEncrypt(String name, String user, String password) {
		this.name = name;
		this.user = user;
		this.password = password;
	}

	/**
	 * Metodo que retorna o Nome da User
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Metodo que seta o Nome da User
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Metodo que retorna o Usuario da User
	 * 
	 * @return
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Metodo que seta o Usuario da User
	 * 
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Metodo que retorna a Senha da User
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Metodo que seta a Senha da User
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}