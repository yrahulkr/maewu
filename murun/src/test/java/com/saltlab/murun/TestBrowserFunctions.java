package com.saltlab.murun;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.w3c.dom.Document;

import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.utils.CDPUtils;
import com.saltlab.murun.utils.Settings;
import com.saltlab.webmutator.utils.DomUtils;

import utils.DriverProvider;
import utils.Properties;

public class TestBrowserFunctions {

	@Test
	public void test() {
		Settings.aspectActive = false;
		ChromiumDriver driver = (ChromeDriver) DriverProvider.getInstance().getDriver_noProps(true);
		// ChromiumDriver driver
		driver.get("http://www.google.ca");
//		try {
//			Map<String, Object> parameters = new HashMap<>();
//			parameters.put("expression", 
//					"$x('//*').map(function(el){if(getEventListeners(el)['click']) return getEventListeners(el)['click'][0].listener.toString()})"		
//					);
//			parameters.put("includeCommandLineAPI", Boolean.TRUE);
//			parameters.put("returnByValue", Boolean.TRUE);
//			
//			Map<String, Object> result = driver.executeCdpCommand("Runtime.evaluate", parameters);
//
//			System.out.println(result);
//		} finally {
//			driver.close();
//		}
		
		try {
			Document dom = DomUtils.asDocument(DomUtils.getStrippedDom(driver.getPageSource()));
			CDPUtils.cleanDom(dom);
			CDPUtils.populateStyle(dom, driver);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			driver.close();
		}
	}
	
	public void recordJavaScriptCoverage(WebDriver driver) {
		Object recordedAccess = ((org.openqa.selenium.JavascriptExecutor)driver).executeScript("return localStorage.getItem('mutationData')");
		System.out.println(recordedAccess);
	}
	
	@Test
	public void testCoverageFromBrowser() {
		Settings.mutationRun = false;
		new Properties(SUBJECT.dummy);
		ChromeDriver driver = (ChromeDriver) DriverProvider.getInstance().getDriver();
		
		driver.navigate().to("http://www.google.ca");
		
		recordJavaScriptCoverage(driver);
		
		driver.close();
	}
	
	@Test
	public void TestPageLoadScript() {
		Settings.USE_CRAWLJAX = false;
		Settings.aspectActive = false;
		new Properties(SUBJECT.addressbook);
		ChromeDriver driver = (ChromeDriver) DriverProvider.getInstance().getDriver();
		driver.navigate().to("http://localhost:3000/addressbook/index.php");
		driver.findElement(By.name("user")).sendKeys("admin");
		driver.findElement(By.name("pass")).sendKeys("secret");
		driver.findElement(By.xpath("/html/body/div/div[4]/form/input[3]")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("expression", "a=$x('/html/body/div/div[4]/form[2]/div[2]/input')[0]; a.attributes.removeNamedItem('onclick'); a.addEventListener('click', DeleteSel); a.removeListenerType('click');");
		parameters.put("includeCommandLineAPI", Boolean.TRUE);
		parameters.put("returnByValue", Boolean.TRUE);
		Map<String, Object> attributeString = driver.executeCdpCommand("Runtime.evaluate", parameters);
		System.out.println(attributeString);
//		driver.executeCdpCommand("Runtime.evaluate", parameters);
		
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			driver.close();
		}
	}

	// public static void main(String[] args) {
	// Launcher launcher = new Launcher();
	//
	// try (SessionFactory factory = launcher.launch();
	// Session session = factory.create()) {
	//
	// session.navigate("http://localhost:3000/addressbook/index.php");
	// session.waitDocumentReady();
	// session.activate();
	// Thread.sleep(1000);
	// // Returns a document snapshot, including the full DOM tree of the root node
	// (including iframes,
	// // template contents, and imported documents).
	//// String snapshot = session.getContent();
	//// System.out.println(snapshot);
	//
	//// String script = "return
	// $x('//a').map(function(el){if(getEventListeners(el)[\"click\"]) return
	// getEventListeners(el)[\"click\"][0].listener.toString()})";
	//
	// session.getCommand().getDOM().enable();
	// Node rootNode = null;
	// List<Node> allNodes = session.getCommand().getDOM().getFlattenedDocument();
	// for(Node node: allNodes) {
	// System.out.println(node.getNodeName() + ":" + node.getNodeId() + ":" +
	// node.getBackendNodeId());
	// if(node.getNodeName().equalsIgnoreCase("body")) {
	// rootNode = node;
	// }
	// }
	//// System.out.println("" + session.getNodeId("/HTML/BODY"));
	//
	//// session.getCommand().getDOM().querySelectorAll(17, "//a");
	//
	// try {
	// Integer nodeIds = session.getCommand().getDOM().querySelector(22, "//*");
	//// for(Integer id : nodeIds) {
	// System.out.println(nodeIds);
	//// }
	// }catch(Exception ex) {
	// ex.printStackTrace();
	// }
	//
	//// PerformSearchResult result =
	// session.getCommand().getDOM().performSearch("//input");
	//// .getDocument().getChildren().get(0).getSearchResults(result.getSearchId(),
	// 0, result.getResultCount());
	//
	// //session.getCommand().getDOMDebugger().getEventListeners(objectId)
	//
	// session.close();
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// catch(Exception ex){
	// ex.printStackTrace();
	// }finally {
	// launcher.kill();
	// }
	// }
	//

	// @Test
	// public void testChromeDevTools() {
	// Session session = ChromeDevToolsHelper.getChromeSession();
	// URL url;
	// try {
	// url = new URL("http://www.google.ca");
	// session.navigate(url.toString());
	// session.waitDocumentReady();
	// } catch (MalformedURLException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//
	// try {
	// Thread.sleep(2000);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// ChromeDevToolsHelper.killSession();
	//
	// }
	
	
}
