package br.unioeste.util;

import java.util.LinkedList;

import br.unioeste.client.video.PackageVideo;

/**
 * Classe que implementa uma lista de arquivos temporarios
 * 
 */
public class TemporaryFrameList {

	private LinkedList<TemporaryFrame> temporaryFrameList = new LinkedList<TemporaryFrame>();

	/**
	 * Verifica se existe algum arquivo temporario na lista de arquivos
	 * temporarios com o mesmo nome se existir retorna a posiçao em que o
	 * arquivo se encontra na lista se nao existir retorna -1
	 * 
	 * @param newPackage
	 * @return posiçao do arquivo temporario que tem o mesmo nome do pkg na
	 *         lista
	 */
	public int hasTmpFrame(PackageVideo newPackage) {
		int ret = -1;
		int index = 0;

		String username = "";
		String touser = "";
		TemporaryFrame tmpFrame = null;

		if (!this.temporaryFrameList.isEmpty()) {
			while ((ret == -1) && (index < this.temporaryFrameList.size())) {
				tmpFrame = this.temporaryFrameList.get(index);
				username = tmpFrame.getUsername();
				touser = tmpFrame.getTouser();

				if (tmpFrame.getUsername().equals(username) && tmpFrame.getTouser().equals(touser)) {
					ret = index;
				}
				index++;
			}
		}
		return ret;
	}

	public int hasTmpFrame(String username, String touser) {
		int ret = -1;
		int index = 0;

		TemporaryFrame tmpFrame = null;

		if (!this.temporaryFrameList.isEmpty()) {
			while ((ret == -1) && (index < this.temporaryFrameList.size())) {
				tmpFrame = this.temporaryFrameList.get(index);

				if (tmpFrame.getUsername().equals(username) && tmpFrame.getTouser().equals(touser)) {
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
	private void insertTmpFrame(PackageVideo newPackage) {
		TemporaryFrame tmp = new TemporaryFrame();
		tmp.setUsername(newPackage.getUsername());
		tmp.setTouser(newPackage.getTouser());
		tmp.add(newPackage);
		this.temporaryFrameList.add(tmp);
	}

	/**
	 * Metodo que adiciona um novo arquivo a lista de arquivos temporarios
	 * 
	 * @param newPackave
	 * @return
	 */
	public synchronized int add(PackageVideo newPackage) {
		int index = this.hasTmpFrame(newPackage);

		if (index == -1) { // nao existe arquivo temporario com mesmo nome
			this.insertTmpFrame(newPackage);
			index = this.temporaryFrameList.size() - 1; // retorna posiçao de
														// onde foi inserido
		} else { // ja existe arquivo temporario incompleto na lista
			TemporaryFrame tmp = this.temporaryFrameList.get(index);
			tmp.add(newPackage);
		}

		return index;
	}

	/**
	 * Metodo que remove um arquivo da lista de arquivos temporarios
	 * 
	 * @param fileName
	 * @return
	 */
	public synchronized TemporaryFrame remove(String username, String touser) {
		int pos = this.hasTmpFrame(username, touser);

		if (pos >= 0) {
			return this.temporaryFrameList.remove(pos);
		}

		return null;
	}

	/**
	 * Metodo que remove um arquivo da lista de arquivos temporarios
	 * 
	 * @param pos
	 * @return
	 */
	public synchronized TemporaryFrame remove(int pos) {
		return this.temporaryFrameList.remove(pos);
	}
}