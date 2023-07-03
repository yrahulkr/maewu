package tests.mantisbt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class DeleteMultipleUsersTest {

	private WebDriver driver;

	@Before
	public void setUp() {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testMantisBTDeleteMultipleUsers() throws Exception {
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("administrator");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("root");
		driver.findElement(By.cssSelector("input.button")).click();
		driver.findElement(By.linkText("Manage")).click();
		driver.findElement(By.linkText("Manage Users")).click();
		driver.findElement(By.xpath("html/body/table[3]/tbody/tr[4]/td[1]/a")).click();
		driver.findElement(By.xpath("//input[@value='Delete User']")).click();
		driver.findElement(By.cssSelector("input.button")).click();
//		driver.findElement(By.linkText("Proceed")).click();
		driver.findElement(By.xpath("html/body/table[3]/tbody/tr[4]/td[1]/a")).click();
		driver.findElement(By.xpath("//input[@value='Delete User']")).click();
		driver.findElement(By.cssSelector("input.button")).click();
//		driver.findElement(By.linkText("Proceed")).click();
		driver.findElement(By.xpath("html/body/table[3]/tbody/tr[4]/td[1]/a")).click();
		driver.findElement(By.xpath("//input[@value='Delete User']")).click();
		driver.findElement(By.cssSelector("input.button")).click();
//		driver.findElement(By.linkText("Proceed")).click();
		assertEquals("Manage Accounts [1]",
				driver.findElement(By.xpath("html/body/table[3]/tbody/tr[1]/td[1]")).getText());
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*user001[\\s\\S]*$"));
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*user002[\\s\\S]*$"));
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*user003[\\s\\S]*$"));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();

	}

}
