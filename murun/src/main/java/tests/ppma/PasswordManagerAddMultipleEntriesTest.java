package tests.ppma;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import utils.BasePageObject;
import utils.DriverProvider;
import utils.Properties;

public class PasswordManagerAddMultipleEntriesTest {

	private WebDriver driver;
	private BasePageObject basePageObject;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.get(Properties.app_url);
		basePageObject = new BasePageObject(driver);
	}

	@Test
	public void testPasswordManagerAddMultipleEntries() throws Exception {
		driver.findElement(By.id("LoginForm_username")).clear();
		driver.findElement(By.id("LoginForm_username")).sendKeys("admin");
		driver.findElement(By.id("LoginForm_password")).clear();
		driver.findElement(By.id("LoginForm_password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='login-form']/div/div[2]/a")).click();
		basePageObject.mouseOver(By.linkText("Entries"));
//		driver.findElement(By.linkText("Entries")).click();
		driver.findElement(By.linkText("Create")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Entry_name"));
		basePageObject.sendKeys(By.id("Entry_name"), "Google");
//		driver.findElement(By.id("Entry_name")).clear();
//		driver.findElement(By.id("Entry_name")).sendKeys("Google");
		basePageObject.sendKeys(By.id("Entry_username"), "myaccount1@google.it");
//		driver.findElement(By.id("Entry_username")).clear();
//		driver.findElement(By.id("Entry_username")).sendKeys("myaccount1@google.it");
		basePageObject.sendKeys(By.id("Entry_password"), "mypassword1");
//		driver.findElement(By.id("Entry_password")).clear();
//		driver.findElement(By.id("Entry_password")).sendKeys("mypassword1");
		basePageObject.sendKeys(By.id("Entry_tagList"), "Email, Google");
//		driver.findElement(By.id("Entry_tagList")).clear();
//		driver.findElement(By.id("Entry_tagList")).sendKeys("Email, Google");
		basePageObject.sendKeys(By.id("Entry_url"), "www.google.it/mail");
//		driver.findElement(By.id("Entry_url")).clear();
//		driver.findElement(By.id("Entry_url")).sendKeys("www.google.it/mail");
		basePageObject.sendKeys(By.id("Entry_comment"), "My personal email");
//		driver.findElement(By.id("Entry_comment")).clear();
//		driver.findElement(By.id("Entry_comment")).sendKeys("My personal email");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
		driver.navigate().refresh();
		basePageObject.mouseOver(By.linkText("Entries"));
//		driver.findElement(By.linkText("Entries")).click();
		driver.findElement(By.linkText("Create")).click();
		basePageObject.sendKeys(By.id("Entry_name"), "Google");
//		driver.findElement(By.id("Entry_name")).clear();
//		driver.findElement(By.id("Entry_name")).sendKeys("Google");
		basePageObject.sendKeys(By.id("Entry_username"), "myaccount2@google.it");
//		driver.findElement(By.id("Entry_username")).clear();
//		driver.findElement(By.id("Entry_username")).sendKeys("myaccount2@google.it");
		basePageObject.sendKeys(By.id("Entry_password"), "mypassword2");
//		driver.findElement(By.id("Entry_password")).clear();
//		driver.findElement(By.id("Entry_password")).sendKeys("mypassword2");
		basePageObject.sendKeys(By.id("Entry_tagList"), "Email, Google");
//		driver.findElement(By.id("Entry_tagList")).clear();
//		driver.findElement(By.id("Entry_tagList")).sendKeys("Email, Google");
		basePageObject.sendKeys(By.id("Entry_url"), "www.google.it/mail");
//		driver.findElement(By.id("Entry_url")).clear();
//		driver.findElement(By.id("Entry_url")).sendKeys("www.google.it/mail");
		basePageObject.sendKeys(By.id("Entry_comment"), "My second personal email");
//		driver.findElement(By.id("Entry_comment")).clear();
//		driver.findElement(By.id("Entry_comment")).sendKeys("My second personal email");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
		driver.navigate().refresh();
		basePageObject.mouseOver(By.linkText("Entries"));
//		driver.findElement(By.linkText("Entries")).click();
		driver.findElement(By.linkText("Create")).click();
		basePageObject.sendKeys(By.id("Entry_name"), "Google");
//		driver.findElement(By.id("Entry_name")).clear();
//		driver.findElement(By.id("Entry_name")).sendKeys("Google");
		basePageObject.sendKeys(By.id("Entry_username"), "myaccount3@google.it");
//		driver.findElement(By.id("Entry_username")).clear();
//		driver.findElement(By.id("Entry_username")).sendKeys("myaccount3@google.it");
		basePageObject.sendKeys(By.id("Entry_password"), "mypassword3");
//		driver.findElement(By.id("Entry_password")).clear();
//		driver.findElement(By.id("Entry_password")).sendKeys("mypassword3");
		basePageObject.sendKeys(By.id("Entry_tagList"), "Email, Google");
//		driver.findElement(By.id("Entry_tagList")).clear();
//		driver.findElement(By.id("Entry_tagList")).sendKeys("Email, Google");
		basePageObject.sendKeys(By.id("Entry_url"), "www.google.it/mail");
//		driver.findElement(By.id("Entry_url")).clear();
//		driver.findElement(By.id("Entry_url")).sendKeys("www.google.it/mail");
		basePageObject.sendKeys(By.id("Entry_comment"), "My third personal email");
//		driver.findElement(By.id("Entry_comment")).clear();
//		driver.findElement(By.id("Entry_comment")).sendKeys("My third personal email");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
		driver.navigate().refresh();
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[1]")).getText().contains("Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[2]")).getText().contains("myaccount1@google.it"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[3]")).getText().contains("Email, Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[2]/td[1]")).getText().contains("Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[2]/td[2]")).getText().contains("myaccount2@google.it"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[2]/td[3]")).getText().contains("Email, Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[1]")).getText().contains("Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[2]")).getText().contains("myaccount3@google.it"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[3]")).getText().contains("Email, Google"));
		driver.findElement(By.linkText("Profile")).click();
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();

	}

}
