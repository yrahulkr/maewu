package tests;

import utils.DriverProvider;
import utils.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class AddMultipleUsersTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineAddMultipleUsers() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("Platform administration")).click();
		driver.findElement(By.linkText("Create user")).click();
		driver.findElement(By.id("lastname")).clear();
		driver.findElement(By.id("lastname")).sendKeys("testuser1");
		driver.findElement(By.id("firstname")).clear();
		driver.findElement(By.id("firstname")).sendKeys("testuser1");
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("testuser1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("testuser1");
		driver.findElement(By.id("password_conf")).clear();
		driver.findElement(By.id("password_conf")).sendKeys("testuser1");
		driver.findElement(By.id("student")).click();
		driver.findElement(By.id("applyChange")).click();
		driver.findElement(By.linkText("Create another new user")).click();
		driver.findElement(By.id("lastname")).clear();
		driver.findElement(By.id("lastname")).sendKeys("testuser2");
		driver.findElement(By.id("firstname")).clear();
		driver.findElement(By.id("firstname")).sendKeys("testuser2");
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("testuser2");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("testuser2");
		driver.findElement(By.id("password_conf")).clear();
		driver.findElement(By.id("password_conf")).sendKeys("testuser2");
		driver.findElement(By.id("courseManager")).click();
		driver.findElement(By.id("applyChange")).click();
		driver.findElement(By.linkText("Create another new user")).click();
		driver.findElement(By.id("lastname")).clear();
		driver.findElement(By.id("lastname")).sendKeys("testuser3");
		driver.findElement(By.id("firstname")).clear();
		driver.findElement(By.id("firstname")).sendKeys("testuser3");
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("testuser3");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("testuser3");
		driver.findElement(By.id("password_conf")).clear();
		driver.findElement(By.id("password_conf")).sendKeys("testuser3");
		driver.findElement(By.id("platformAdmin")).click();
		driver.findElement(By.id("applyChange")).click();
		driver.findElement(By.linkText("Back to admin page")).click();
		driver.findElement(By.linkText("User list")).click();
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div[2]/table[2]/tbody/tr[4]/td[2]")).getText().contains("testuser1"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div[2]/table[2]/tbody/tr[5]/td[2]")).getText().contains("testuser2"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div[2]/table[2]/tbody/tr[2]/td[2]")).getText().contains("testuser3"));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
