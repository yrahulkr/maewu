package tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import utils.DriverProvider;
import utils.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class DoCourseExerciseQuestionsTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineDoCourseExerciseQuestions() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("user001");
		driver.findElement(By.id("password")).clear();
		// driver.findElement(By.id("password")).sendKeys("Question 1"); // create fake dependency with AddCourseExerciseQuestionsTest
		// driver.findElement(By.id("password")).clear(); // create fake dependency with AddCourseExerciseQuestionsTest
		driver.findElement(By.id("password")).sendKeys("password001");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("001 - Course001")).click();
		driver.findElement(By.id("CLQWZ")).click();
		driver.findElement(By.linkText("Exercise 001")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[1]/tr/td/table/tbody/tr[1]/td[1]/input"))
				.click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[2]/tr/td/table/tbody/tr[1]/td[1]/input"))
				.click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[3]/tr/td/table/tbody/tr[1]/td[1]/input"))
				.click();
		driver.findElement(By.name("cmdOk")).click();
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/div[1]/div/strong")).getText()
				.contains("Your total score is 9/9"));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
