package tests.mantisbt;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import utils.DriverProvider;
import utils.Properties;

public class AddProjectTest {

	private WebDriver driver;

	@Before
	public void setUp() {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testMantisBTAddProject() throws Exception {
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("administrator");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("root");
		driver.findElement(By.cssSelector("input.button")).click();
		driver.findElement(By.linkText("Manage")).click();
		driver.findElement(By.linkText("Manage Projects")).click();
		driver.findElement(By.cssSelector("td.form-title > form > input.button-small")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Project001");
		new Select(driver.findElement(By.name("status"))).selectByVisibleText("release");
		new Select(driver.findElement(By.name("view_state"))).selectByVisibleText("public");
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("description");
		driver.findElement(By.cssSelector("input.button")).click();
//		driver.findElement(By.linkText("Proceed")).click();
//		Thread.sleep(Long.valueOf(Properties.wait));
		assertEquals("Project001", driver.findElement(By.xpath("html/body/table[3]/tbody/tr[3]/td[1]/a")).getText());
		assertEquals("release", driver.findElement(By.xpath("html/body/table[3]/tbody/tr[3]/td[2]")).getText());
		assertEquals("public", driver.findElement(By.xpath("html/body/table[3]/tbody/tr[3]/td[4]")).getText());
		assertEquals("description", driver.findElement(By.xpath("html/body/table[3]/tbody/tr[3]/td[5]")).getText());
		driver.findElement(By.linkText("Logout")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
