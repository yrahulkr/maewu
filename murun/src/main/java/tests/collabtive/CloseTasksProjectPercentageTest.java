package tests.collabtive;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class CloseTasksProjectPercentageTest {

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
	public void testCollabtiveCloseTasksProjectPercentage() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[3]/a")).click();
		driver.findElement(By.linkText("Project001")).click();
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[3]/a")).click();
		driver.findElement(By.cssSelector("a.butn_check")).click();
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[1]/a")).click();
		assertEquals("50%",
				driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div/div[1]/div[2]/div")).getText());
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[3]/a")).click();
		driver.findElement(By.cssSelector("a.butn_check")).click();
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[1]/a")).click();
		assertEquals("100%",
				driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div/div[1]/div[2]/div")).getText());
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[4]/a")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
