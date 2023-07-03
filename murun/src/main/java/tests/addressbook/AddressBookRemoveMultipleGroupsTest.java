package tests.addressbook;

import static org.junit.Assert.assertFalse;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class AddressBookRemoveMultipleGroupsTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookRemoveMultipleGroups() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		driver.findElement(By.linkText("groups")).click();
		driver.findElement(By.xpath(".//*[@id='content']/form/input[4]")).click();
		driver.findElement(By.xpath(".//*[@id='content']/form/input[5]")).click();
		driver.findElement(By.xpath(".//*[@id='content']/form/input[6]")).click();
		driver.findElement(By.xpath(".//*[@id='content']/form/input[8]")).click();
		driver.findElement(By.linkText("group page")).click();
		assertFalse(driver.findElement(By.xpath(".//*[@id='content']/form")).getText().contains("Group1"));
		assertFalse(driver.findElement(By.xpath(".//*[@id='content']/form")).getText().contains("Group2"));
		assertFalse(driver.findElement(By.xpath(".//*[@id='content']/form")).getText().contains("Group3"));
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
