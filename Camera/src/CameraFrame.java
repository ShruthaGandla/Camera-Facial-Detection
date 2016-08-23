import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.opencv.core.Core;
import org.opencv.highgui.VideoCapture;

public class CameraFrame extends JFrame implements ActionListener {
	CameraPanel cp;
	public CameraFrame()
	{
		System.loadLibrary("opencv_java249");
		VideoCapture list = new VideoCapture(0);
		cp = new CameraPanel();
		Thread thread = new Thread(cp);		
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Camera");
		bar.add(menu);
		int i =1;
		//while(list.isOpened())
		//{
			JMenuItem item = new JMenuItem("Camera " +i);
			item.addActionListener(this);
			menu.add(item);
			list.release();
			list = new VideoCapture(i);
			i++;
			
		
		//}
		thread.start();
		add(cp);
		setJMenuBar(bar);
		setVisible(true);
		setSize(400,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args)
	{
		System.out.println(System.getProperty("java.library.path"));

		CameraFrame cf = new CameraFrame();
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem)e.getSource();
		int num = Integer.parseInt(source.getText().substring(7))-1;
		System.out.println(num);
		cp.switchCamera(num);
		
		
	}
	
	

}
