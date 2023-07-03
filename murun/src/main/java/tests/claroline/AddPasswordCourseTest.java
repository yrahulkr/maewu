package tests.claroline;

import org.openqa.selenium.support.ui.Select;
import utils.DriverProvider;
import utils.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class AddPasswordCourseTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineAddPasswordCourse() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("Platform administration")).click();
		driver.findElement(By.linkText("Create course")).click();
		driver.findElement(By.id("course_title")).clear();
		driver.findElement(By.id("course_title")).sendKeys("Course002");
		driver.findElement(By.id("course_officialCode")).clear();
		driver.findElement(By.id("course_officialCode")).sendKeys("002");
		new Select(driver.findElement(By.id("mslist2"))).selectByVisibleText("Sciences");
		driver.findElement(By.xpath("//a[@class=\"msremove\"]")).click();
		driver.findElement(By.id("access_public")).click();
		driver.findElement(By.id("registration_key")).click();
		driver.findElement(By.id("course_registrationKey")).clear();
		driver.findElement(By.id("course_registrationKey")).sendKeys("password");
		driver.findElement(By.name("changeProperties")).click();
		assertTrue(driver.findElement(By.xpath("//*[@id='claroBody']/div[2]/div")).getText()
				.contains("You have just created the course website : 002"));
		driver.findElement(By.linkText("Continue")).click();
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
