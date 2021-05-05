package tests;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class LoginUserTest {

	private WebDriver driver;

	@Before
	public void setUp() {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testCollabtiveLoginUser() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("username001");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("password001");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath("//*[@id=\"mainmenue\"]/li[2]/a")).click();
		assertTrue(driver.findElement(By.cssSelector("body")).getText().matches("^[\\s\\S]*username001[\\s\\S]*$"));
		driver.findElement(By.xpath("//*[@id='mainmenue']/li[3]/a")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
