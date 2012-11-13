package br.unioeste.client.video;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import br.unioeste.common.Solicitation;
import br.unioeste.server.file.transmission.TCPComunication;
import br.unioeste.util.TemporaryFrameList;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ReceiveVideoHandler implements Runnable {

	private TCPComunication com;

	private String username;

	private String touser;

	private int width = 640;

	private int height = 480;

	private CanvasFrame canvas = new CanvasFrame("Web Cam");
	
	private boolean connected = false;

	public ReceiveVideoHandler(TCPComunication socketCommunication, String username, String touser) {
		if ((this.com = socketCommunication) != null) {
			this.setConnected(true);
		}
		this.username = username;
		this.touser = touser;
		canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	}

	private BufferedImage getBufferedImage(byte[] imgbuffer) {
		BufferedImage ret = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		DataBuffer buffer2 = new DataBufferByte(imgbuffer, imgbuffer.length);
		SampleModel samplemodel = new ComponentSampleModel(
				DataBuffer.TYPE_BYTE, width, height, 3, width * 3, new int[] {
						2, 1, 0 });
		Raster raster = Raster.createRaster(samplemodel, buffer2, null);
		ret.setData(raster);

		return ret;
	}

	private byte[] receiveFrame() throws UnknownHostException, IOException,
			ClassNotFoundException {
		// Instancia uma solicitação
		Solicitation solicitation = new Solicitation();
		solicitation.setCode(Solicitation.DOWNLOAD_FRAME);
		solicitation.setTouser(touser);
		solicitation.setUsername(username);
		// Encia a solicitação
		com.sendObject(solicitation);

		PackageVideo packreceived;
		TemporaryFrameList tempFrame = new TemporaryFrameList();

		System.out
				.println("[Modulo Client] - Recebendo frame do modulo de Gerenciamento");
		// Recebe todos os pacotes e armazena no buffer
		do {
			packreceived = (PackageVideo) com.readObject();
			System.out.println("[Modulo Client] - Nº pacote: "
					+ packreceived.getSequenceNumber());
			tempFrame.add(packreceived);
		} while (packreceived.isNotLast());
		
		System.out.println("[Modulo Client] - Completado download do frame");
		System.out.println();

		// Instancia variaveis de buffer do frame
		LinkedList<PackageVideo> list = tempFrame.remove(this.username,
				this.touser).getPackageList();

		ByteArrayOutputStream bufferout = new ByteArrayOutputStream();
		for (PackageVideo pack : list) {
			bufferout.write(pack.getPayLoad());
			bufferout.flush();
		}
		
		byte[] buffer = bufferout.toByteArray();
		bufferout.close();
		
		return buffer;
	}

	@Override
	public void run() {
		try {
			while (true) {
				byte[] buffer = receiveFrame();

				if (buffer.length > 0) {
					BufferedImage imagebuffer = getBufferedImage(buffer);
					// Create image from buffer:
					IplImage ipl = IplImage.createFrom(imagebuffer);
					// show image on window:
					canvas.showImage(ipl);
					System.out.println("123");
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
