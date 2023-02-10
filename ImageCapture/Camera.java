package ImageCapture;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Camera extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1925689962229076583L;
	
	
	//camera Screen
	protected static JLabel cameraScreeenJLabel;
	private JButton cameraCaptureButton;
	private static VideoCapture videoCapture;
	private static Mat imageMat;
	
	private static boolean clicked = false , closed = false;
	
	/**
	 * Create the frame.
	 */
	public Camera() {
		getContentPane().setLayout(null);
		setSize(new Dimension(640,560));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		cameraScreeenJLabel = new JLabel();
		cameraScreeenJLabel.setBounds(0,0,626,470);
		getContentPane().add(cameraScreeenJLabel);
		
		cameraCaptureButton = new JButton("capture");
		cameraCaptureButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clicked = true;
			}
		});
		
		addWindowFocusListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				//videoCapture.release();
				imageMat.release();
				closed = true;
				System.exit(0);
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				super.windowDeactivated(e);
			}
		});
		
		cameraCaptureButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cameraCaptureButton.setForeground(new Color(153, 102, 0));
		cameraCaptureButton.setBackground(new Color(255, 153, 51));
		cameraCaptureButton.setBorderPainted(false);
		cameraCaptureButton.setFocusPainted(false);
		cameraCaptureButton.setFont(new Font("Ubuntu", Font.BOLD, 19));
		cameraCaptureButton.setBounds(232,472,147,40);
		getContentPane().add(cameraCaptureButton);
		
		
	}
	
	// starting the camera
	public static void startCamera() throws HeadlessException
	{
		videoCapture = new VideoCapture(0);
		imageMat = new Mat();
		byte[] imagebyte;
		
		ImageIcon imageIcon;
		
		while(true) {
			// read image to the matrix
			videoCapture.read(imageMat);
			
			//convert image to byte
			final MatOfByte bufByte = new MatOfByte();
			Imgcodecs.imencode(".jpg", imageMat, bufByte);
			
			imagebyte = bufByte.toArray();
			
			//add a label
			imageIcon = new ImageIcon(imagebyte);
			
			cameraScreeenJLabel.setIcon(imageIcon);
			
			//capture and save to file
			if (clicked) {
				String nameString  = JOptionPane.showInputDialog(cameraScreeenJLabel,"Enter the name of the image");
				if(nameString == null) {
					nameString = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
				}
				
				//write to file
				Imgcodecs.imwrite("images/"+ nameString + ".jpg", imageMat);
				
				clicked = false;
			}
			
			//closing the function
			if(closed) {
				break;
			}
			
		}
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Camera frame = new Camera();
					frame.setVisible(true);
					
					//start the camera in Thead
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub\
							Camera.startCamera();
							
						}
					}).start();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
}
