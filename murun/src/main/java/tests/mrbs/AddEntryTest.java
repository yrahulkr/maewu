package tests.mrbs;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import utils.DriverProvider;
import utils.Properties;

public class AddEntryTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddEntry() throws Exception {
		driver.findElement(By.cssSelector("#logon_box > form > div > input[type=\"submit\"]")).click();
		driver.findElement(By.name("NewUserName")).clear();
		driver.findElement(By.name("NewUserName")).sendKeys("administrator");
		driver.findElement(By.name("NewUserPassword")).clear();
		driver.findElement(By.name("NewUserPassword")).sendKeys("secret");
		driver.findElement(By.cssSelector("input.submit")).click();
		driver.findElement(By.xpath("(//a[contains(text(),'12')])[2]")).click();
		driver.findElement(By.xpath("//div[@id='dwm_areas']/ul/li[3]/a/span")).click();
//		driver.findElement(By.xpath("//table[@id='day_main']/tbody/tr/td[2]/div/a")).click();
		driver.findElement(By.xpath("(//table[@id='day_main']/tbody/tr/td[2]/div/a)[1]")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("My Event");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Description for MyEvent");
		new Select(driver.findElement(By.id("area"))).selectByVisibleText("MyBuilding");
		new Select(driver.findElement(By.id("rooms"))).selectByVisibleText("MyRoom");
		driver.findElement(By.name("save_button")).click();
		assertEquals("My Event",
				driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[2]/div/div[2]/a")).getText());
		driver.findElement(By.cssSelector("#logon_box > form > div > input[type=\"submit\"]")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
