package com.saltlab.murun.runner;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.DriverProvider;


public class AddressBookAddAddressBookTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
//		WebDriverManager.chromedriver().setup();
//		driver = new ChromeDriver();
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get("http://localhost:3000/addressbook/");
	}

	@Test
	public void testAddressBookAddAddressBook() throws Exception {
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath(".//*[@id='content']/form/input[3]")).click();
		driver.findElement(By.partialLinkText("add ne"));
		driver.findElement(By.linkText("add new")).click();
		driver.findElement(By.name("quickadd")).click();
		driver.findElement(By.name("firstname")).clear();
		driver.findElement(By.name("firstname")).sendKeys("firstname");
		driver.findElement(By.name("lastname")).clear();
		driver.findElement(By.name("lastname")).sendKeys("lastname");
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("address");
		driver.findElement(By.name("home")).clear();
		driver.findElement(By.name("home")).sendKeys("01056321");
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("mail@mail.it");
		new Select(driver.findElement(By.name("bday"))).selectByVisibleText("19");
		new Select(driver.findElement(By.name("bmonth"))).selectByVisibleText("June");
		driver.findElement(By.name("byear")).clear();
		driver.findElement(By.name("byear")).sendKeys("1985");
		driver.findElement(By.name("submit")).click();
		assertTrue(driver.findElement(By.xpath(".//*[@id='content']/div")).getText()
				.contains("Information entered into address book"));
		driver.findElement(By.linkText("home page")).click();
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div[4]/label/strong")).getText()
				.contains("Number of results: 1"));
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();

	}
}
