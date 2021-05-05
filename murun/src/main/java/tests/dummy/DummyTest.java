package tests.dummy;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.DriverProvider;

public class DummyTest {
	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		File file = new File("src/main/resources/dummyWebPage.html");
		System.out.println(new URI(file.getAbsolutePath()).getRawPath());
		driver.get("file://" + file.getAbsolutePath());		
		
	}

	@Test
	public void testDummy() throws Exception {
		
		driver.findElement(By.id("buttonId")).click();
		String text = driver.findElement(By.id("textId")).getText();
		assertEquals(text, "hello");
		
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();

	}
}
