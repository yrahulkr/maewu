package tests.addressbook;

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

public class AddressBookAssignToMultipleGroupsTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookAssignToMultipleGroups() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[2]/td[1]/input")).click();
		driver.findElement(By.name("add")).click();
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div[4]/div")).getText().contains("Users added."));
		driver.findElement(By.linkText("home")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[3]/td[1]/input")).click();
		new Select(driver.findElement(By.name("to_group"))).selectByVisibleText("Group2");
		driver.findElement(By.name("add")).click();
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div[4]/div")).getText().contains("Users added."));
		driver.findElement(By.linkText("home")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[4]/td[1]/input")).click();
		new Select(driver.findElement(By.name("to_group"))).selectByVisibleText("Group3");
		driver.findElement(By.name("add")).click();
		assertTrue(driver.findElement(By.xpath(".//*[@id='content']/div")).getText().contains("Users added."));
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
