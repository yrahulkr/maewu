package tests;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import utils.DriverProvider;
import utils.Properties;

public class AddConflictualEntryNegativeTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddConflictualEntryNegative() throws Exception {
		driver.findElement(By.xpath(".//*[@id='logon_box']/form/div/input[3]")).click();
		driver.findElement(By.name("NewUserName")).clear();
		driver.findElement(By.name("NewUserName")).sendKeys("administrator");
		driver.findElement(By.name("NewUserPassword")).clear();
		driver.findElement(By.name("NewUserPassword")).sendKeys("secret");
		driver.findElement(By.cssSelector("input.submit")).click();
		driver.findElement(By.linkText("MyBuilding")).click();
//		(//*[@id='day_main']/tbody/tr[1]/td[2]/div/div/a)[1]
		driver.findElement(By.xpath("//*[@id='day_main']/tbody/tr[1]/td[2]/div/a")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("MyConflictualEvent");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Description for MyConflictualEvent");
		driver.findElement(By.id("start_datepicker")).clear();
		driver.findElement(By.id("start_datepicker")).sendKeys("12/12/2018" + Keys.ENTER);
		driver.findElement(By.id("end_datepicker")).clear();
		driver.findElement(By.id("end_datepicker")).sendKeys("12/12/2018" + Keys.ENTER);
		new Select(driver.findElement(By.xpath("html/body/div[2]/form/fieldset/div[3]/select[3]")))
				.selectByVisibleText("07:00");
		new Select(driver.findElement(By.id("area"))).selectByVisibleText("MyBuilding");
		new Select(driver.findElement(By.id("rooms"))).selectByVisibleText("MyRoom");
		driver.findElement(By.name("save_button")).click();
		assertEquals("Scheduling Conflict", driver.findElement(By.xpath(".//*[@id='contents']/h2")).getText());
		driver.findElement(By.cssSelector("#logon_box > form > div > input[type=\"submit\"]")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
