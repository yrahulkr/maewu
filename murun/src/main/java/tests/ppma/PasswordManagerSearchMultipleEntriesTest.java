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

public class PasswordManagerSearchMultipleEntriesTest {

	private WebDriver driver;
	private BasePageObject basePageObject;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.get(Properties.app_url);
		basePageObject = new BasePageObject(driver);
	}

	@Test
	public void testPasswordManagerSearchMultipleEntries() throws Exception {
		driver.findElement(By.id("LoginForm_username")).clear();
		driver.findElement(By.id("LoginForm_username")).sendKeys("admin");
		driver.findElement(By.id("LoginForm_password")).clear();
		driver.findElement(By.id("LoginForm_password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='login-form']/div/div[2]/a")).click();
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Entry_name"));
		basePageObject.sendKeys(By.id("Entry_name"), "Google");
//		driver.findElement(By.id("Entry_name")).clear();
//		driver.findElement(By.id("Entry_name")).sendKeys("Google");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.className("summary"));
//		Thread.sleep(1000);
		assertTrue(driver.findElement(By.className("summary")).getText().contains("Displaying 1-3 of 3 results."));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[1]")).getText().contains("Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[2]")).getText().contains("myaccount1@google.it"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[3]")).getText().contains("Email, Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[2]/td[1]")).getText().contains("Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[2]/td[2]")).getText().contains("myaccount2@google.it"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[2]/td[3]")).getText().contains("Email, Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[1]")).getText().contains("Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[2]")).getText().contains("myaccount3@google.it"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[3]")).getText().contains("Email, Google"));
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Entry_name"));
		basePageObject.sendKeys(By.id("Entry_name"), "");
//		driver.findElement(By.id("Entry_name")).clear();
//		driver.findElement(By.id("Entry_name")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_username"), "");
//		driver.findElement(By.id("Entry_username")).clear();
//		driver.findElement(By.id("Entry_username")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_url"), "");
//		driver.findElement(By.id("Entry_url")).clear();
//		driver.findElement(By.id("Entry_url")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_tagList"), "");
//		driver.findElement(By.id("Entry_tagList")).clear();
//		driver.findElement(By.id("Entry_tagList")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_comment"), "");
//		driver.findElement(By.id("Entry_comment")).clear();
//		driver.findElement(By.id("Entry_comment")).sendKeys("");
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingInvisibleOnPage(By.id("Entry_username"));
//		Thread.sleep(1000);
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Entry_username"));
		basePageObject.sendKeys(By.id("Entry_username"), "myaccount1@google.it");
//		driver.findElement(By.id("Entry_username")).clear();
//		driver.findElement(By.id("Entry_username")).sendKeys("myaccount1@google.it");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.className("summary"));
//		Thread.sleep(1000);
		assertTrue(driver.findElement(By.className("summary")).getText().contains("Displaying 1-1 of 1 result."));
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Entry_name"));
		basePageObject.sendKeys(By.id("Entry_name"), "");
//		driver.findElement(By.id("Entry_name")).clear();
//		driver.findElement(By.id("Entry_name")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_username"), "");
//		driver.findElement(By.id("Entry_username")).clear();
//		driver.findElement(By.id("Entry_username")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_url"), "");
//		driver.findElement(By.id("Entry_url")).clear();
//		driver.findElement(By.id("Entry_url")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_tagList"), "");
//		driver.findElement(By.id("Entry_tagList")).clear();
//		driver.findElement(By.id("Entry_tagList")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_comment"), "");
//		driver.findElement(By.id("Entry_comment")).clear();
//		driver.findElement(By.id("Entry_comment")).sendKeys("");
		driver.findElement(By.linkText("Advanced Search")).click();
//		Thread.sleep(1000);
		basePageObject.waitForElementBeingInvisibleOnPage(By.id("Entry_username"));
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Entry_username"));
		basePageObject.sendKeys(By.id("Entry_username"), "myaccount2@google.it");
//		driver.findElement(By.id("Entry_username")).clear();
//		driver.findElement(By.id("Entry_username")).sendKeys("myaccount2@google.it");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
//		Thread.sleep(1000);
		basePageObject.waitForElementBeingPresentOnPage(By.className("summary"));
//		assertTrue(driver.findElement(By.className("summary")).getText().contains("Displaying 1-1 of 1 result."));
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Entry_name"));
		basePageObject.sendKeys(By.id("Entry_name"), "");
//		driver.findElement(By.id("Entry_name")).clear();
//		driver.findElement(By.id("Entry_name")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_username"), "");
//		driver.findElement(By.id("Entry_username")).clear();
//		driver.findElement(By.id("Entry_username")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_url"), "");
//		driver.findElement(By.id("Entry_url")).clear();
//		driver.findElement(By.id("Entry_url")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_tagList"), "");
//		driver.findElement(By.id("Entry_tagList")).clear();
//		driver.findElement(By.id("Entry_tagList")).sendKeys("");
		basePageObject.sendKeys(By.id("Entry_comment"), "");
//		driver.findElement(By.id("Entry_comment")).clear();
//		driver.findElement(By.id("Entry_comment")).sendKeys("");
		driver.findElement(By.linkText("Advanced Search")).click();
//		Thread.sleep(1000);
		basePageObject.waitForElementBeingInvisibleOnPage(By.id("Entry_username"));
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Entry_username"));
		basePageObject.sendKeys(By.id("Entry_username"), "myaccount3@google.it");
//		driver.findElement(By.id("Entry_username")).clear();
//		driver.findElement(By.id("Entry_username")).sendKeys("myaccount3@google.it");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
//		Thread.sleep(1000);
		basePageObject.click(By.className("summary"));
		assertTrue(driver.findElement(By.className("summary")).getText().contains("Displaying 1-1 of 1 result."));
		driver.findElement(By.linkText("Profile")).click();
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
