package com.saltlab.murun.utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DebugUtils {

	public static void showPicture(BufferedImage state1Annotated) throws InterruptedException{

		JLabel picLabel = new JLabel(new ImageIcon(state1Annotated));
		JFrame frame=new JFrame();  
        frame.add(picLabel);  
        frame.setSize(1200,890);  
        frame.setVisible(true);  
        
        
		Object lock = new Object();
	    Thread t = new Thread() {
	        public void run() {
	            synchronized(lock) {
	                while (frame.isVisible())
	                    try {
	                        lock.wait();
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                System.out.println("Closing!!");
	            }
	        }
	    };
	    t.start();

	    frame.addWindowListener(new WindowAdapter() {

	        @Override
	        public void windowClosing(WindowEvent arg0) {
	            synchronized (lock) {
	            	frame.setVisible(false);
	                lock.notify();
	            }
	        }

	    });

	    t.join();
	}

}
