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

public class PasswordManagerSearchTagNegativeTest {

	private WebDriver driver;
	private BasePageObject basePageObject;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.get(Properties.app_url);
		basePageObject = new BasePageObject(driver);
	}

	@Test
	public void testPasswordManagerSearchTagNegative() throws Exception {
		driver.findElement(By.id("LoginForm_username")).clear();
		driver.findElement(By.id("LoginForm_username")).sendKeys("admin");
		driver.findElement(By.id("LoginForm_password")).clear();
		driver.findElement(By.id("LoginForm_password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='login-form']/div/div[2]/a")).click();
		driver.findElement(By.linkText("Tags")).click();
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Tag_name"));
		basePageObject.sendKeys(By.id("Tag_name"), "NotATag");
//		driver.findElement(By.id("Tag_name")).clear();
//		driver.findElement(By.id("Tag_name")).sendKeys("NotATag");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
		assertTrue(driver.findElement(By.className("empty")).getText().contains("No results found."));
		driver.findElement(By.linkText("Profile")).click();
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
