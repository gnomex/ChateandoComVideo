package br.unioeste.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import br.unioeste.client.Message;

public class Server extends Thread {

	private ServerSocket server;

	private Socket connection;

	private int countclientsconnecteds = 0;

	private ObjectOutputStream output;

	private ObjectInputStream input;

	//Roda o "Servidor"
	@Override
	public void run(){

		try{
			//Instancia Servidor na porta 12345
			this.server = new ServerSocket(12345, 100);
			System.out.println("Server works in port 12345. Waiting for connections");

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
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		
		Message outmessage = new Message();
		
		try{
			outmessage.setMessage("Connection Successful on Server");
			outmessage.setDestino("SERVER To: Client");
			sendData(outmessage);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Message sent Fail!");
		}
		

		System.out.println("Input OK");
	}
	//Processa conexão com cliente
	private void processConnection() throws IOException{


		
		Message inmessage = new Message();
		Message outmessage = new Message();
		outmessage.setDestino("SERVER To Client");
		do{
			
			try{
				inmessage = (Message) input.readObject();
				System.out.println("Message received: " + inmessage.getMessage() + " Stream: " + inmessage.getDestino());
				
				outmessage.setMessage("Message received. Thank's for All!");
				
				sendData(outmessage);
				
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}while(!inmessage.getMessage().equals("CLIENT>> sair"));

		System.out.println("SERVER: finalmente uma morga");
		
	}
	//Envia dados ao cliente
	private void sendData(Message message){

		try{
			/*output.writeObject("SERVER>> " + message);
			output.flush();*/
			output.writeObject(message);
			output.flush();

		}catch (IOException e) {
			// TODO: handle exception
		}

	}
	/*Fecha conexão com cliente*/
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
		srv.run();
	}
	 

}
