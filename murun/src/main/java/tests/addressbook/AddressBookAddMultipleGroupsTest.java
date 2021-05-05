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

public class AddressBookAddMultipleGroupsTest {
	
	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookAddMultipleGroups() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		driver.findElement(By.linkText("groups")).click();
		driver.findElement(By.name("new")).click();
		driver.findElement(By.name("group_name")).clear();
		driver.findElement(By.name("group_name")).sendKeys("Group1");
		driver.findElement(By.name("group_header")).clear();
		driver.findElement(By.name("group_header")).sendKeys("Header1");
		driver.findElement(By.name("group_footer")).clear();
		driver.findElement(By.name("group_footer")).sendKeys("Footer1");
		driver.findElement(By.name("submit")).click();
		driver.findElement(By.linkText("group page")).click();
		driver.findElement(By.name("new")).click();
		driver.findElement(By.name("group_name")).clear();
		driver.findElement(By.name("group_name")).sendKeys("Group2");
		driver.findElement(By.name("group_header")).clear();
		driver.findElement(By.name("group_header")).sendKeys("Header2");
		driver.findElement(By.name("group_footer")).clear();
		driver.findElement(By.name("group_footer")).sendKeys("Footer2");
		driver.findElement(By.name("submit")).click();
		driver.findElement(By.linkText("group page")).click();
		driver.findElement(By.name("new")).click();
		driver.findElement(By.name("group_name")).clear();
		driver.findElement(By.name("group_name")).sendKeys("Group3");
		driver.findElement(By.name("group_header")).clear();
		driver.findElement(By.name("group_header")).sendKeys("Header3");
		driver.findElement(By.name("group_footer")).clear();
		driver.findElement(By.name("group_footer")).sendKeys("Footer3");
		driver.findElement(By.name("submit")).click();
		driver.findElement(By.linkText("group page")).click();
		assertTrue(driver.findElement(By.xpath(".//*[@id='content']/form")).getText()
				.contains("Group1"));
		assertTrue(driver.findElement(By.xpath(".//*[@id='content']/form")).getText()
				.contains("Group2"));
		assertTrue(driver.findElement(By.xpath(".//*[@id='content']/form")).getText()
				.contains("Group3"));
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
