package tests.mrbs;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import utils.DriverProvider;
import utils.Properties;

public class AddRoomTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddRoom() throws Exception {
		driver.findElement(By.xpath(".//*[@id='logon_box']/form/div/input[3]")).click();
		driver.findElement(By.name("NewUserName")).clear();
		driver.findElement(By.name("NewUserName")).sendKeys("administrator");
		driver.findElement(By.name("NewUserPassword")).clear();
		driver.findElement(By.name("NewUserPassword")).sendKeys("secret");
		driver.findElement(By.cssSelector("input.submit")).click();
		driver.findElement(By.linkText("Rooms")).click();
		new Select(driver.findElement(By.xpath(".//*[@id='area_select']"))).selectByIndex(2);
		driver.findElement(By.xpath("(//input[@name='name'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@name='name'])[2]")).sendKeys("MyRoom");
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("Description of MyRoom");
		driver.findElement(By.name("capacity")).clear();
		driver.findElement(By.name("capacity")).sendKeys("10");
		driver.findElement(By.xpath("//input[@value='Add Room']")).click();
		assertTrue(driver
				.findElement(By.xpath(".//*[@id='rooms_table_wrapper']/div[6]/div[2]/div[2]/table/tbody/tr[1]/td/div"))
				.getText().matches("^[\\s\\S]*MyRoom[\\s\\S]*$"));
		driver.findElement(By.cssSelector("#logon_box > form > div > input[type=\"submit\"]")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
