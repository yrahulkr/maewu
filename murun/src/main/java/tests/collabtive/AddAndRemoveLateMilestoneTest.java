package tests.collabtive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class AddAndRemoveLateMilestoneTest {

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
	public void testCollabtiveAddAndRemoveLateMilestone() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[3]/a")).click();
		driver.findElement(By.linkText("Project001")).click();
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[2]/a")).click();
		driver.findElement(By.id("add_butn")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Milestone002");
		driver.findElement(By.id("end")).clear();
		driver.findElement(By.id("end")).sendKeys("20.03.2012");
		driver.findElement(By.linkText("Close")).click();
		Thread.sleep(Long.valueOf(Properties.wait));
		driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
		assertEquals("Milestone002",
				driver.findElement(By.xpath(
						"html/body/div[1]/div[2]/div[2]/div/div[1]/div[3]/div[2]/div[2]/table/tbody/tr[1]/td[2]/div/a"))
						.getText());
		assertEquals("20.03.2012",
				driver.findElement(By.xpath(
						"html/body/div[1]/div[2]/div[2]/div/div[1]/div[3]/div[2]/div[2]/table/tbody/tr[1]/td[3]"))
						.getText());
		driver.findElement(By.cssSelector("tr.marker-late > td.tools > a.tool_del")).click();
		Thread.sleep(200);
		assertTrue(driver.switchTo().alert().getText()
				.matches("^Really delete this item[\\s\\S]\nDeleting cannot be undone\\.$"));
		driver.switchTo().alert().accept();
		driver.navigate().refresh();
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Milestone002[\\s\\S]*$"));
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*20\\.03\\.2012[\\s\\S]*$"));
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[4]/a")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
