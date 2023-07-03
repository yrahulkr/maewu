package tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import utils.DriverProvider;
import utils.JavascriptActions;
import utils.JavascriptExecutor;
import utils.Properties;

public class AddAndRemoveSerialEntryTest {

	private WebDriver driver;
	private JavascriptExecutor javascriptExecutor;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
		javascriptExecutor = new JavascriptExecutor(driver);
	}

	@Test
	public void testAddAndRemoveSerialEntry() throws Exception {
		driver.findElement(By.xpath(".//*[@id='logon_box']/form/div/input[3]")).click();
		driver.findElement(By.name("NewUserName")).clear();
		driver.findElement(By.name("NewUserName")).sendKeys("administrator");
		driver.findElement(By.name("NewUserPassword")).clear();
		driver.findElement(By.name("NewUserPassword")).sendKeys("secret");
		driver.findElement(By.cssSelector("input.submit")).click();
		driver.findElement(By.linkText("Rooms")).click();
		new Select(driver.findElement(By.xpath(".//*[@id='area_select']"))).selectByIndex(2);
		driver.findElement(By.xpath("(//input[@name='name'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@name='name'])[2]")).sendKeys("LastRoom");
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("Description of LastRoom");
		driver.findElement(By.name("capacity")).clear();
		driver.findElement(By.name("capacity")).sendKeys("10");
		driver.findElement(By.xpath("//input[@value='Add Room']")).click();
		driver.findElement(By.linkText("Meeting Room Booking System")).click();
		driver.findElement(By.linkText("MyBuilding")).click();
//		driver.findElement(By.xpath("(//a[contains(text(),'1')])[29]")).click();
		driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[2]/div/a")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("SerialEvent");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Description for SerialEvent");
		driver.findElement(By.xpath("(//input[@name='rep_type'])[2]")).click();
		driver.findElement(By.id("start_datepicker")).clear();
		driver.findElement(By.id("start_datepicker")).sendKeys("29/12/2018");
		driver.findElement(By.id("rep_end_datepicker")).clear();
		driver.findElement(By.id("rep_end_datepicker")).sendKeys("31/12/2018");
		new Select(driver.findElement(By.id("area"))).selectByVisibleText("MyBuilding");
		driver.findElement(By.tagName("fieldset")).click(); // click outside to let the calendar disappear
		javascriptExecutor.click(driver.findElement(By.xpath("//div[@id=\"edit_entry_submit_save\"]/input")));
//		driver.findElement(By.name("save_button")).click();
		assertTrue(driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[2]/div/div[2]/a")).getText().matches("^[\\s\\S]*SerialEvent[\\s\\S]*$"));
		driver.findElement(By.linkText("SerialEvent")).click();
		driver.findElement(By.linkText("Delete Series")).click();
		driver.switchTo().alert().accept();
		driver.findElement(By.linkText("Rooms")).click();
		new Select(driver.findElement(By.xpath(".//*[@id='area_select']"))).selectByIndex(2);
		driver.findElement(
				By.xpath(".//*[@id='rooms_table_wrapper']/div[6]/div[1]/div[2]/table/tbody/tr[1]/td/div/a/img"))
				.click();
		driver.findElement(By.id("del_yes")).click();
		driver.findElement(By.cssSelector("#logon_box > form > div > input[type=\"submit\"]")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
