package com.crawljax.plugins.coverage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.crawljax.core.CrawlSession;
import com.crawljax.core.ExitNotifier.ExitStatus;
import com.crawljax.core.plugin.PostCrawlingPlugin;
import com.crawljax.core.state.StateVertex;
import com.crawljax.stateabstractions.hybrid.HybridStateVertexImpl;
import com.crawljax.util.DomUtils;
import com.crawljax.vips_selenium.VipsUtils;
import com.crawljax.vips_selenium.VipsUtils.AccessType;
import com.crawljax.vips_selenium.VipsUtils.Coverage;

public class CoveragePlugin implements PostCrawlingPlugin {

	@Override
	public void postCrawling(CrawlSession session, ExitStatus exitReason) {
		File output = new File(session.getConfig().getOutputDir(), "screenshots");

		for(StateVertex state: session.getStateFlowGraph().getAllStates()) {
			BufferedImage annotated = annotateState(state);
			try {
				ImageIO.write(annotated, "PNG", new File(output, "cov" + state.getName() + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public BufferedImage annotateState(StateVertex state) {
		try {
			if(!(state instanceof HybridStateVertexImpl)) {
				System.err.println("Cannot annotate " + state.getClass());
				return null;
			}
			HybridStateVertexImpl sv = (HybridStateVertexImpl)state;
			Document doc = state.getDocument();
			Node body = doc.getElementsByTagName("body").item(0);
			NodeList allNodes = DomUtils.getAllSubtreeNodes(body);
			NodeList leafNodes= DomUtils.getAllLeafNodes(doc);
			BufferedImage screenshot = sv.getImage();
			BufferedImage graphImage = new BufferedImage(screenshot.getWidth(), screenshot.getHeight(), screenshot.getType());
			Graphics2D graphics = graphImage.createGraphics();
			graphics.drawImage(screenshot, 0, 0, null);
			
			for(int i =0; i< leafNodes.getLength(); i++) {
				Node leafNode = leafNodes.item(i);
				Rectangle rect = VipsUtils.getRectangle(leafNode, null);
				if(rect.isEmpty())
					continue;
				Coverage coverage = VipsUtils.getCoverage(leafNode, AccessType.any);
				Color color = null;
				switch(coverage) {
				case action:
					color = Color.GREEN;
					break;
				case find:
					color = Color.YELLOW;
					break;
				case assertion:
					color = Color.blue;
					break;
				case implicit:
				case childList:
				case attributes:
				case characterData:
				case subtree:
					color = Color.gray;
					break;
				case none:
					color = Color.red;
					break;
				}
				graphics.setStroke(new BasicStroke(3));
				graphics.setColor(color);
				graphics.draw(rect);
				
			}
//			try {
//				showPicture(graphImage);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			return graphImage;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void showPicture(BufferedImage state1Annotated) throws InterruptedException{

		JLabel picLabel = new JLabel(new ImageIcon(state1Annotated));
		JFrame frame=new JFrame();  
        frame.add(picLabel);  
        frame.setSize(state1Annotated.getWidth(), state1Annotated.getHeight());  
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
	                frame.dispose();
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
	
//	public static void main(String args[]) {
//		Rectangle rect = new Rectangle(-1,-1,1,0);
//		System.out.println(rect.isEmpty());
//	}
//	
}
