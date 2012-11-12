package br.unioeste.common;

/**
 * Classe que implementa algoritmos utilizados em todo o projeto
 */
public class Util {

	/**
	 * Método utilizado para copiar de buffer a quantidade de bytes util para
	 * criar um pacote com o payload com o tamanho correto
	 * 
	 * @param buffer
	 * @param qntde
	 * @return byte array com qntde de bytes do buffer
	 */
	public static byte[] copyBytes(byte[] buffer, int qntde) {
		byte[] ret = new byte[qntde];

		if (qntde > buffer.length) {
			return buffer;
		}

		for (int i = 0; i < qntde; i++) {
			ret[i] = buffer[i];
		}

		return ret;
	}
}