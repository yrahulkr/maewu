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

public class PasswordManagerRemoveMultipleEntriesTest {

	private WebDriver driver;
	private BasePageObject basePageObject;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.get(Properties.app_url);
		basePageObject = new BasePageObject(driver);
	}

	@Test
	public void testPasswordManagerRemoveMultipleEntries() throws Exception {
		driver.findElement(By.id("LoginForm_username")).clear();
		driver.findElement(By.id("LoginForm_username")).sendKeys("admin");
		driver.findElement(By.id("LoginForm_password")).clear();
		driver.findElement(By.id("LoginForm_password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='login-form']/div/div[2]/a")).click();
		driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[4]/a[5]")).click();
		driver.switchTo().alert().accept();
//		Thread.sleep(1000);
		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[2]/td[4]/a[5]"));
		basePageObject.click(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[2]/td[4]/a[5]"));
//		driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[2]/td[4]/a[5]")).click();
		driver.switchTo().alert().accept();
//		Thread.sleep(1000);
		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[4]/a[5]"));
		basePageObject.click(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[4]/a[5]"));
//		driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[4]/a[5]")).click();
		driver.switchTo().alert().accept();
//		Thread.sleep(1000);
		basePageObject.waitForElementBeingPresentOnPage(By.className("empty"));
		assertTrue(driver.findElement(By.className("empty")).getText().contains("No results found."));
		driver.findElement(By.linkText("Profile")).click();
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
