package br.unioeste.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import br.unioeste.common.Package;

/**
 * Classe que implementa um arquivo temporario
 */
public class TemporaryFile {

	private String fileName;
	private LinkedList<Package> packageList = new LinkedList<Package>();

	/**
	 * Metodo que adiciona um pacote no arquivo temporario
	 * 
	 * @param newPackage
	 */
	public void add(Package newPackage) {
		this.packageList.add(newPackage);
	}

	/**
	 * Metodo que retorna o nome do arquivo temporario
	 * 
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Metodo que seta o nome do arquivo temporario
	 * 
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Metodo que retorna a lista de todos os pacotes que representa o arquivo
	 * temporario
	 * 
	 * @return
	 */
	public LinkedList<Package> getPackageList() {
		return this.packageList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sort() {
		LinkedList<Package> list = this.getPackageList();

		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				Package p1 = (Package) o1;
				Package p2 = (Package) o2;
				return p1.getSequenceNumber() < p2.getSequenceNumber() ? -1
						: p1.getSequenceNumber() < p2.getSequenceNumber() ? 1
								: 0;
			}
		});

	}

}