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

public class AddressBookSearchMultipleAddressBookNameTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookSearchMultipleAddressBookName() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		assertEquals("Number of results: 3",
				driver.findElement(By.xpath("html/body/div[1]/div[4]/label/strong")).getText());
		assertEquals("lastname1", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[2]")).getText());
		assertEquals("firstname1", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[3]")).getText());
		assertEquals("mail1@mail.it",
				driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[4]")).getText());
		assertEquals("01056321", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[5]")).getText());
		assertEquals("lastname2", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[2]")).getText());
		assertEquals("firstname2", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[3]")).getText());
		assertEquals("mail2@mail.it",
				driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[4]")).getText());
		assertEquals("01056322", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[5]")).getText());
		assertEquals("lastname3", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[2]")).getText());
		assertEquals("firstname3", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[3]")).getText());
		assertEquals("mail3@mail.it",
				driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[4]")).getText());
		assertEquals("01056323", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[5]")).getText());
		driver.findElement(By.name("searchstring")).clear();
		driver.findElement(By.name("searchstring")).sendKeys("lastname1");
		assertEquals("Number of results: 1",
				driver.findElement(By.xpath("html/body/div[1]/div[4]/label/strong")).getText());
		assertEquals("lastname1", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[2]")).getText());
		assertEquals("firstname1", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[3]")).getText());
		assertEquals("mail1@mail.it",
				driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[4]")).getText());
		assertEquals("01056321", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[5]")).getText());
		driver.findElement(By.name("searchstring")).clear();
		driver.findElement(By.name("searchstring")).sendKeys("lastname2");
		assertEquals("lastname2", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[2]")).getText());
		assertEquals("firstname2", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[3]")).getText());
		assertEquals("mail2@mail.it",
				driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[4]/a")).getText());
		assertEquals("01056322", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[5]")).getText());
		driver.findElement(By.name("searchstring")).clear();
		driver.findElement(By.name("searchstring")).sendKeys("lastname3");
		assertEquals("lastname3", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[2]")).getText());
		assertEquals("firstname3", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[3]")).getText());
		assertEquals("mail3@mail.it",
				driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[4]")).getText());
		assertEquals("01056323", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[5]")).getText());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
