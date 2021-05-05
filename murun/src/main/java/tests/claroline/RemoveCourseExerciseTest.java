package tests.claroline;

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

public class RemoveCourseExerciseTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineRemoveCourseExercise() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("001 - Course001")).click();
		driver.findElement(By.id("CLQWZ")).click();
		assertTrue(driver.findElement(By.xpath("//*[@id='courseRightContent']/table/tbody/tr[2]/td[1]/a")).getText()
				.contains("Exercise 001"));
		driver.findElement(By.xpath("//*[@id='courseRightContent']/table/tbody/tr[2]/td[3]/a/img")).click();
		driver.switchTo().alert().accept();
		assertFalse(driver.findElement(By.xpath("//*[@id='claroBody']/div[2]")).getText().contains("Exercise 001"));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
