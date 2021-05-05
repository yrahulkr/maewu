package tests.collabtive;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class RemoveTasklistTest {

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
	public void testCollabtiveRemoveTasklist() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[3]/a")).click();
		driver.findElement(By.linkText("Project001")).click();
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[3]/a")).click();
		driver.findElement(By.xpath(".//*[@id='content-left-in']/div/div[4]/div/a[3]")).click();
		Thread.sleep(200);
		assertTrue(driver.switchTo().alert().getText()
				.matches("^Really delete this item[\\s\\S]\nDeleting cannot be undone\\.$"));
		driver.switchTo().alert().accept();
		driver.navigate().refresh();
		assertTrue(driver.findElement(By.xpath("//*[@id=\"content-left-in\"]/div")).getText()
				.contains("No tasklist found"));
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[4]/a")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
