package com.crawljax.examples.collabtive;

import java.util.concurrent.TimeUnit;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.examples.Plugin;
import com.crawljax.core.configuration.CrawlRules.CrawlPriorityMode;
import com.crawljax.core.configuration.CrawlRules.FormFillMode;
import com.crawljax.plugins.crawloverview.CrawlOverview;

public class CollabtiveCrawlingRules {

	/**
	 * List of crawling rules for the AddressBook application.
	 */
	public static void setCrawlingRules(CrawljaxConfigurationBuilder builder) {

		/* crawling rules. */
		builder.crawlRules().clickElementsInRandomOrder(false);
		builder.crawlRules().clickDefaultElements();
		builder.crawlRules().crawlHiddenAnchors(true);
		builder.crawlRules().crawlFrames(false);
		builder.crawlRules().clickOnce(false);

		builder.crawlRules().setFormFillMode(FormFillMode.RANDOM);

//		builder.crawlRules().click("a", "button", "input");

		/* do not click these. */
		builder.crawlRules().dontClick("a").withText("php-addressbook");
		builder.crawlRules().dontClick("a").withText("v9.0.0.1");
//		builder.crawlRules().dontClick("a").withText("map");
//		builder.crawlRules().dontClick("a").withText("export");
//		builder.crawlRules().dontClick("a").withText("import");
		
		//builder.crawlRules().setCrawlPriorityMode(CrawlPriorityMode.OLDEST_FIRST);

		/* set timeouts. */
		builder.setUnlimitedCrawlDepth();
		// builder.setMaximumRunTime(30, TimeUnit.MINUTES);
		builder.setUnlimitedStates();
		//builder.setMaximumStates(150);
		//builder.setUnlimitedRuntime();
		builder.setMaximumRunTime(1, TimeUnit.MINUTES);
		builder.crawlRules().waitAfterReloadUrl(CollabtiveRunner.WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(CollabtiveRunner.WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);

		/* set browser. */
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME, 1));

		/* input data. */
		builder.crawlRules().setInputSpec(CollabtiveCrawlingRules.collabtiveInputSpecification());

		/* CrawlOverview. */
		builder.addPlugin(new CrawlOverview());
	}

	/**
	 * List of inputs to crawl the AddressBook application.
	 */
	static InputSpecification collabtiveInputSpecification() {

		InputSpecification inputCollabtive= new InputSpecification();

		CollabtiveForms.login(inputCollabtive);
//		CollabtiveForms.newEntry(inputAddressBook);
//		CollabtiveForms.newUser(inputAddressBook);

		return inputCollabtive;
	}

}
