package tests.claroline;

import utils.DriverProvider;
import org.openqa.selenium.support.ui.Select;
import utils.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;


/**
 * There was a problem in the application related to this test. The application adds a set of tables in the database
 * every time a course is created. There is no feature in the application to remove a course (and the tables associated
 * with it) so my solution was to delete all tables from the database and restore those present at the initial state.
 * What happens is that when the same course is created again the backend remembers that the same course was previously
 * created and it appends a counter to the table names related to the course to make them unique. Moreover there is a
 * check in the server code that limits the number of times the same course can be created. This limit is 64. If the
 * same course is created for the 65th time then the code throws an error and the application does not behave as
 * expected. This checks is in claroline1.11.10/claroline/inc/lib/add_course.lib.inc.php here:
 *
 * // here  we can add a counter to exit if need too many try
 * $limitQtyTry = 128;
 *
 * if (($tryNewFSCId+$tryNewFSCDb+$tryNewFSCDir > $limitQtyTry)
 *     or ($tryNewFSCId > $limitQtyTry / 2 )
 *     or ($tryNewFSCDb > $limitQtyTry / 2 )
 *     or ($tryNewFSCDir > $limitQtyTry / 2 )
 * )
 * {
 *     trigger_error('too many try for ' .  $wantedCode ,E_USER_WARNING);
 *     return false;
 * }
 *
 * In the application I commented this code.
 * */
public class AddCourseTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testClarolineAddCourse() throws Exception {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='loginBox']/form/fieldset/button")).click();
		driver.findElement(By.linkText("Platform administration")).click();
		driver.findElement(By.linkText("Create course")).click();
		driver.findElement(By.id("course_title")).clear();
		driver.findElement(By.id("course_title")).sendKeys("Course001");
		driver.findElement(By.id("course_officialCode")).clear();
		driver.findElement(By.id("course_officialCode")).sendKeys("001");
		new Select(driver.findElement(By.id("mslist2"))).selectByVisibleText("Sciences");
		driver.findElement(By.xpath("//a[@class=\"msremove\"]")).click();
		driver.findElement(By.id("registration_true")).click();
		driver.findElement(By.id("access_public")).click();
		driver.findElement(By.name("changeProperties")).click();
		assertTrue(driver.findElement(By.xpath("//*[@id='claroBody']/div[2]/div")).getText().contains("You have just created the course website : 001"));
		driver.findElement(By.linkText("Continue")).click();
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
