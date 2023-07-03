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

public class RemoveCourseExerciseQuestionsTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineRemoveCourseExerciseQuestions() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("001 - Course001")).click();
		driver.findElement(By.id("CLQWZ")).click();
		driver.findElement(By.xpath("//*[@id='courseRightContent']/table/tbody/tr[2]/td[2]/a/img")).click();
		driver.findElement(By.xpath("//*[@id='courseRightContent']/table/tbody/tr[1]/td[6]/a/img")).click();
		driver.switchTo().alert().accept();
		driver.findElement(By.xpath("//*[@id='courseRightContent']/table/tbody/tr[1]/td[6]/a/img")).click();
		driver.switchTo().alert().accept();
		driver.findElement(By.xpath("//*[@id='courseRightContent']/table/tbody/tr[1]/td[6]/a/img")).click();
		driver.switchTo().alert().accept();
		assertTrue(driver.findElement(By.xpath("//*[@id='courseRightContent']/table/tbody/tr/td")).getText().contains("Empty"));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
