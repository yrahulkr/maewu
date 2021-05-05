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

public class AddressBookPrintAddressBookTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookPrintAddressBook() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		driver.findElement(By.linkText("print all")).click();
		assertTrue(driver.findElement(By.xpath(".//*[@id='view']/tbody/tr/td[1]")).getText()
				.matches("^[\\s\\S]*firstname[\\s\\S]*$"));
		assertTrue(driver.findElement(By.xpath(".//*[@id='view']/tbody/tr/td[1]")).getText()
				.matches("^[\\s\\S]*lastname[\\s\\S]*$"));
		assertTrue(driver.findElement(By.xpath(".//*[@id='view']/tbody/tr/td[1]")).getText()
				.matches("^[\\s\\S]*address[\\s\\S]*$"));
		assertTrue(driver.findElement(By.xpath(".//*[@id='view']/tbody/tr/td[1]")).getText()
				.matches("^[\\s\\S]*01056321[\\s\\S]*$"));
		assertTrue(driver.findElement(By.xpath(".//*[@id='view']/tbody/tr/td[1]")).getText()
				.matches("^[\\s\\S]*mail@mail\\.it[\\s\\S]*$"));
		assertTrue(driver.findElement(By.xpath(".//*[@id='view']/tbody/tr/td[1]")).getText()
				.matches("^[\\s\\S]*19[\\s\\S]*$"));
		assertTrue(driver.findElement(By.xpath(".//*[@id='view']/tbody/tr/td[1]")).getText()
				.matches("^[\\s\\S]*June[\\s\\S]*$"));
		assertTrue(driver.findElement(By.xpath(".//*[@id='view']/tbody/tr/td[1]")).getText()
				.matches("^[\\s\\S]*1985[\\s\\S]*$"));
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
