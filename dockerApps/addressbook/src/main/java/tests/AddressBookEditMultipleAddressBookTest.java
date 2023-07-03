package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.DriverProvider;
import utils.Properties;

public class AddressBookEditMultipleAddressBookTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookEditMultipleAddressBook() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[7]/a/img")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("newaddress1");
		driver.findElement(By.name("home")).clear();
		driver.findElement(By.name("home")).sendKeys("111111");
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("newmail1@mail.it");
		driver.findElement(By.xpath(".//*[@id='content']/form[1]/input[19]")).click();
		assertTrue(driver.findElement(By.xpath(".//*[@id='content']/div")).getText()
				.contains("Address book updated"));
		driver.findElement(By.linkText("home page")).click();
		driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[7]/a/img")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("newaddress2");
		driver.findElement(By.name("home")).clear();
		driver.findElement(By.name("home")).sendKeys("222222");
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("newmail2@mail.it");
		driver.findElement(By.xpath(".//*[@id='content']/form[1]/input[19]")).click();
		assertTrue(driver.findElement(By.xpath(".//*[@id='content']/div")).getText()
				.contains("Address book updated"));
		driver.findElement(By.linkText("home page")).click();
		driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[7]/a/img")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("newaddress3");
		driver.findElement(By.name("home")).clear();
		driver.findElement(By.name("home")).sendKeys("333333");
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("newmail3@mail.it");
		driver.findElement(By.xpath(".//*[@id='content']/form[1]/input[19]")).click();
		assertTrue(driver.findElement(By.xpath(".//*[@id='content']/div")).getText()
				.contains("Address book updated"));
		driver.findElement(By.linkText("home page")).click();
		assertEquals("newmail1@mail.it",
				driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[4]")).getText());
		assertEquals("111111", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[2]/td[5]")).getText());
		assertEquals("newmail2@mail.it",
				driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[4]")).getText());
		assertEquals("222222", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[3]/td[5]")).getText());
		assertEquals("newmail3@mail.it",
				driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[4]")).getText());
		assertEquals("333333", driver.findElement(By.xpath(".//*[@id='maintable']/tbody/tr[4]/td[5]")).getText());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
