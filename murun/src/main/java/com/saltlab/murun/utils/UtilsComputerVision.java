package com.saltlab.murun.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;

import ru.yandex.qatools.ashot.shooting.ShootingStrategy;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;
import ru.yandex.qatools.ashot.shooting.ViewportPastingDecorator;


public class UtilsComputerVision {

	
	public static BufferedImage getScreenShotAsBufferedImage(WebDriver driver, int pixelDensity, int scrollTime) throws Exception {

		if (pixelDensity != -1) {
			// BufferedImage img = Shutterbug.shootPage(getWebDriver(),
			// ScrollStrategy.WHOLE_PAGE_CHROME,true).getImage();
			BufferedImage img = Shutterbug
					.shootPage(driver, ScrollStrategy.BOTH_DIRECTIONS, scrollTime, true)
					.getImage();
			BufferedImage resizedImage = new BufferedImage(img.getWidth() / pixelDensity,
					img.getHeight() / pixelDensity, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(img, 0, 0, img.getWidth() / pixelDensity,
					img.getHeight() / pixelDensity,
					Color.WHITE, null);
			g.dispose();
			return resizedImage;
		}

		try {
			ShootingStrategy pasting =
					new ViewportPastingDecorator(new SimpleShootingStrategy())
							.withScrollTimeout(scrollTime);
			return pasting.getScreenshot(driver);

		} catch (IllegalStateException e) {
			Thread.currentThread().interrupt();
			throw new Exception(e);
		}
	}
	
	public static void saveScreenshot(WebDriver browser, String screenshotBefore) {
		try {
			BufferedImage image = getScreenShotAsBufferedImage(browser, Settings.PIXEL_DENSITY, 500);
			ImageIO.write(image, "PNG", new File(screenshotBefore));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
