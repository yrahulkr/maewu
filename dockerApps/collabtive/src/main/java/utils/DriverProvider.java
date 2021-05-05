package utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverProvider {

	private static DriverProvider ourInstance = new DriverProvider();

	private DriverProvider() {
		Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
	}

	public WebDriver getDriver() {

		WebDriverManager.firefoxdriver().setup();

		FirefoxProfile profile = new FirefoxProfile();

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setCapability("marionette", true);
		firefoxOptions.setProfile(profile);

		/* for headless Firefox. */
		firefoxOptions.setHeadless(Boolean.valueOf(Properties.getInstance().getProperty("headless_browser")));

		return new FirefoxDriver(firefoxOptions);
	}

	public static DriverProvider getInstance() {
		return ourInstance;
	}
}
