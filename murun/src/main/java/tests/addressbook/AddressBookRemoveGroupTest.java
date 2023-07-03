package tests.addressbook;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class AddressBookRemoveGroupTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookRemoveGroup() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		driver.findElement(By.linkText("groups")).click();
		driver.findElement(By.name("selected[]")).click();
		driver.findElement(By.name("delete")).click();
		assertTrue(driver.findElement(By.xpath(".//*[@id='content']/div")).getText()
				.matches("^Group has been removed\\.[\\s\\S]*$"));
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
