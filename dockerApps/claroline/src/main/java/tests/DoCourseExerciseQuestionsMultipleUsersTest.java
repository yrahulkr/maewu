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

public class DoCourseExerciseQuestionsMultipleUsersTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineDoCourseExerciseQuestionsMultipleUsers() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("testuser1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("testuser1");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("001 - Course001")).click();
		driver.findElement(By.id("CLQWZ")).click();
		driver.findElement(By.linkText("Exercise 001")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[1]/tr/td/table/tbody/tr[2]/td[1]/input"))
				.click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[2]/tr/td/table/tbody/tr[2]/td[1]/input"))
				.click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[3]/tr/td/table/tbody/tr[3]/td[1]/input"))
				.click();
		driver.findElement(By.name("cmdOk")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("testuser2");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("testuser2");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("001 - Course001")).click();
		driver.findElement(By.id("CLQWZ")).click();
		driver.findElement(By.linkText("Exercise 001")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[1]/tr/td/table/tbody/tr[1]/td[1]/input"))
				.click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[2]/tr/td/table/tbody/tr[2]/td[1]/input"))
				.click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[3]/tr/td/table/tbody/tr[3]/td[1]/input"))
				.click();
		driver.findElement(By.name("cmdOk")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("testuser3");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("testuser3");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("001 - Course001")).click();
		driver.findElement(By.id("CLQWZ")).click();
		driver.findElement(By.linkText("Exercise 001")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[1]/tr/td/table/tbody/tr[1]/td[1]/input"))
				.click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[2]/tr/td/table/tbody/tr[1]/td[1]/input"))
				.click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div[3]/div[2]/form/table/tbody[3]/tr/td/table/tbody/tr[2]/td[1]/input"))
				.click();
		driver.findElement(By.name("cmdOk")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("001 - Course001")).click();
		driver.findElement(By.id("CLQWZ")).click();
		driver.findElement(By.xpath("//*[@id='courseRightContent']/table/tbody/tr[2]/td[6]/a/img")).click();
		assertTrue(driver.findElement(By.xpath("//*[@id='courseRightContent']/table[1]/tbody/tr[3]/td[1]")).getText().contains("testuser1 testuser1"));
		assertTrue(driver.findElement(By.xpath("//*[@id='courseRightContent']/table[1]/tbody/tr[3]/td[3]")).getText().contains("-3"));
		assertTrue(driver.findElement(By.xpath("//*[@id='courseRightContent']/table[1]/tbody/tr[4]/td[1]")).getText().contains("testuser2 testuser2"));
		assertTrue(driver.findElement(By.xpath("//*[@id='courseRightContent']/table[1]/tbody/tr[4]/td[3]")).getText().contains("0"));
		assertTrue(driver.findElement(By.xpath("//*[@id='courseRightContent']/table[1]/tbody/tr[5]/td[1]")).getText().contains("testuser3 testuser3"));
		assertTrue(driver.findElement(By.xpath("//*[@id='courseRightContent']/table[1]/tbody/tr[5]/td[3]")).getText().contains("6"));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();

	}

}
