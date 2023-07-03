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

public class CloseTasklistTest {

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
	public void testCollabtiveCloseTasklist() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[3]/a")).click();
		driver.findElement(By.linkText("Project001")).click();
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[3]/a")).click();
		driver.findElement(By.xpath(".//*[@id='content-left-in']/div/div[4]/div/a[1]")).click();
		assertEquals("Open",
				driver.findElement(
						By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div/table/tbody/tr[1]/td[1]/a"))
						.getAttribute("title"));
		assertEquals("Tasklist001",
				driver.findElement(
						By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div/table/tbody/tr[1]/td[2]/div/a"))
						.getText());

		/* preconditions for RemoveMilestoneTest. */
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[2]/a")).click();
		driver.findElement(By.id("donebutn")).click();
		driver.findElement(By.cssSelector("a.butn_checked")).click();
		assertEquals("Close",
				driver.findElement(By.xpath(
						"html/body/div[1]/div[2]/div[2]/div/div[1]/div[3]/div[2]/div[1]/table/tbody/tr[1]/td[1]/a"))
						.getAttribute("title"));
		assertEquals("Milestone001", driver.findElement(By.linkText("Milestone001")).getText());

		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[4]/a")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
