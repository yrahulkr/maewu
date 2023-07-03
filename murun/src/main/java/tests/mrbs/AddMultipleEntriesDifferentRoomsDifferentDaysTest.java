package tests.mrbs;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import utils.DriverProvider;
import utils.Properties;

public class AddMultipleEntriesDifferentRoomsDifferentDaysTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddMultipleEntriesDifferentRoomsDifferentDays() throws Exception {
		driver.findElement(By.xpath(".//*[@id='logon_box']/form/div/input[3]")).click();
		driver.findElement(By.name("NewUserName")).clear();
		driver.findElement(By.name("NewUserName")).sendKeys("administrator");
		driver.findElement(By.name("NewUserPassword")).clear();
		driver.findElement(By.name("NewUserPassword")).sendKeys("secret");
		driver.findElement(By.cssSelector("input.submit")).click();
		driver.findElement(By.linkText("MyBuilding")).click();
		driver.findElement(By.xpath("(//a[contains(text(),'12')])[2]")).click();
		driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[2]/div/a")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("MyEvent1");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Description for MyEvent1");
		new Select(driver.findElement(By.id("area"))).selectByVisibleText("MyBuilding");
		driver.findElement(By.name("save_button")).click();
		driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[3]/div/a")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("MyEvent2");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Description for MyEvent2");
		new Select(driver.findElement(By.id("area"))).selectByVisibleText("MyBuilding");
		driver.findElement(By.name("save_button")).click();
		driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[4]/div/a")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("MyEvent3");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Description for MyEvent3");
		new Select(driver.findElement(By.id("area"))).selectByVisibleText("MyBuilding");
		driver.findElement(By.name("save_button")).click();
		driver.findElement(By.xpath("(//a[contains(text(),'12')])[2]")).click();
		assertEquals("MyEvent1",
				driver.findElement(By.xpath("html/body/div[2]/table[1]/tbody/tr[1]/td[2]/div/div[2]/a")).getText());
		assertEquals("MyEvent2",
				driver.findElement(By.xpath("html/body/div[2]/table[1]/tbody/tr[1]/td[3]/div/div[2]/a")).getText());
		assertEquals("MyEvent3",
				driver.findElement(By.xpath("html/body/div[2]/table[1]/tbody/tr[1]/td[4]/div/div[2]/a")).getText());
		driver.findElement(By.cssSelector("#logon_box > form > div > input[type=\"submit\"]")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
