package tests.collabtive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class EditRoleTest {

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
	public void testCollabtiveEditRole() throws Exception {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("admin");
		driver.findElement(By.cssSelector("button.loginbutn")).click();
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[3]/a")).click();
		driver.findElement(By.xpath(".//*[@id='contentwrapper']/div[1]/ul/li[2]/a")).click();
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[6]/table/tbody[1]/tr[1]/td[2]/div/a"))
				.click();
		driver.findElement(By.id("rolename")).clear();
		driver.findElement(By.id("rolename")).sendKeys("NewRole");
		driver.findElement(By.xpath(
				"html/body/div[1]/div[2]/div[2]/div/div/div[6]/table/tbody[1]/tr[2]/td[2]/div[2]/form/fieldset/div[3]/div[3]/input"))
				.click();
		driver.findElement(By.xpath(
				"html/body/div[1]/div[2]/div[2]/div/div/div[6]/table/tbody[1]/tr[2]/td[2]/div[2]/form/fieldset/div[3]/div[5]/input"))
				.click();
		driver.findElement(By.xpath(
				"html/body/div[1]/div[2]/div[2]/div/div/div[6]/table/tbody[1]/tr[2]/td[2]/div[2]/form/fieldset/div[5]/button[1]"))
				.click();
		assertEquals("NewRole",
				driver.findElement(
						By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[6]/table/tbody[1]/tr[1]/td[2]/div/a"))
						.getText());
		driver.findElement(By.xpath("html/body/div[1]/div[2]/div[2]/div/div/div[6]/table/tbody[1]/tr[1]/td[2]/div/a"))
				.click();
		assertFalse(driver.findElement(By.xpath(
				"html/body/div[1]/div[2]/div[2]/div/div/div[6]/table/tbody[1]/tr[2]/td[2]/div[2]/form/fieldset/div[3]/div[5]/input"))
				.isSelected());
		assertFalse(driver.findElement(By.xpath(
				"html/body/div[1]/div[2]/div[2]/div/div/div[6]/table/tbody[1]/tr[2]/td[2]/div[2]/form/fieldset/div[3]/div[5]/input"))
				.isSelected());
		driver.findElement(By.xpath(".//*[@id='mainmenue']/li[4]/a")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
