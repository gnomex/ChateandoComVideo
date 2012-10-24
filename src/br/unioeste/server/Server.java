package br.unioeste.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket server;

	private Socket connection;

	private int countclientsconnecteds = 0;

	private ObjectOutputStream output;

	private ObjectInputStream input;

	//Roda o "Servidor"
	public void runServer() throws IOException{

		try{
			//Instancia Servidor na porta 12345
			this.server = new ServerSocket(12345, 100);
			System.out.println("Server Estartado na porta 12345, Aguardando conexões");

			while(true){
				try{
					waitForConnection();
					getstrams();
					processConnection();
				}catch (IOException e) {
					// TODO: handle exception
					System.out.println("Houston, we have a problem. See the log:\n");
					e.printStackTrace();

				}finally{
					closeConnection();
					++countclientsconnecteds;
				}
			}

		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			server.close();
		}

	}

	//Espera por nova conexão
	private void waitForConnection() throws IOException{ 
		
		connection = server.accept();
		System.out.println("Connection: " + countclientsconnecteds + " from: " + connection.getInetAddress().getHostName());

	}
	//Obtem fluxos de enviar e receber dados
	private void getstrams() throws IOException{

		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();

		input = new ObjectInputStream(connection.getInputStream());

		System.out.println("Input OK");
	}
	//Processa conexão com cliente
	private void processConnection() throws IOException{

		String message = "Connection Successful on Server";
		sendData(message);
		
		String inmessage = "";
		
		do{
			
			try{
				inmessage = (String) input.readObject();
				System.out.println("Message received: " + inmessage);
				
				sendData("Message received, Thank's for all");
				
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}while(!inmessage.equals("CLIENT>> sair"));

		
		
	}
	//Envia dados ao cliente
	private void sendData(String message){

		try{
			output.writeObject("SERVER>> " + message);
			output.flush();

		}catch (IOException e) {
			// TODO: handle exception
		}

	}

	private void closeConnection(){

		System.out.println("##\n#### Closing connection\n");

		try{
			output.close();
			input.close();
			connection.close();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static void main(String[] args){
		Server srv = new Server();
		try {
			srv.runServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error: Server Don't Works ");
		}
	}
	 

}
