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

public class PasswordManagerCheckUsedTagsTest {

	private WebDriver driver;
	private BasePageObject basePageObject;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.get(Properties.app_url);
		basePageObject = new BasePageObject(driver);
	}

	@Test
	public void testPasswordManagerCheckUsedTags() throws Exception {
		driver.findElement(By.id("LoginForm_username")).clear();
		driver.findElement(By.id("LoginForm_username")).sendKeys("admin");
		driver.findElement(By.id("LoginForm_password")).clear();
		driver.findElement(By.id("LoginForm_password")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id='login-form']/div/div[2]/a")).click();
//		driver.findElement(By.linkText("Tags")).click();
//		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[2]"));
//		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[2]")).getText().contains("3"));
//		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[2]"));
//		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[2]")).getText().contains("3"));
		driver.findElement(By.linkText("Entries")).click();
		driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[4]/a[4]")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.xpath("(//input[@id='Entry_tagList'])[2]"));
		basePageObject.waitForElementBeingVisibleOnPage(By.xpath("(//input[@id='Entry_tagList'])[2]"));
//		basePageObject.sendKeys(By.xpath("(//input[@id='Entry_tagList'])[2]"), "");
		driver.findElement(By.xpath("(//input[@id='Entry_tagList'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@id='Entry_tagList'])[2]")).sendKeys("");
//		basePageObject.click(By.name("yt1"));
		basePageObject.waitForElementBeingPresentOnPage(By.name("yt1"));
		basePageObject.waitForElementBeingClickable(By.name("yt1"));
		driver.findElement(By.name("yt1")).click();
		driver.navigate().refresh();
//		driver.findElement(By.linkText("Tags")).click();
//		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[2]"));
//		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[2]")).getText().contains("2"));
//		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[2]"));
//		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[2]")).getText().contains("2"));
		driver.findElement(By.linkText("Entries")).click();
		driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[2]/td[4]/a[4]")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.xpath("(//input[@id='Entry_tagList'])[2]"));
		basePageObject.waitForElementBeingVisibleOnPage(By.xpath("(//input[@id='Entry_tagList'])[2]"));
//		basePageObject.sendKeys(By.xpath("(//input[@id='Entry_tagList'])[2]"), "");
		driver.findElement(By.xpath("(//input[@id='Entry_tagList'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@id='Entry_tagList'])[2]")).sendKeys("");
//		basePageObject.click(By.name("yt1"));
		basePageObject.waitForElementBeingPresentOnPage(By.name("yt1"));
		basePageObject.waitForElementBeingClickable(By.name("yt1"));
		driver.findElement(By.name("yt1")).click();
		driver.navigate().refresh();
//		driver.findElement(By.linkText("Tags")).click();
//		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[2]"));
//		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[2]")).getText().contains("1"));
//		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[2]"));
//		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[2]")).getText().contains("1"));
		driver.findElement(By.linkText("Entries")).click();
		driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[4]/a[4]")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.xpath("(//input[@id='Entry_tagList'])[2]"));
		basePageObject.waitForElementBeingVisibleOnPage(By.xpath("(//input[@id='Entry_tagList'])[2]"));
//		basePageObject.sendKeys(By.xpath("(//input[@id='Entry_tagList'])[2]"), "");
		driver.findElement(By.xpath("(//input[@id='Entry_tagList'])[2]")).clear();
		driver.findElement(By.xpath("(//input[@id='Entry_tagList'])[2]")).sendKeys("");
//		basePageObject.click(By.name("yt1"));
		basePageObject.waitForElementBeingPresentOnPage(By.name("yt1"));
		basePageObject.waitForElementBeingClickable(By.name("yt1"));
		driver.findElement(By.name("yt1")).click();
		driver.navigate().refresh();
		driver.findElement(By.linkText("Tags")).click();
		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[2]"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[1]/td[2]")).getText().contains("0"));
		basePageObject.waitForElementBeingPresentOnPage(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[2]"));
		assertTrue(driver.findElement(By.xpath("html/body/div[1]/div/div/div[3]/table/tbody/tr[3]/td[2]")).getText().contains("0"));
		driver.findElement(By.linkText("Profile")).click();
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
