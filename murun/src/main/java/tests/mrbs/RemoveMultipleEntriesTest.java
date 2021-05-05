package tests.mrbs;

import static org.junit.Assert.assertFalse;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class RemoveMultipleEntriesTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testRemoveMultipleEntries() throws Exception {
		driver.findElement(By.xpath(".//*[@id='logon_box']/form/div/input[3]")).click();
		driver.findElement(By.name("NewUserName")).clear();
		driver.findElement(By.name("NewUserName")).sendKeys("administrator");
		driver.findElement(By.name("NewUserPassword")).clear();
		driver.findElement(By.name("NewUserPassword")).sendKeys("secret");
		driver.findElement(By.cssSelector("input.submit")).click();
		driver.findElement(By.linkText("MyBuilding")).click();
		driver.findElement(By.xpath("(//a[contains(text(),'12')])[2]")).click();
		driver.findElement(By.linkText("MyEvent1")).click();
		driver.findElement(By.linkText("Delete Entry")).click();
		driver.switchTo().alert().accept();
		driver.findElement(By.linkText("MyEvent2")).click();
		driver.findElement(By.linkText("Delete Entry")).click();
		driver.switchTo().alert().accept();
		driver.findElement(By.linkText("MyEvent3")).click();
		driver.findElement(By.linkText("Delete Entry")).click();
		driver.switchTo().alert().accept();
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*MyEvent1[\\s\\S]*$"));
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*MyEvent2[\\s\\S]*$"));
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*MyEvent3[\\s\\S]*$"));
		driver.findElement(By.xpath("(//a[contains(text(),'10')])[3]")).click();
		driver.findElement(By.linkText("MyEvent1")).click();
		driver.findElement(By.linkText("Delete Entry")).click();
		driver.switchTo().alert().accept();
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*MyEvent1[\\s\\S]*$"));
		driver.findElement(By.xpath("(//a[contains(text(),'11')])[2]")).click();
		driver.findElement(By.linkText("MyEvent2")).click();
		driver.findElement(By.linkText("Delete Entry")).click();
		driver.switchTo().alert().accept();
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*MyEvent2[\\s\\S]*$"));
		driver.findElement(By.xpath("(//a[contains(text(),'12')])[2]")).click();
		driver.findElement(By.linkText("MyEvent3")).click();
		driver.findElement(By.linkText("Delete Entry")).click();
		driver.switchTo().alert().accept();
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*MyEvent3[\\s\\S]*$"));
		driver.findElement(By.cssSelector("#logon_box > form > div > input[type=\"submit\"]")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
