package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class AddAndRemoveMultipleTasksTest {

	private WebDriver driver;

	@Before
	public void setUp() {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testCollabtiveAddAndRemoveMultipleTasks() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[3]/a")).click();
		driver.findElement(By.linkText("Project001")).click();
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[3]/a")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div[3]/div/a[1]")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("mytask1");
		driver.findElement(
				By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div[1]/div/form/fieldset/div[6]/button[1]"))
				.click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div[3]/div/a[1]")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("mytask2");
		driver.findElement(
				By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div[1]/div/form/fieldset/div[6]/button[1]"))
				.click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div[3]/div/a[1]")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("mytask3");
		driver.findElement(
				By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div[1]/div/form/fieldset/div[6]/button[1]"))
				.click();
		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*mytask1[\\s\\S]*$"));
		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*mytask2[\\s\\S]*$"));
		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*mytask3[\\s\\S]*$"));
		driver.findElement(
				By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div[2]/table/tbody[1]/tr[1]/td[5]/a[2]"))
				.click();
		Thread.sleep(200);
		assertTrue(
				driver.switchTo().alert().getText().matches("^Really delete this item[\\s\\S]\nDeleting cannot be undone\\.$"));
		driver.switchTo().alert().accept();
		driver.findElement(
				By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div[2]/table/tbody[2]/tr[1]/td[5]/a[2]"))
				.click();
		Thread.sleep(200);
		assertTrue(
				driver.switchTo().alert().getText().matches("^Really delete this item[\\s\\S]\nDeleting cannot be undone\\.$"));
		driver.switchTo().alert().accept();
		driver.findElement(
				By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[5]/div[2]/table/tbody[3]/tr[1]/td[5]/a[2]"))
				.click();
		Thread.sleep(200);
		assertTrue(
				driver.switchTo().alert().getText().matches("^Really delete this item[\\s\\S]\nDeleting cannot be undone\\.$"));
		driver.switchTo().alert().accept();
		driver.navigate().refresh();
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*mytask1[\\s\\S]*$"));
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*mytask2[\\s\\S]*$"));
		assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*mytask3[\\s\\S]*$"));
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[4]/a")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
