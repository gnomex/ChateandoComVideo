package br.unioeste.GUI;

import static br.unioeste.global.SocketConstants.MANAGER_ADDR;
import static br.unioeste.global.SocketConstants.VIDEO_STREAMING_RECEIVE_PORT;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import br.unioeste.client.video.SendVideoHandler;
import br.unioeste.server.file.transmission.TCPComunication;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class VideoGUI implements Runnable {

	// final int INTERVAL=1000;///you may use interval
	private IplImage image;
	private CanvasFrame canvas = new CanvasFrame("Web Cam");
	private int width = 640;
	private int height = 480;

	public VideoGUI() {
		canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	}

	private byte[] getByteArray(BufferedImage imgbuffer) {
		return ((DataBufferByte) (imgbuffer).getRaster().getDataBuffer())
				.getData();
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

	@Override
	public void run() {
		FrameGrabber grabber = new VideoInputFrameGrabber(0);
		int i = 0;
		try {
			grabber.start();
			IplImage img;

			while (true) {
				img = grabber.grab();
				if (img != null) {
					// cvFlip(img, img, 1);// l-r =
					// 90_degrees_steps_anti_clockwise
					// cvSaveImage((i++)+"-capture.jpg", img);
					BufferedImage imagebuffer = img.getBufferedImage();
					// ByteArrayOutputStream baos = new ByteArrayOutputStream();
					// ImageIO.write(imagebuffer, "jpg", baos);
					// byte[] buffer;
					// baos.flush();
					// buffer = baos.toByteArray();
					// baos.close();
					byte[] buffer = getByteArray(imagebuffer);
					
					// System.out.println(buffer.length);

					// InputStream in = new ByteArrayInputStream(buffer);
					// BufferedImage image = ImageIO.read(in);
					// IplImage ipl = IplImage.createFrom(image);

					 BufferedImage imagebuffer2 = getBufferedImage(buffer);
					 IplImage ipl = IplImage.createFrom(imagebuffer2);
					
					 // show image on window
					 canvas.showImage(ipl);
				}
				// Thread.sleep(INTERVAL);
			}
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		new VideoGUI().run();
	}

}
