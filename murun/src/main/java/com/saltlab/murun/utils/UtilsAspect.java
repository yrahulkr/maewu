package com.saltlab.murun.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.aspectj.lang.JoinPoint;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawljax.core.state.StateVertex;
import com.crawljax.stateabstractions.hybrid.HybridStateVertexImpl;
import com.saltlab.murun.runner.TraceSession;
import com.saltlab.webmutator.utils.DomUtils;

import utils.DriverProvider;

public class UtilsAspect {

	/**
	 * return an identifier for the statement in the form <testname>-<line> from a
	 * joinPoint of type WebElement
	 * 
	 * @param joinPoint
	 * @return String
	 */
	public static String getStatementNameFromJoinPoint(JoinPoint joinPoint) {

		String name = "";

		name = joinPoint.getStaticPart().getSourceLocation().getFileName().replace(".java", "");
		name = name.concat("_");
		name = name.concat(Integer.toString(joinPoint.getStaticPart().getSourceLocation().getLine()));

		return name;
	}

	/**
	 * return the statement line from a joinPoint of type WebElement
	 * 
	 * @param joinPoint
	 * @return int
	 */
	public static int getStatementLineFromJoinPoint(JoinPoint joinPoint) {
		return joinPoint.getStaticPart().getSourceLocation().getLine();
	}

	/**
	 * creates a directory in the project workspace
	 * 
	 * @param joinPoint
	 * @return int
	 */
	public static void createTestFolder(String path) {

		File theDir = new File(path);
		if (!theDir.exists()) {

			if (Settings.VERBOSE)
				System.out.print("[LOG]\tcreating directory " + path + "...");

			boolean result = theDir.mkdirs();
			if (result) {
				if (Settings.VERBOSE)
					System.out.println("done");
			} else {
				if (Settings.VERBOSE)
					System.out.print("failed!");
				System.exit(1);
			}
		}

	}
	

	/**
	 * save an HTML file of the a WebDriver instance
	 * 
	 * @param d
	 * @param filePath
	 */
	public static void saveDOM(WebDriver d, String filePath) {

//		try {
			saveDom(d.getPageSource(), filePath);
//			FileUtils.writeStringToFile(new File(filePath), d.getPageSource());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
	

	private static void saveDom(String dom, String htmlPath) {

		try {
			FileUtils.writeStringToFile(new File(htmlPath), dom);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save rendered webpage path = where to save the html file
	 */
	public static File saveHTMLPage(String urlString, String path) throws IOException {

		File savedHTML = new File(path);

		/* necessary to avoid garbage. */
		if (savedHTML.exists()) {
			FileUtils.deleteDirectory(savedHTML);
		}

		/* wget to save html page. */
		Runtime runtime = Runtime.getRuntime();
		Process p = runtime.exec("/usr/local/bin/wget -p -k -E -nd -P " + path + " " + urlString);

		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return savedHTML;
	}

	public static void registerEventAfter(WebDriver driver, String stateName, String imagePath, String htmlPath) {
		StateVertex newState = TraceSession.getInstance().newState(stateName);
		saveDom(newState.getDom(), htmlPath);
		if(newState instanceof HybridStateVertexImpl) {
			saveImage(((HybridStateVertexImpl)newState).getImage(), imagePath);
		}
		else {
			UtilsComputerVision.saveScreenshot(driver, imagePath);
		}
		
	}


	private static void saveImage(BufferedImage image, String imagePath) {
		try {
			ImageIO.write(image, "PNG", new File(imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void registerEventBefore(String statementName, WebElement elem) {
		TraceSession.getInstance().recordEvent(statementName, elem);
	}
	
	public static void registerNav(String stateName) {
		TraceSession.getInstance().newNavState(stateName);
	}

}
