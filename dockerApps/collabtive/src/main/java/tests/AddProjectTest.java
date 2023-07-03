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

public class AddProjectTest {

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
	public void testCollabtiveAddProject() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[3]/a")).click();
		
		driver.findElement(By.xpath("//*[@id=\"contentwrapper\"]/div[1]/ul/li/a")).click();
		Thread.sleep(Long.valueOf(Properties.wait));
		
		driver.findElement(By.id("add_butn_myprojects")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Project001");
		driver.findElement(By.tagName("body")).click();
		driver.findElement(By.tagName("body")).sendKeys("Description for Project001");
		driver.findElement(By.id("budget")).clear();
		driver.findElement(By.id("budget")).sendKeys("10000");
		driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();		
		assertEquals("Project001", driver.findElement(By.linkText("Project001")).getText());
		driver.findElement(By.linkText("Project001")).click();
		assertEquals("Budget: 10000",
				driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div/div[1]/div[2]/ul/li[4]/a")).getText());
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[4]/a")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
