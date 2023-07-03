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

public class PasswordManagerAddTagTest {

	private WebDriver driver;
	private BasePageObject basePageObject;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.get(Properties.app_url);
		basePageObject = new BasePageObject(driver);
	}

	@Test
	public void testPasswordManagerAddTag() throws Exception {
		driver.findElement(By.id("LoginForm_username")).clear();
		driver.findElement(By.id("LoginForm_username")).sendKeys("admin");
		driver.findElement(By.id("LoginForm_password")).clear();
		driver.findElement(By.id("LoginForm_password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='login-form']/div/div[2]/a")).click();
//		driver.findElement(By.linkText("Tags")).click();
		basePageObject.mouseOver(By.linkText("Tags"));
		driver.findElement(By.linkText("Create")).click();
		driver.findElement(By.id("Tag_name")).clear();
		driver.findElement(By.id("Tag_name")).sendKeys("Facebook");
		driver.findElement(By.name("yt0")).click();
//		Thread.sleep(1000);
		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[4]/table/tbody/tr/td[1]"));
		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[4]/table/tbody/tr/td[2]"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[4]/table/tbody/tr/td[1]")).getText().contains("Facebook"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[4]/table/tbody/tr/td[2]")).getText().contains("0"));
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
