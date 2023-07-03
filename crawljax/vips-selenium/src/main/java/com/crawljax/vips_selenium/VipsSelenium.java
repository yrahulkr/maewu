package com.crawljax.vips_selenium;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.WebDriverBrowserBuilder;
import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.BrowserOptions;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.util.DomUtils;

/**
 * Vision-based Page Segmentation algorithm
 * @author Tomas Popela
 *
 */
public class VipsSelenium {
	private String url = null;
	public WebDriver driver  = null;
	private EmbeddedBrowser browser = null;
	public Document dom = null;
	public BufferedImage viewport = null;

	private boolean graphicsOutput = true;
	private boolean outputToFolder = false;
	private boolean outputEscaping = true;
	private int pDoC = 11;
	private String filename = "";
	private	int sizeTresholdWidth = 700;
	private	int sizeTresholdHeight = 700;

	private PrintStream originalOut = null;
	long startTime = 0;
	long endTime = 0;

	/**
	 * Default constructor
	 */
	public VipsSelenium(String url)
	{
		this.url = url;
		CrawljaxConfigurationBuilder configBuilder = CrawljaxConfiguration.builderFor(url);
		BrowserConfiguration browserConfiguration = new BrowserConfiguration(BrowserType.CHROME, 1,
                new BrowserOptions());
		configBuilder.setBrowserConfig(browserConfiguration);
		WebDriverBrowserBuilder builder = new WebDriverBrowserBuilder(configBuilder.build(), null);
	
		browser = builder.get();
		driver = browser.getWebDriver();
		driver.navigate().to(url);
		if (graphicsOutput)
			exportPageToImage();

		getDomTree();
	}

	/**
	 * Enables or disables graphics output of VIPS algorithm.
	 * @param enable True for enable, otherwise false.
	 */
	public void enableGraphicsOutput(boolean enable)
	{
		graphicsOutput = enable;
	}

	/**
	 * Enables or disables creation of new directory for every algorithm run.
	 * @param enable True for enable, otherwise false.
	 */
	public void enableOutputToFolder(boolean enable)
	{
		outputToFolder = enable;
	}

	/**
	 * Enables or disables output XML character escaping.
	 * @param enable True for enable, otherwise false.
	 */
	public void enableOutputEscaping(boolean enable)
	{
		outputEscaping = enable;
	}

	/**
	 * Sets permitted degree of coherence (pDoC) value.
	 * @param value pDoC value.
	 */
	public void setPredefinedDoC(int value)
	{
		if (value <= 0 || value > 11)
		{
			System.err.println("pDoC value must be between 1 and 11! Not " + value + "!");
			return;
		}
		else
		{
			pDoC = value;
		}
	}

