package tests.mrbs;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;
import utils.Properties;

public class AddLongNameBuildingNegativeTest {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(Properties.app_url);
	}

	@Test
	public void testAddLongNameBuildingNegative() throws Exception {
		driver.findElement(By.xpath(".//*[@id='logon_box']/form/div/input[3]")).click();
		driver.findElement(By.name("NewUserName")).clear();
		driver.findElement(By.name("NewUserName")).sendKeys("administrator");
		driver.findElement(By.name("NewUserPassword")).clear();
		driver.findElement(By.name("NewUserPassword")).sendKeys("secret");
		driver.findElement(By.cssSelector("input.submit")).click();
		driver.findElement(By.linkText("Rooms")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("LongMoreThanThirtyCharactersBuildingName");
		driver.findElement(By.xpath("//input[@value='Add Area']")).click();
		assertFalse(driver.findElement(By.xpath(".//*[@id='area_select']")).getText()
				.matches("^[\\s\\S]*LongMoreThanThirtyCharactersBuildingName[\\s\\S]*$"));
		assertTrue(driver.findElement(By.xpath(".//*[@id='area_select']")).getText()
				.matches("^[\\s\\S]*LongMoreThanThirtyCharactersBu[\\s\\S]*$"));
		driver.findElement(By.xpath(".//*[@id='areaChangeForm']/fieldset/input[6]")).click();
		driver.findElement(By.cssSelector("#logon_box > form > div > input[type=\"submit\"]")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
