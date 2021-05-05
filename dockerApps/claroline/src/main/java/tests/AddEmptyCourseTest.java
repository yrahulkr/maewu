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

public class AddEmptyCourseTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineAddEmptyCourse() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("Platform administration")).click();
		driver.findElement(By.linkText("Create course")).click();
		driver.findElement(By.name("changeProperties")).click();
		assertTrue(driver.findElement(By.xpath("//*[@id='claroBody']/div[2]/div/span[1]")).getText().contains("Course title needed"));
		assertTrue(driver.findElement(By.xpath("//*[@id='claroBody']/div[2]/div/span[2]")).getText().contains("Course code needed"));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