	/**
	 * Parses a builds DOM tree from page source.
	 * @param urlStream Input stream with page source.
	 */
	private void getDomTree()
	{
		try {
			dom = DomUtils.asDocument(driver.getPageSource());
			com.crawljax.vips_selenium.DomUtils.cleanDom(dom);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Exports rendered page to image.
	 */
	private void exportPageToImage()
	{
		try
		{
			viewport = browser.getScreenShotAsBufferedImage(1000);
			String filename = System.getProperty("user.dir") + "/page.png";
			ImageIO.write(viewport, "png", new File(filename));
		} catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Generates folder filename
	 * @return Folder filename
	 */
	private String generateFolderName()
	{
		String outputFolder = "";

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		outputFolder += sdf.format(cal.getTime());
		outputFolder += "_";
		try {
			outputFolder += (new URL(url)).getHost().replaceAll("\\.", "_").replaceAll("/", "_");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputFolder;
	}

	/**
	 * Performs page segmentation.
	 */
	private void performSegmentation()
	{

		startTime = System.nanoTime();
		int numberOfIterations = 20;
		int pageWidth = viewport.getWidth();
		int pageHeight = viewport.getHeight();

		
		VipsSeparatorGraphicsDetector detector;
		VipsSeleniumParser vipsParser = new VipsSeleniumParser(this);
		VisualStructureConstructor constructor = new VisualStructureConstructor(pDoC, viewport);
		constructor.setGraphicsOutput(graphicsOutput);

		for (int iterationNumber = 1; iterationNumber < numberOfIterations+1; iterationNumber++)
		{
			System.out.println("Iteration " + iterationNumber);
			detector = new VipsSeparatorGraphicsDetector(viewport);

			//visual blocks detection
			vipsParser.setSizeTresholdHeight(sizeTresholdHeight);
			vipsParser.setSizeTresholdWidth(sizeTresholdWidth);

			vipsParser.parse();

			Node vipsBlocks = vipsParser.getVipsBlocks();

			if (iterationNumber == 1)
			{
				if (graphicsOutput)
				{
					// in first round we'll export global separators
					detector.setVipsBlock(vipsBlocks);
					detector.fillPool();
					detector.saveToImage("blocks" + iterationNumber);
					detector.setCleanUpSeparators(0);
					detector.detectHorizontalSeparators();
					detector.detectVerticalSeparators();
					detector.exportHorizontalSeparatorsToImage();
					detector.exportVerticalSeparatorsToImage();
					detector.exportAllToImage();
				}

				// visual structure construction
				constructor.setVipsBlocks(vipsBlocks);
				constructor.setPageSize(pageWidth, pageHeight);
			}
			else
			{
				vipsBlocks = vipsParser.getVipsBlocks();
				constructor.updateVipsBlocks(vipsBlocks);

				if (graphicsOutput)
				{
					detector.setVisualBlocks(constructor.getVisualBlocks());
					detector.fillPool();
					detector.saveToImage("blocks" + iterationNumber);
				}
			}

			// visual structure construction
			constructor.constructVisualStructure();

			// prepare tresholds for next iteration
			if (iterationNumber <= 15)
			{
				sizeTresholdHeight -= 50;
				sizeTresholdWidth -= 50;

			}
			if (iterationNumber == 16)
			{
				sizeTresholdHeight = 100;
				sizeTresholdWidth = 100;
			}
			if (iterationNumber == 17)
			{
				sizeTresholdHeight = 80;
				sizeTresholdWidth = 80;
			}
			if (iterationNumber == 18)
			{
				sizeTresholdHeight = 40;
				sizeTresholdWidth = 10;
			}
			if (iterationNumber == 19)
			{
				sizeTresholdHeight = 1;
				sizeTresholdWidth = 1;
			}

		}

		constructor.normalizeSeparatorsSoftMax();
		constructor.normalizeSeparatorsMinMax();

		VipsOutput vipsOutput = new VipsOutput(pDoC);
		vipsOutput.setEscapeOutput(outputEscaping);
		vipsOutput.setOutputFileName(filename);
		vipsOutput.writeXML(constructor.getVisualStructure(), viewport, url, driver.getTitle());

		endTime = System.nanoTime();

		long diff = endTime - startTime;

		System.out.println("Execution time of VIPS: " + diff + " ns; " +
				(diff / 1000000.0) + " ms; " +
				(diff / 1000000000.0) + " sec");
	}

	/**
	 * Restores stdout
	 */
	private void restoreOut()
	{
		if (originalOut != null)
		{
			System.setOut(originalOut);
		}
	}

	/**
	 * Redirects stdout to nowhere
	 */
	private void redirectOut()
	{
		originalOut = System.out;
		System.setOut(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException
			{

			}
		}));
	}

	/**
	 * Starts visual segmentation of page
	 * @throws Exception
	 */
	public void startSegmentation()
	{
		try
		{

//			redirectOut();

			startTime = System.nanoTime();
//			getViewport();
//			restoreOut();

			String outputFolder = "";
			String oldWorkingDirectory = "";
			String newWorkingDirectory = "";

			if (outputToFolder)
			{
				outputFolder = generateFolderName();

				if (!new File(outputFolder).mkdir())
				{
					System.err.println("Something goes wrong during directory creation!");
				}
				else
				{
					oldWorkingDirectory = System.getProperty("user.dir");
					newWorkingDirectory += oldWorkingDirectory + "/" + outputFolder + "/";
					System.setProperty("user.dir", newWorkingDirectory);
				}
			}

			performSegmentation();

			if (outputToFolder)
				System.setProperty("user.dir", oldWorkingDirectory);
		}
		catch (Exception e)
		{
			System.err.println("Something's wrong!");
			e.printStackTrace();
		}
	}

	public void setOutputFileName(String filenameStr)
	{
		if (!filenameStr.equals(""))
		{
			filename = filenameStr;
		}
		else
		{
			System.out.println("Invalid filename!");
		}
	}

	public BufferedImage getViewport() {
		if(this.viewport != null) {
			return this.viewport;
		}
		exportPageToImage();
		return this.viewport;
	}

	public void cleanup() {
		this.driver.close();
	}
}
