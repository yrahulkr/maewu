package tests;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class LoginNegativeTest {

	private WebDriver driver;

	@Before
	public void setUp() {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testMantisBTLoginNegative() throws Exception {
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("");
		driver.findElement(By.cssSelector("input.button")).click();
		assertEquals("Your account may be disabled or blocked or the username/password you entered is incorrect.",
				driver.findElement(By.xpath("html/body/div[2]/font")).getText());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
