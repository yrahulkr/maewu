package tests.ppma;

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

public class PasswordManagerSearchAndRemoveMultipleTagsTest {

	private WebDriver driver;
	private BasePageObject basePageObject;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.get(Properties.app_url);
		basePageObject = new BasePageObject(driver);
	}

	@Test
	public void testPasswordManagerSearchAndRemoveMultipleTags() throws Exception {
		driver.findElement(By.id("LoginForm_username")).clear();
		driver.findElement(By.id("LoginForm_username")).sendKeys("admin");
		driver.findElement(By.id("LoginForm_password")).clear();
		driver.findElement(By.id("LoginForm_password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='login-form']/div/div[2]/a")).click();
		driver.findElement(By.linkText("Tags")).click();
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Tag_name"));
		basePageObject.sendKeys(By.id("Tag_name"), "Google");
//		driver.findElement(By.id("Tag_name")).clear();
//		driver.findElement(By.id("Tag_name")).sendKeys("Google");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
		driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr/td[3]/a[3]")).click();
		driver.switchTo().alert().accept();
		driver.navigate().refresh();
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Tag_name"));
		basePageObject.sendKeys(By.id("Tag_name"), "Email");
//		driver.findElement(By.id("Tag_name")).clear();
//		driver.findElement(By.id("Tag_name")).sendKeys("Email");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
		driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr/td[3]/a[3]")).click();
		driver.switchTo().alert().accept();
		driver.navigate().refresh();
		driver.findElement(By.linkText("Advanced Search")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.id("Tag_name"));
		basePageObject.sendKeys(By.id("Tag_name"), "Facebook");
//		driver.findElement(By.id("Tag_name")).clear();
//		driver.findElement(By.id("Tag_name")).sendKeys("Facebook");
		basePageObject.click(By.name("yt0"));
//		driver.findElement(By.name("yt0")).click();
		driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr/td[3]/a[3]")).click();
		driver.switchTo().alert().accept();
		driver.navigate().refresh();
		driver.findElement(By.linkText("Profile")).click();
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
