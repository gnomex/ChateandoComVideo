package br.unioeste.server.file.transmission;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static br.unioeste.global.SocketConstants.*;

/**
 * Classe que implementa a comunicacao TCP
 */
public class TCPComunication {

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket socket;

	/**
	 * Construtor da classe
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public TCPComunication(Socket socket) throws IOException {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.out.flush();
		this.in = new ObjectInputStream(socket.getInputStream());
		this.socket = socket;
		// this.socket.setSoTimeout(TIMEOUT);
	}

	/**
	 * Metodo que envia um array de bytes pelo canal de comunicacao
	 * 
	 * @param buffer
	 * @throws IOException
	 */
	public void send(byte[] buffer) throws IOException {
		this.out.write(buffer);
		this.out.flush();
	}

	/**
	 * Metodo que le um array de bytes pelo canal de comunicacao
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte[] read() throws IOException {
		byte[] newByte = new byte[BUFFER_SIZE];
		this.in.read(newByte);
		return newByte;
	}

	/**
	 * Metodo que envia um objeto pelo canal de comunicacao
	 * 
	 * @param obj
	 * @throws IOException
	 */
	public void sendObject(Object obj) throws IOException {
		this.out.writeObject(obj);
		this.out.flush();
	}

	/**
	 * Metodo que le um objeto pelo canal de comunicacao
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object readObject() throws IOException, ClassNotFoundException {
		Object obj = this.in.readObject();
		return obj;
	}

	/**
	 * Metodo que fecha todos os canais de comunicacao
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.in.close();
		this.out.close();
		this.socket.close();
	}
}