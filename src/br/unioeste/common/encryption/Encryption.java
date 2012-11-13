package br.unioeste.common.encryption;


/**
 * Classe que contem os algoritmos de criptografia do projeto
 */
public class Encryption {

	/**
	 * Metodo que faz a criptografia de uma mensagem
	 * 
	 * @param user
	 *            - Objeto UserEncrypt a ser criptografado
	 * @return Objeto Encriptografado
	 */
	public UserEncrypt encrypt(UserEncrypt user) {
		UserEncrypt userReturn = new UserEncrypt();
		int valor;
		char auxiliar;
		String message, messageReturn;

		// Encriptografa Nome da Pessoa
		message = user.getName();
		if (message != null) {
			messageReturn = "";
			for (int i = 0; i < message.length(); i++) {
				valor = (int) message.charAt(i);
				valor++;
				auxiliar = (char) valor;
				messageReturn = messageReturn.concat(String.valueOf(auxiliar));
			}
			userReturn.setName(messageReturn);
		}

		// Encriptografa Usuario da Pessoa
		message = user.getName();
		if (message != null) {
			messageReturn = "";
			for (int i = 0; i < message.length(); i++) {
				valor = (int) message.charAt(i);
				valor++;
				auxiliar = (char) valor;
				messageReturn = messageReturn.concat(String.valueOf(auxiliar));
			}

			userReturn.setUser(messageReturn);
		}

		// Encriptografa Senha da Pessoa
		message = user.getPassword();
		if (message != null) {
			messageReturn = "";
			for (int i = 0; i < message.length(); i++) {
				valor = (int) message.charAt(i);
				valor++;
				auxiliar = (char) valor;
				messageReturn = messageReturn.concat(String.valueOf(auxiliar));
			}
			userReturn.setPassword(messageReturn);
		}

		return userReturn;
	}

	/**
	 * Metodo que faz a descriptografia de uma mensagem
	 * 
	 * @param user
	 *            - Objeto UserEncrypt a ser descriptografado
	 * @return Objeto descriptografado
	 */
	public UserEncrypt decrypt(UserEncrypt user) {
		UserEncrypt userReturn = new UserEncrypt();
		int valor;
		char auxiliar;
		String message, messageReturn;

		// Descriptografa Nome da Pessoa
		message = user.getUser();
		if (message != null) {
			messageReturn = "";
			for (int i = 0; i < message.length(); i++) {
				valor = (int) message.charAt(i);
				valor--;
				auxiliar = (char) valor;
				messageReturn = messageReturn.concat(String.valueOf(auxiliar));
			}
			userReturn.setName(messageReturn);
		}

		// Descriptografa Usuario da Pessoa
		message = user.getUser();
		if (message != null) {
			messageReturn = "";
			for (int i = 0; i < message.length(); i++) {
				valor = (int) message.charAt(i);
				valor--;
				auxiliar = (char) valor;
				messageReturn = messageReturn.concat(String.valueOf(auxiliar));
			}
			userReturn.setUser(messageReturn);
		}

		// Descriptografa Senha da Pessoa
		message = user.getPassword();
		if (message != null) {
			messageReturn = "";
			for (int i = 0; i < message.length(); i++) {
				valor = (int) message.charAt(i);
				valor--;
				auxiliar = (char) valor;
				messageReturn = messageReturn.concat(String.valueOf(auxiliar));
			}
			userReturn.setPassword(messageReturn);
		}

		return userReturn;
	}
}