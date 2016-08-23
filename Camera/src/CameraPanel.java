import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;



public class CameraPanel extends JPanel implements Runnable, ActionListener {
	BufferedImage image;
	VideoCapture capture;
	JButton button;
	CascadeClassifier cascadeClassifier;
	MatOfRect matOfRect;
	CameraPanel()
	{
		cascadeClassifier = new CascadeClassifier(CameraPanel.class.getResource("haarcascade_frontalface_alt.xml").getPath());
		matOfRect = new MatOfRect();		
		button = new JButton("Screenshot");
		button.addActionListener(this);
		add(button);
		
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		File output = new File("Screenshot1.png");
		int i=0;
		while(output.exists()){
			i++;
			output = new File("Screenshot" + i + "png");
		
		}
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

	@Override
	public void run() 
	{
		System.loadLibrary("opencv_java249");
		capture = new VideoCapture(0);
		Mat webcam_image = new Mat();
		if(capture.isOpened())
		{
			while(true)
			{
				capture.read(webcam_image);
				if(!webcam_image.empty())
				{
					JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
					topFrame.setSize(webcam_image.width() + 40,webcam_image.height() +110);
					matToBufferedImage(webcam_image);
					cascadeClassifier.detectMultiScale(webcam_image, matOfRect);
					repaint();
				}
		 	}
		}
	}
		
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(image==null) return;
		g.drawImage(image, 10, 40, image.getHeight(),image.getHeight(),null);
		g.setColor(Color.GREEN);
		for(Rect rect : matOfRect.toArray())
		{
			
			g.drawRect(rect.x +10,rect.y +40,rect.width, rect.height);
		}
	}
	
	public void matToBufferedImage(Mat matBGR)
	{
		int width = matBGR.width(),height = matBGR.height(), channels = matBGR.channels();
		byte[] source = new byte[height *width*channels];
		matBGR.get(0, 0,source);
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR );
		final byte[] target = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(source,0,target,0, source.length);
		
		
	}
	public void switchCamera(int x)
	{
		capture = new VideoCapture(x);
	}
	
	
	

}
