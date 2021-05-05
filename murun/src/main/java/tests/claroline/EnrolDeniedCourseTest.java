package tests.claroline;

import utils.DriverProvider;
import utils.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class EnrolDeniedCourseTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineEnrolDeniedCourse() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("user001");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("password001");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("Enrol on a new course")).click();
		driver.findElement(By.id("coursesearchbox_keyword")).clear();
		driver.findElement(By.id("coursesearchbox_keyword")).sendKeys("Course003");
		driver.findElement(By.xpath("//*[@id='claroBody']/form/button")).click();
//		driver.findElement(By.xpath("//*[@id='claroBody']/dl[2]/dt[2]/a[1]/img")).click();
		driver.findElement(By.xpath("//*[@id='claroBody']/dl[1]/dt[1]/a[1]/img")).click();
		assertTrue(driver.findElement(By.xpath("//*[@id='claroBody']/div[1]/div[1]")).getText()
				.contains("This course currently does not allow new enrolments (registration: close)"));
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
