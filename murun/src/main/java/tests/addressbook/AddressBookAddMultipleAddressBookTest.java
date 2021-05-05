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

public class AddressBookAddMultipleAddressBookTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookAddMultipleAddressBook() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		driver.findElement(By.linkText("add new")).click();
		driver.findElement(By.name("quickadd")).click();
		driver.findElement(By.name("firstname")).clear();
		driver.findElement(By.name("firstname")).sendKeys("firstname1");
		driver.findElement(By.name("lastname")).clear();
		driver.findElement(By.name("lastname")).sendKeys("lastname1");
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("address1");
		driver.findElement(By.name("home")).clear();
		driver.findElement(By.name("home")).sendKeys("01056321");
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("mail1@mail.it");
		new Select(driver.findElement(By.name("bday"))).selectByVisibleText("11");
		new Select(driver.findElement(By.name("bmonth"))).selectByVisibleText("June");
		driver.findElement(By.name("byear")).clear();
		driver.findElement(By.name("byear")).sendKeys("1981");
		driver.findElement(By.name("submit")).click();
		driver.findElement(By.linkText("add next")).click();
		driver.findElement(By.name("quickadd")).click();
		driver.findElement(By.name("firstname")).clear();
		driver.findElement(By.name("firstname")).sendKeys("firstname2");
		driver.findElement(By.name("lastname")).clear();
		driver.findElement(By.name("lastname")).sendKeys("lastname2");
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("address2");
		driver.findElement(By.name("home")).clear();
		driver.findElement(By.name("home")).sendKeys("01056322");
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("mail2@mail.it");
		new Select(driver.findElement(By.name("bday"))).selectByVisibleText("12");
		new Select(driver.findElement(By.name("bmonth"))).selectByVisibleText("June");
		driver.findElement(By.name("byear")).clear();
		driver.findElement(By.name("byear")).sendKeys("1982");
		driver.findElement(By.name("submit")).click();
		driver.findElement(By.linkText("add next")).click();
		driver.findElement(By.name("quickadd")).click();
		driver.findElement(By.name("firstname")).clear();
		driver.findElement(By.name("firstname")).sendKeys("firstname3");
		driver.findElement(By.name("lastname")).clear();
		driver.findElement(By.name("lastname")).sendKeys("lastname3");
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("address3");
		driver.findElement(By.name("home")).clear();
		driver.findElement(By.name("home")).sendKeys("01056323");
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("mail3@mail.it");
		new Select(driver.findElement(By.name("bday"))).selectByVisibleText("13");
		new Select(driver.findElement(By.name("bmonth"))).selectByVisibleText("June");
		driver.findElement(By.name("byear")).clear();
		driver.findElement(By.name("byear")).sendKeys("1983");
		driver.findElement(By.name("submit")).click();
		driver.findElement(By.linkText("home page")).click();
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
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
