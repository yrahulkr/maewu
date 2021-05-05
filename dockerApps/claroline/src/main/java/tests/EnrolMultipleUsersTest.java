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

public class EnrolMultipleUsersTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineEnrolMultipleUsers() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("testuser1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("testuser1");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("Enrol on a new course")).click();
		driver.findElement(By.id("coursesearchbox_keyword")).clear();
		driver.findElement(By.id("coursesearchbox_keyword")).sendKeys("Course001");
		driver.findElement(By.xpath("//*[@id='claroBody']/form/button")).click();
		driver.findElement(By.xpath("//*[@id='claroBody']/dl[1]/dt/a[1]/img")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("testuser2");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("testuser2");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("Enrol on a new course")).click();
		driver.findElement(By.id("coursesearchbox_keyword")).clear();
		driver.findElement(By.id("coursesearchbox_keyword")).sendKeys("Course001");
		driver.findElement(By.xpath("//*[@id='claroBody']/form/button")).click();
		driver.findElement(By.xpath("//*[@id='claroBody']/dl[1]/dt/a[1]/img")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("testuser3");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("testuser3");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("Enrol on a new course")).click();
		driver.findElement(By.id("coursesearchbox_keyword")).clear();
		driver.findElement(By.id("coursesearchbox_keyword")).sendKeys("Course001");
		driver.findElement(By.xpath("//*[@id='claroBody']/form/button")).click();
		driver.findElement(By.xpath("//*[@id='claroBody']/dl[1]/dt/a[1]/img")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("001 - Course001")).click();
		driver.findElement(By.id("CLUSR")).click();
		assertTrue(driver.findElement(By.linkText("Testuser1")).getText().contains("Testuser1"));
		assertTrue(driver.findElement(By.linkText("Testuser2")).getText().contains("Testuser2"));
		assertTrue(driver.findElement(By.linkText("Testuser3")).getText().contains("Testuser3"));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
