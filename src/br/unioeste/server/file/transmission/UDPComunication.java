package br.unioeste.server.file.transmission;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static br.unioeste.global.SocketConstants.*;

/**
 * Classe que implementa comunicação UDP
 */
public class UDPComunication {

	/**
	 * Metodo que envia um objeto pelo canal de comunicacao
	 * 
	 * @param address
	 * @param port
	 * @param obj
	 * @throws IOException
	 */
	public void sendObject(String address, int port, Object obj)
			throws IOException {
		// Instancia variaveis da comunicacao
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress iPAddress = InetAddress.getByName(address);

		// Serializa o objeto
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut);
		objectOut.flush();
		objectOut.writeObject(obj);
		objectOut.close();

		// Obtém os bytes do objeto serializado
		byte[] message = byteArrayOut.toByteArray();

		// Envia o objeto
		DatagramPacket sendPacket = new DatagramPacket(message, message.length,
				iPAddress, port);
		clientSocket.send(sendPacket);

		// Fecha o canal de comunicacao
		clientSocket.close();
	}

	/**
	 * Metodo que recebe um objeto pelo canal de comunicacao
	 * 
	 * @param group
	 * @param port
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object readGroupObject(String group, int port) throws IOException,
			ClassNotFoundException {
		// Instancia variaveis de comunicacao
		InetAddress groupAddress = InetAddress.getByName(group);
		MulticastSocket multicastSocket = new MulticastSocket(port);
		multicastSocket.joinGroup(groupAddress);

		// Recebe o objeto
		byte[] receiveData = new byte[MAX_DATA_READ];
		DatagramPacket datagramPacket = new DatagramPacket(receiveData,
				receiveData.length);
		multicastSocket.receive(datagramPacket);
		receiveData = datagramPacket.getData();

		// Desserializa o objeto
		ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(receiveData);
		ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn);
		Object object = (Object) objectIn.readObject();

		// Fecha os canais de comunicação
		objectIn.close();
		byteArrayIn.close();
		multicastSocket.leaveGroup(groupAddress);

		return object;
	}

	/**
	 * Metodo que le um objeto UDP na porta dada como parametro
	 * 
	 * @param port
	 * @return Objeto lido da porta passado como parametro
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object readObject(int port) throws IOException,
			ClassNotFoundException {

		DatagramSocket serverSocket = new DatagramSocket(port);
		byte[] receiveData = new byte[BUFFER_SIZE];

		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);
		// serverSocket.setSoTimeout(TIMEOUT);
		serverSocket.receive(receivePacket);
		receiveData = receivePacket.getData();

		// Desserializa o objeto
		ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(receiveData);
		ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn);
		Object object = (Object) objectIn.readObject();

		return object;
	}

}