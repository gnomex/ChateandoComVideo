package br.unioeste.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import br.unioeste.client.video.PackageVideo;
import br.unioeste.common.Package;

/**
 * Classe que implementa um arquivo temporario
 */
public class TemporaryFrame {

	private String username;
	private String touser;
	private LinkedList<PackageVideo> packageList = new LinkedList<PackageVideo>();

	/**
	 * Metodo que adiciona um pacote no arquivo temporario
	 * 
	 * @param newPackage
	 */
	public void add(PackageVideo newPackage) {
		this.packageList.add(newPackage);
	}

	/**
	 * Metodo que retorna a lista de todos os pacotes que representa o arquivo
	 * temporario
	 * 
	 * @return
	 */
	public LinkedList<PackageVideo> getPackageList() {
		return this.packageList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sort() {
		LinkedList<PackageVideo> list = this.getPackageList();

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

}