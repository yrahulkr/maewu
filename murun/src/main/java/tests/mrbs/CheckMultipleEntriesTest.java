package tests.mrbs;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class CheckMultipleEntriesTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testCheckMultipleEntries() throws Exception {
		driver.findElement(By.xpath(".//*[@id='logon_box']/form/div/input[3]")).click();
		driver.findElement(By.name("NewUserName")).clear();
		driver.findElement(By.name("NewUserName")).sendKeys("administrator");
		driver.findElement(By.name("NewUserPassword")).clear();
		driver.findElement(By.name("NewUserPassword")).sendKeys("secret");
		driver.findElement(By.cssSelector("input.submit")).click();
		driver.findElement(By.linkText("MyBuilding")).click();
		driver.findElement(By.xpath("(//a[contains(text(),'12')])[2]")).click();
//		assertEquals("Wednesday 12 December 2018", driver.findElement(By.xpath(".//*[@id='dwm']/h2")).getText());
		assertTrue(driver.findElement(By.xpath(".//*[@id='dwm']/h2")).getText().contains("12"));
		assertEquals("MyEvent1",
				driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[2]/div/div[2]/a")).getText());
		assertEquals("MyEvent2",
				driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[3]/div/div[2]/a")).getText());
		assertEquals("MyEvent3",
				driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[4]/div/div[2]/a")).getText());
		driver.findElement(By.xpath("(//a[contains(text(),'10')])[3]")).click();
//		assertEquals("Thursday 10 January 2019", driver.findElement(By.xpath(".//*[@id='dwm']/h2")).getText());
		assertTrue(driver.findElement(By.xpath(".//*[@id='dwm']/h2")).getText().contains("10"));
		assertEquals("MyEvent1",
				driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[2]/div/div[2]/a")).getText());
		driver.findElement(By.xpath("(//a[contains(text(),'11')])[2]")).click();
//		assertEquals("Friday 11 January 2019", driver.findElement(By.xpath(".//*[@id='dwm']/h2")).getText());
		assertTrue(driver.findElement(By.xpath(".//*[@id='dwm']/h2")).getText().contains("11"));
		assertEquals("MyEvent2",
				driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[3]/div/div[2]/a")).getText());
		driver.findElement(By.xpath("(//a[contains(text(),'12')])[2]")).click();
//		assertEquals("Saturday 12 January 2019", driver.findElement(By.xpath(".//*[@id='dwm']/h2")).getText());
		assertTrue(driver.findElement(By.xpath(".//*[@id='dwm']/h2")).getText().contains("12"));
		assertEquals("MyEvent3",
				driver.findElement(By.xpath(".//*[@id='day_main']/tbody/tr[1]/td[4]/div/div[2]/a")).getText());
		driver.findElement(By.cssSelector("#logon_box > form > div > input[type=\"submit\"]")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
