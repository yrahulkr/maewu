package tests.collabtive;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import utils.DriverProvider;
import utils.Properties;

public class AddTasklistTest {

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
	public void testCollabtiveAddTasklist() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[3]/a")).click();
		driver.findElement(By.linkText("Project001")).click();
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[3]/a")).click();
		driver.findElement(By.id("addtasklists")).click();
		Thread.sleep(Long.valueOf(Properties.wait));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Tasklist001");
		new Select(driver.findElement(By.id("milestone"))).selectByVisibleText("Milestone001");
		driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
		assertEquals("Tasklist001",
				driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[4]/h2/a")).getText());
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[2]/a")).click();
		driver.findElement(By.linkText("Milestone001")).click();
		assertEquals("Tasklist001",
				driver.findElement(By.xpath(
						"html/body/div[1]/div[2]/div[2]/div/div/div[5]/div[3]/ul/li/div/table/tbody/tr[2]/td/span/a"))
						.getText());
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[4]/a")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
