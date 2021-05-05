package tests.addressbook;

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

// it was AddressBookUnassignFromMultipleGroupsTest
public class AddressBookRemoveFromMultipleGroupsTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookRemoveFromMultipleGroups() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		new Select(driver.findElement(By.name("group"))).selectByVisibleText("NewGroup1");
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[2]/td[1]/input")).click();
		driver.findElement(By.name("remove")).click();
		driver.findElement(By.linkText("home")).click();
		new Select(driver.findElement(By.name("group"))).selectByVisibleText("NewGroup2");
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[2]/td[1]/input")).click();
		driver.findElement(By.name("remove")).click();
		driver.findElement(By.linkText("home")).click();
		new Select(driver.findElement(By.name("group"))).selectByVisibleText("NewGroup3");
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[2]/td[1]/input")).click();
		driver.findElement(By.name("remove")).click();
		driver.findElement(By.linkText("home")).click();
		new Select(driver.findElement(By.name("group"))).selectByVisibleText("NewGroup1");
		assertEquals("Number of results: 0",
				driver.findElement(By.xpath(".//*[@id='content']/label/strong")).getText());
		new Select(driver.findElement(By.name("group"))).selectByVisibleText("NewGroup2");
		assertEquals("Number of results: 0",
				driver.findElement(By.xpath(".//*[@id='content']/label/strong")).getText());
		new Select(driver.findElement(By.name("group"))).selectByVisibleText("NewGroup3");
		assertEquals("Number of results: 0",
				driver.findElement(By.xpath(".//*[@id='content']/label/strong")).getText());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
