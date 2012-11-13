package br.unioeste.client.video.teste;

import static br.unioeste.global.SocketConstants.MANAGER_ADDR;
import static br.unioeste.global.SocketConstants.VIDEO_STREAMING_RECEIVE_PORT;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import br.unioeste.client.video.SendVideoHandler;
import br.unioeste.server.file.transmission.TCPComunication;

public class VideoTesteCliente1 implements Runnable {

	@Override
	public void run() {
		Socket socket;
		TCPComunication com = null;
		try {
			socket = new Socket(MANAGER_ADDR, VIDEO_STREAMING_RECEIVE_PORT);
			com = new TCPComunication(socket);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SendVideoHandler sender = new SendVideoHandler(com, "Deivide", "Kenner");
		new Thread(sender, "teste_video_sender").start();
	}
	
	public static void main(String[] args) {
		new Thread(new VideoTesteCliente1()).start();
	}

}
