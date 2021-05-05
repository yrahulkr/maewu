package com.saltlab.murun;

import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;

import com.saltlab.murun.runner.MutationRunResult;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.runner.TestFailureDetail;
import com.saltlab.murun.runner.TraceSession;
import com.saltlab.murun.runner.UtilsRunner;
import com.saltlab.murun.utils.Settings;

import utils.DriverProvider;
import utils.Properties;
import utils.TestCaseExecutor;

public class TestCrawljaxIntegration {
	@Test
	public void testEmbeddedBrowser() {
		
		TraceSession provider = TraceSession.getInstance();
		try {
			Settings.USE_CRAWLJAX = true;
			new Properties(SUBJECT.addressbook);

			provider.crawljaxSetup("testCrawljax", "http://localhost:3000");
			WebDriver driver = DriverProvider.getInstance().getDriver();
			driver.navigate().to("http://localhost:3000/addressbook");
			System.out.println(provider.getBrowser().getStrippedDom());
			provider.getPlugins().runPreCrawlingPlugins(provider.getConfig());
			provider.newNavState("index");
			
//			HybridStateVertexFactory factory = new HybridStateVertexFactory(0, provider.getBuilder(), true);
//			StateVertex newState = factory.newStateVertex(0, provider.getBrowser().getCurrentUrl(), "index", provider.getBrowser().getDom(), provider.getBrowser().getStrippedDom(), provider.getBrowser());
//			provider.getPlugins().runOnNewStatePlugins(provider.getContext(), newState);
//			CrawlSessionProvider crawlSessionProvider = provider.getSessionProvider();
//			crawlSessionProvider.setup(newState, provider.getContext());
//			provider.getPlugins().runOnNewStatePlugins(provider.getContext(), newState);
//			executeConsumers(firstConsumer);
//			CrawlSession session = crawlSessionProvider.get();
//			provider.getPlugins().runPostCrawlingPlugins(session, null);
			provider.stopTrace();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			provider.getBrowser().close();
		}

	}
	
	@Test
	public void testCoverage() {
		
		
		Settings.traceDir = "testCrawljax";

		Settings.outputDir = Settings.traceDir;
		

		Settings.outputDir = Settings.traceDir;
		
		
		SUBJECT subject = SUBJECT.addressbook;
		
		TestCaseExecutor.restartDocker(subject, true);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String traceOutput = Paths.get(Settings.outputDir, subject.name()).toString();
		
			
		TraceSession session = TraceSession.getInstance();
		Settings.aspectActive = true;
		long start = System.currentTimeMillis();

		Settings.currResult = new MutationRunResult(null, Settings.outputDir);
		try {
			Optional<Description> failure = UtilsRunner.executeTests(subject, false);
			if(failure.isPresent()) {
				Settings.currResult.setFailed(true);
			}
		}catch(Exception ex) {
			Settings.currResult.setFailed(true);
			TestFailureDetail detail = new TestFailureDetail("overall", ex.toString(), ex.toString());
			Settings.currResult.addTestResult(detail);
			session = null;
		}
		
		System.out.println(session.getCoverageMap());
		System.out.println(session.getObserverCoverageMap());
		
//		session.stopTrace();
		System.out.println(Settings.currResult);
		TestCaseExecutor.restartDocker(subject, false);
	}
	
}
