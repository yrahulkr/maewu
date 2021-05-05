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

public class AddMultipleProjectsTest {

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
	public void testCollabtiveAddMultipleProjects() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[3]/a")).click();
		driver.findElement(By.id("add_butn_myprojects")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("myProject1");
		driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
		driver.findElement(By.id("add_butn_myprojects")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("myProject2");
		driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
		driver.findElement(By.id("add_butn_myprojects")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("myProject3");
		driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*myProject1[\\s\\S]*$"));
		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*myProject2[\\s\\S]*$"));
		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*myProject3[\\s\\S]*$"));
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[4]/a")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
