package tests.ppma;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import utils.BasePageObject;
import utils.DriverProvider;
import utils.Properties;

public class PasswordManagerAddEntryTest {

	private WebDriver driver;
	private BasePageObject basePageObject;


	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.get(Properties.app_url);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		basePageObject = new BasePageObject(driver);
	}

	@Test
	public void testPasswordManagerAddEntry() throws Exception {
		driver.findElement(By.id("LoginForm_username")).clear();
		driver.findElement(By.id("LoginForm_username")).sendKeys("admin");
		driver.findElement(By.id("LoginForm_password")).clear();
		driver.findElement(By.id("LoginForm_password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='login-form']/div/div[2]/a")).click();
		basePageObject.mouseOver(By.linkText("Entries"));
//		driver.findElement(By.linkText("Entries")).click();
		driver.findElement(By.linkText("Create")).click();
		driver.findElement(By.id("Entry_name")).clear();
		driver.findElement(By.id("Entry_name")).sendKeys("Google");
		driver.findElement(By.id("Entry_username")).clear();
		driver.findElement(By.id("Entry_username")).sendKeys("myaccount@google.it");
		driver.findElement(By.id("Entry_password")).clear();
		driver.findElement(By.id("Entry_password")).sendKeys("mypassword");
		driver.findElement(By.id("Entry_tagList")).clear();
		driver.findElement(By.id("Entry_tagList")).sendKeys("Email, Google");
		driver.findElement(By.id("Entry_url")).clear();
		driver.findElement(By.id("Entry_url")).sendKeys("www.google.it/mail");
		driver.findElement(By.id("Entry_comment")).clear();
		driver.findElement(By.id("Entry_comment")).sendKeys("My personal email");
		driver.findElement(By.name("yt0")).click();
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[4]/table/tbody/tr/td[1]")).getText().contains("Google"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[4]/table/tbody/tr/td[2]")).getText().contains("myaccount@google.it"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[4]/table/tbody/tr/td[3]")).getText().contains("Email, Google"));
		driver.findElement(By.linkText("Profile")).click();
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
