package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.DriverProvider;
import utils.Properties;

public class AddressBookRemoveMultipleAddressBookTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddressBookRemoveMultipleAddressBook() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[2]/td[1]/input")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/div[2]/input")).click();
		driver.switchTo().alert().accept();
		driver.findElement(By.linkText("home")).click();
		assertFalse(driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[2]/td[3]")).getText()
				.contains("firstname1"));
		assertEquals("Number of results: 2",
				driver.findElement(By.xpath("html/body/div[1]/div[4]/label/strong")).getText());
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[2]/td[1]/input")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/div[2]/input")).click();
		driver.switchTo().alert().accept();
		driver.findElement(By.linkText("home")).click();
		assertFalse(driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[2]/td[3]")).getText()
				.contains("firstname2"));
		assertEquals("Number of results: 1",
				driver.findElement(By.xpath("html/body/div[1]/div[4]/label/strong")).getText());
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/table/tbody/tr[2]/td[1]/input")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[4]/form[2]/div[2]/input")).click();
		driver.switchTo().alert().accept();
		driver.findElement(By.linkText("home")).click();
		assertEquals("Number of results: 0",
				driver.findElement(By.xpath("html/body/div[1]/div[4]/label/strong")).getText());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
