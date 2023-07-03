package tests;

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

public class UpdateIssueStatusAcknowledgedTest {

	private WebDriver driver;

	@Before
	public void setUp() {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testMantisBTUpdateIssueStatusAcknowledged() throws Exception {
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("administrator");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("root");
		driver.findElement(By.cssSelector("input.button")).click();
		driver.findElement(By.linkText("View Issues")).click();
		driver.findElement(By.cssSelector("img[alt=\"Edit\"]")).click();
		new Select(driver.findElement(By.name("status"))).selectByVisibleText("acknowledged");
		driver.findElement(By.cssSelector("input.button")).click();
		driver.findElement(By.linkText("View Issues")).click();
		assertEquals("acknowledged (administrator)",
				driver.findElement(By.xpath(".//*[@id='buglist']/tbody/tr[4]/td[8]")).getText());
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
