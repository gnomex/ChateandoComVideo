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
	
	/*Roda o cliente*/
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
	/*Conecta com servidor*/
	public void connectToServer() throws IOException{

		cliente = new Socket("127.0.0.1", 12345);
		System.out.println("CLIENT>> Conectado com: " + cliente.getInetAddress().getAddress());
	}
	/*Obtem fluxos de dados*/
	public void getStreams() throws IOException{

		out = new ObjectOutputStream( cliente.getOutputStream());
		out.flush();

		in = new ObjectInputStream( cliente.getInputStream());

		System.out.println("Got I/O Streams");

	}
	/*Processa conexão com server*/
	public void processConnection(){

		Message inmessage = new Message();

		Boolean aindaenviado = true;
		
		do{

			try{
				inmessage = (Message) in.readObject();
				System.out.println("\n Message: " + inmessage.getMessage() + " FROM: "+ inmessage.getDestino());

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			try{
				Message outmessage = new Message();
				outmessage.setDestino("CLIENT To: SERVER");
				
				outmessage.setMessage(JOptionPane.showInputDialog("Message to server: "));
				
				if(outmessage.getMessage().equals("sair")){
					outmessage.setMessage("CLIENT>> sair");
					aindaenviado = false;
				}
				
				sentData(outmessage);
				
			}catch (Exception e) {
				// TODO: handle exception
			}
			

		}while(aindaenviado);
		
		System.out.println("End of conversation");
		
	}
	/*Fecha conexão com server*/
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
	/*Envia dados ao servidor*/
	public void sentData(Message message){

		try{
			/*out.writeObject("CLIENT>> " + message);
			out.flush();*/
			out.writeObject(message);
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
