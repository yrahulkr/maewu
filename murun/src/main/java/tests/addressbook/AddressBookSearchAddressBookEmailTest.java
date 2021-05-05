package tests.addressbook;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class AddressBookSearchAddressBookEmailTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookSearchAddressBookEmail() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		driver.findElement(By.name("searchstring")).clear();
		driver.findElement(By.name("searchstring")).sendKeys("mail@mail.it");
		assertEquals("Number of results: 1",
				driver.findElement(By.xpath("html/body/div[1]/div[4]/label/strong")).getText());
		assertEquals("lastname", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[2]")).getText());
		assertEquals("firstname", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[3]")).getText());
		assertEquals("mail@mail.it", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[4]")).getText());
		assertEquals("01056321", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[5]")).getText());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
