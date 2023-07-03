package tests;

import utils.DriverProvider;
import utils.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RemoveMultipleUsersTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineRemoveMultipleUsers() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("Platform administration")).click();
		driver.findElement(By.id("search_user")).clear();
		driver.findElement(By.id("search_user")).sendKeys("testuser");
		driver.findElement(By.xpath("//*[@id='claroBody']/ul/li[1]/ul/li[1]/form/input[2]")).click();
		driver.findElement(By.xpath("//*[@id='claroBody']/table[2]/tbody/tr[1]/td[10]/a/img")).click();
		driver.switchTo().alert().accept();
		assertTrue(driver.findElement(By.xpath("//*[@id='claroBody']/div[2]/div[1]")).getText()
				.contains("Deletion of the user was done sucessfully"));
		driver.findElement(By.xpath("//*[@id='claroBody']/table[2]/tbody/tr[1]/td[10]/a/img")).click();
		driver.switchTo().alert().accept();
		assertTrue(driver.findElement(By.xpath("//*[@id='claroBody']/div[2]/div[1]")).getText()
				.contains("Deletion of the user was done sucessfully"));
		driver.findElement(By.xpath("//*[@id='claroBody']/table[2]/tbody/tr[1]/td[10]/a/img")).click();
		driver.switchTo().alert().accept();
		assertTrue(driver.findElement(By.xpath("//*[@id='claroBody']/div[2]/div[1]")).getText()
				.contains("Deletion of the user was done sucessfully"));
		assertFalse(driver.findElement(By.xpath("html/body/div[1]/div[2]/table[2]/tbody/tr/td/center")).getText()
				.contains("No user to diplay"));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
