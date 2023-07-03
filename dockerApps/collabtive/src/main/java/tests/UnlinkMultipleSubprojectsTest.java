package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class UnlinkMultipleSubprojectsTest {

	private WebDriver driver;

	@Before
	public void setUp() {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testMantisBTUnlinkMultipleSubprojects() throws Exception {
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("administrator");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("root");
		driver.findElement(By.cssSelector("input.button")).click();
		driver.findElement(By.linkText("Manage")).click();
		driver.findElement(By.linkText("Manage Projects")).click();
		driver.findElement(By.linkText("Project1")).click();
		driver.findElement(By.xpath("html/body/div[5]/table/tbody/tr[3]/td[7]/span[2]/a")).click();
		driver.findElement(By.linkText("Proceed")).click();
		driver.findElement(By.xpath("html/body/div[5]/table/tbody/tr[3]/td[7]/span[2]/a")).click();
		driver.findElement(By.linkText("Proceed")).click();
		driver.navigate().refresh();
		assertFalse(isElementPresent(By.xpath("html/body/div[6]/table/tbody/tr[3]/td[1]/a")));
		assertFalse(isElementPresent(By.xpath("html/body/div[6]/table/tbody/tr[4]/td[1]/a")));
		driver.findElement(By.linkText("Manage Projects")).click();
		assertEquals("sub1", driver.findElement(By.xpath("html/body/table[3]/tbody/tr[4]/td[1]/a")).getText());
		assertEquals("sub2", driver.findElement(By.xpath("html/body/table[3]/tbody/tr[5]/td[1]/a")).getText());
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
