package br.unioeste.client.video;

import static br.unioeste.global.SocketConstants.BUFFER_SIZE;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.UnknownHostException;

import br.unioeste.common.Util;
import br.unioeste.server.file.transmission.TCPComunication;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class SendVideoHandler implements Runnable {

	private TCPComunication com;

	private String username;

	private String touser;
	
	private boolean connected = false;

	public SendVideoHandler(TCPComunication socketCommunication, String username, String touser) {
		if ((this.com = socketCommunication) != null) {
			this.setConnected(true);
		}
		this.username = username;
		this.touser = touser;
	}

	private byte[] getByteArray(BufferedImage imgbuffer) {
		return ((DataBufferByte) (imgbuffer).getRaster().getDataBuffer())
				.getData();
	}

	private void sendFrame(byte[] framebuffer) throws UnknownHostException, IOException {
		// Instancia variaveis
		byte[] buffer = new byte[BUFFER_SIZE];
		int read;
		int pakageNumber = 1;
		PackageVideo pack;
		ByteArrayInputStream bufferin = new ByteArrayInputStream(framebuffer);

		System.out
				.println("[Modulo Client] - Enviando frame ao modulo de Gerenciamento");
		// Efetua toda a leitura do frame e envia em pacotes
		do {
			read = bufferin.read(buffer);

			pack = new PackageVideo();
			pack.setUsername(username);
			pack.setTouser(touser);
			pack.setSequenceNumber(pakageNumber++);
			pack.setNext(pakageNumber);
			pack.setPayLoad(buffer);

			if (read == BUFFER_SIZE) {
				pack.setNotLast(true);
			} else {
				pack.setNotLast(false);
				pack.setPayLoad(Util.copyBytes(buffer, read));
			}

			System.out.println("[Modulo Client] - Nº pacote: "
					+ pack.getSequenceNumber());
			com.sendObject(pack);

		} while (read == BUFFER_SIZE);
		
		System.out
				.println("[Modulo Client] - Frame enviado ao modulo de Gerenciamento");
		System.out.println();

		// Fecha os canais de comunicação
		bufferin.close();
	}

	@Override
	public void run() {
		FrameGrabber grabber = new VideoInputFrameGrabber(0);
		try {
			grabber.start();
			IplImage img;

			while (true) {
				img = grabber.grab();
				if (img != null) {
					BufferedImage imagebuffer = img.getBufferedImage();
					byte[] buffer = getByteArray(imagebuffer);
					sendFrame(buffer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
