package br.unioeste.util;

import java.util.LinkedList;

import br.unioeste.common.Package;

/**
 * Classe que implementa uma lista de arquivos temporarios
 * 
 */
public class TemporaryFileList {

	private LinkedList<TemporaryFile> temporaryFileList = new LinkedList<TemporaryFile>();

	/**
	 * Verifica se existe algum arquivo temporario na lista de arquivos
	 * temporarios com o mesmo nome se existir retorna a posiçao em que o
	 * arquivo se encontra na lista se nao existir retorna -1
	 * 
	 * @param newPackage
	 * @return posiçao do arquivo temporario que tem o mesmo nome do pkg na
	 *         lista
	 */
	public int hasTmpFile(Package newPackage) {
		int ret = -1;
		int index = 0;

		String fileName = "";
		TemporaryFile tmpFile = null;

		if (!this.temporaryFileList.isEmpty()) {
			while ((ret == -1) && (index < this.temporaryFileList.size())) {
				tmpFile = this.temporaryFileList.get(index);
				fileName = tmpFile.getFileName();

				if (tmpFile.getFileName().equals(fileName)) {
					ret = index;
				}
				index++;
			}
		}
		return ret;
	}

	/**
	 * Verifica se existe algum arquivo temporario na lista de arquivos
	 * temporarios com o mesmo nome
	 * 
	 * @param fileName
	 * @return
	 */
	public int hasTmpFile(String fileName) {
		int ret = -1;
		int index = 0;

		TemporaryFile tmpFile = null;

		if (!this.temporaryFileList.isEmpty()) {
			while ((ret == -1) && (index < this.temporaryFileList.size())) {
				tmpFile = this.temporaryFileList.get(index);

				if (tmpFile.getFileName().equals(fileName)) {
					ret = index;
				}
				index++;
			}
		}
		return ret;
	}

	/**
	 * Metodo que insere um novo arquivo a lista de arquivos temporarios
	 * 
	 * @param pkg
	 */
	private void insertTmpFile(Package newPackage) {
		TemporaryFile tmp = new TemporaryFile();
		tmp.setFileName(newPackage.getFileName());
		tmp.add(newPackage);
		this.temporaryFileList.add(tmp);
	}

	/**
	 * Metodo que adiciona um novo arquivo a lista de arquivos temporarios
	 * 
	 * @param newPackave
	 * @return
	 */
	public synchronized int add(Package newPackave) {
		int index = this.hasTmpFile(newPackave);

		if (index == -1) { // nao existe arquivo temporario com mesmo nome
			this.insertTmpFile(newPackave);
			index = this.temporaryFileList.size() - 1; // retorna posiçao de
														// onde foi inserido
		} else { // ja existe arquivo temporario incompleto na lista
			TemporaryFile tmp = this.temporaryFileList.get(index);
			tmp.add(newPackave);
		}

		return index;
	}

	/**
	 * Metodo que remove um arquivo da lista de arquivos temporarios
	 * 
	 * @param fileName
	 * @return
	 */
	public synchronized TemporaryFile remove(String fileName) {
		int pos = this.hasTmpFile(fileName);

		if (pos >= 0) {
			return this.temporaryFileList.remove(pos);
		}

		return null;
	}

	/**
	 * Metodo que remove um arquivo da lista de arquivos temporarios
	 * 
	 * @param pos
	 * @return
	 */
	public synchronized TemporaryFile remove(int pos) {
		return this.temporaryFileList.remove(pos);
	}
}