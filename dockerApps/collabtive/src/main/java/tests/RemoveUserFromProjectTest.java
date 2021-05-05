package tests;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import utils.DriverProvider;
import utils.Properties;

// it was DeassignUserToProjectTest
public class RemoveUserFromProjectTest {

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
	public void testCollabtiveRemoveUserFromProject() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath("//*[@id='mainmenue']/li[3]/a")).click();
		driver.findElement(By.linkText("Project001")).click();
		driver.findElement(By.xpath("//*[@id='contentwrapper']/div[1]/ul/li[6]/a")).click();
		new Actions(driver).moveToElement(driver.findElement(By.xpath(
				"html/body/div[1]/div[2]/div[2]/div/div/div[3]/div[2]/div[2]/div/div/ul/li[2]/div/table/tbody/tr[1]/td[2]/a/img")))
				.build().perform();
		driver.findElement(By.xpath("(//table/tbody/tr[1]/td[3]/div/a[1])[2]")).click();
		new Select(driver.findElement(By.id("assignto"))).selectByVisibleText("admin");
		driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
		Thread.sleep(Long.valueOf(Properties.wait));
		try {
			driver.findElement(By.partialLinkText("username001"));
		} catch (NoSuchElementException e) {
			driver.findElement(By.xpath("//*[@id='mainmenue']/li[4]/a")).click();
		}
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
