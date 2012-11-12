package br.unioeste.GUI;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;

import br.unioeste.messenger.ClientListener;
import br.unioeste.messenger.ManageClients;
import br.unioeste.messenger.ManageMessages;
import br.unioeste.messenger.MessagesListener;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class VideoGUI implements Runnable {
	
	//final int INTERVAL=1000;///you may use interval
    private IplImage image;
    private CanvasFrame canvas = new CanvasFrame("Web Cam");
    private ManageMessages messageManager; // communicates with message server
	private ManageClients clientsManager; // communication with clients server
	
	private ClientListener clientsListener; //
	private MessagesListener messageListener;
	
    public VideoGUI()
    {
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }
    
    @Override
    public void run()
    {
        FrameGrabber grabber = new VideoInputFrameGrabber(0); 
        int i=0;
        try {
            grabber.start();
            IplImage img;
            while (true) {
                img = grabber.grab();
                if (img != null) {
//                    cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
                    //cvSaveImage((i++)+"-capture.jpg", img);
                    BufferedImage imagebuffer = img.getBufferedImage();
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    ImageIO.write(imagebuffer, "jpg", baos);
//                    byte[] buffer;
//                    baos.flush();
//                    buffer = baos.toByteArray();
//                    baos.close();
                    byte[] buffer = ((DataBufferByte)(imagebuffer).getRaster().getDataBuffer()).getData();
                    
                    //System.out.println(buffer.length);
                    
//                    InputStream in = new ByteArrayInputStream(buffer);
//                    BufferedImage image = ImageIO.read(in);
//                    IplImage ipl = IplImage.createFrom(image);
                    
                    int width = img.width();
                    int height = img.height();
                    BufferedImage imagebuffer2 = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                    DataBuffer buffer2 = new DataBufferByte(buffer, buffer.length);
                    SampleModel samplemodel = new ComponentSampleModel(DataBuffer.TYPE_BYTE, width, height, 3, width*3, new int[]{2,1,0});
                    Raster raster = Raster.createRaster(samplemodel, buffer2, null);
                    imagebuffer2.setData(raster);
                    IplImage ipl = IplImage.createFrom(imagebuffer2);
                    
                    // show image on window
                    canvas.showImage(ipl);
                }
                 //Thread.sleep(INTERVAL);
            }
        } catch (Exception e) {
        }
    }
    
	public static void main(String[] args)
	{
		new VideoGUI().run();
	}

}
