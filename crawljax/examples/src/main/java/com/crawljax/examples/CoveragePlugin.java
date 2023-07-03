package com.crawljax.examples;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.plugin.OnBrowserClosePlugin;
import com.crawljax.core.plugin.OnBrowserCreatedPlugin;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.plugin.OnRevisitStatePlugin;
import com.crawljax.core.plugin.OnUrlLoadPlugin;
import com.crawljax.core.plugin.PreCrawlingPlugin;
import com.crawljax.core.state.StateVertex;

import code.coverage.CoverageInfo;
import code.coverage.CoverageManager;

public class CoveragePlugin implements OnBrowserClosePlugin, PreCrawlingPlugin, OnUrlLoadPlugin, OnNewStatePlugin, OnRevisitStatePlugin{
	
	 CoverageManager coverageManager = new CoverageManager(); 

	 public void saveIntermediateCoverageReportAndReset(WebDriver driver, String pathToESTestSuite) {
		 Object coverage = coverageManager.getCoverageObject(driver); 
		 if(coverage == null) {
			 return;
		 }
		 coverageManager.sendCoverageObjectToExpressServer(coverage); 
		 String htmlCoverageReport = coverageManager.getCoverageReportFromExpressServer();
		 List<CoverageInfo> coverageInfos = coverageManager.parseHTMLResponse(htmlCoverageReport); 
		 for(CoverageInfo info: coverageInfos) {
			 System.out.println(info.toString());
		 }
		 coverageManager.writeCoverageReport(coverageInfos, pathToESTestSuite);  
	 }

	 
	 

	@Override
	public void onBrowserClose(CrawlerContext context) {
		System.out.println("Before reload");
		saveIntermediateCoverageReportAndReset(context.getBrowser().getWebDriver(), context.getConfig().getOutputDir().getAbsolutePath());
	}



	@Override
	public void preCrawling(CrawljaxConfiguration config) throws RuntimeException {
		coverageManager.resetCoverageStats();
		String htmlCoverageReport = coverageManager.getCoverageReportFromExpressServer();
		if(htmlCoverageReport != null) {
			System.out.println("After reset!!");
			System.out.println(htmlCoverageReport);
			try {
			 List<CoverageInfo> coverageInfos = coverageManager.parseHTMLResponse(htmlCoverageReport); 
			 
			 for(CoverageInfo info: coverageInfos) {
				 System.out.println(info.toString());
			 }
			 }catch(Exception ex) {
					System.out.println("No existing coverage report " +  ex.getMessage());

			 }
		}
		else {
			System.out.println("No existing coverage report ");
		}
	}




	@Override
	public void onUrlLoad(CrawlerContext context) {
		System.out.println("After reload");
		saveIntermediateCoverageReportAndReset(context.getBrowser().getWebDriver(), context.getConfig().getOutputDir().getAbsolutePath());
	}




	@Override
	public void onNewState(CrawlerContext context, StateVertex newState) {
		System.out.println("On New state");
		saveIntermediateCoverageReportAndReset(context.getBrowser().getWebDriver(), context.getConfig().getOutputDir().getAbsolutePath());
	}




	@Override
	public void onRevisitState(CrawlerContext context, StateVertex currentState) {
		System.out.println("On Revisit state");
		saveIntermediateCoverageReportAndReset(context.getBrowser().getWebDriver(), context.getConfig().getOutputDir().getAbsolutePath());
	}

}
