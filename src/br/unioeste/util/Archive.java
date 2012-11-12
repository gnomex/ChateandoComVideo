package br.unioeste.util;

import java.io.Serializable;

/**
 * Classe que implementa o objeto File que representa um arquivo
 */
public class Archive implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private long size;
	private boolean notLast;

	/**
	 * Metodo que retorna o atributo notLast do arquivo
	 * 
	 * @return
	 */
	public boolean isNotLast() {
		return notLast;
	}

	/**
	 * Metodo que seta o atributo notLast do arquivo
	 * 
	 * @param last
	 */
	public void setNotLast(boolean notLast) {
		this.notLast = notLast;
	}

	/**
	 * Metodo que retorna o nome do arquivo
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Metodo que seta o nome do arquivo
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Metodo que retorna o tamanho do arquivo
	 * 
	 * @return
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Metodo que seta o tamanho do arquivo
	 * 
	 * @param size
	 */
	public void setSize(long size) {
		this.size = size;
	}
}