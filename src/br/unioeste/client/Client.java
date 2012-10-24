package br.unioeste.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;


public class Client extends Thread{

	private Socket cliente;

	private ObjectOutputStream out;
	private ObjectInputStream in;

	@Override
	public void run(){

		try{
			connectToServer();
			getStreams();
			processConnection();
		}catch (EOFException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("sorry, client killed");
		}catch (IOException e) {
			// TODO: handle exception

			e.printStackTrace();
		}

		finally{
			closeConnection();
		}

	}

	public void connectToServer() throws IOException{

		cliente = new Socket("127.0.0.1", 12345);
		System.out.println("CLIENT>> Conectado com: " + cliente.getInetAddress().getAddress());
	}

	public void getStreams() throws IOException{

		out = new ObjectOutputStream( cliente.getOutputStream());
		out.flush();

		in = new ObjectInputStream( cliente.getInputStream());

		System.out.println("Got I/O Streams");

	}

	public void processConnection(){

		String inmessage = "Initialing conversation";
		String outmessage = "";

		do{

			try{
				inmessage = (String) in.readObject();
				System.out.println("\n Message: " + inmessage);

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			outmessage = JOptionPane.showInputDialog("Message to server: ");
			sentData(outmessage);

		}while(!outmessage.equals("sair"));
		
		try{
			
			sentData("sair");
			
		}catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void closeConnection(){

		try{
			out.close();
			in.close();
			cliente.close();

			System.out.println("Client Killed");
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("Error: client not killed");
		}
	}

	public void sentData(String message){

		try{
			out.writeObject("CLIENT>> " + message);
			out.flush();
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("Message not sent");
		}		
	}

	public static void main(String[] args){
		Client clt = new Client();
		clt.run();
	}

}
