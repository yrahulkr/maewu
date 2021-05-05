package tests.claroline;

import utils.DriverProvider;
import utils.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class AddCourseEventTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineAddCourseEvent() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("001 - Course001")).click();
		driver.findElement(By.id("CLCAL")).click();
		driver.findElement(By.linkText("Add an event")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("Exam 001");
		new Select(driver.findElement(By.id("fday"))).selectByVisibleText("31");
		new Select(driver.findElement(By.id("fmonth"))).selectByVisibleText("May");
		new Select(driver.findElement(By.id("fyear"))).selectByVisibleText("2021");
		driver.findElement(By.id("location")).clear();
		driver.findElement(By.id("location")).sendKeys("Genoa");
		driver.findElement(By.name("submitEvent")).click();
		assertTrue(driver.findElement(By.xpath("//*[@id='courseRightContent']/div[2]/div")).getText()
				.contains("Event added to the agenda."));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
