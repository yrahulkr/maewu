package com.crawljax.examples.angularphonecat;

import java.util.concurrent.TimeUnit;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawlRules.FormFillMode;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.plugins.crawloverview.CrawlOverview;

public class PhonecatCrawlingRules {

	/**
	 * List of crawling rules for the Angular Phonecat application.
	 */
	public static void setCrawlingRules(CrawljaxConfigurationBuilder builder) {

		/* crawling rules. */
		builder.crawlRules().clickElementsInRandomOrder(false);
		builder.crawlRules().clickDefaultElements();
		builder.crawlRules().crawlHiddenAnchors(true);
		builder.crawlRules().crawlFrames(false);

		builder.crawlRules().setFormFillMode(FormFillMode.RANDOM);

		/* do not click these. */

		/* set timeouts. */
		// builder.setUnlimitedRuntime();
		builder.setMaximumRunTime(60, TimeUnit.MINUTES);
		builder.setUnlimitedCrawlDepth();
		builder.setUnlimitedStates();
		builder.crawlRules().waitAfterReloadUrl(PhonecatRunner.WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(PhonecatRunner.WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);

		/* set browser. */
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.PHANTOMJS, 1));

		/* input data. */
		builder.crawlRules().setInputSpec(PhonecatCrawlingRules.phonecatInputSpecification());

		/* CrawlOverview. */
		builder.addPlugin(new CrawlOverview());
	}

	/**
	 * List of inputs to crawl the Phonecat application.
	 */
	static InputSpecification phonecatInputSpecification() {

		InputSpecification inputPhonecat = new InputSpecification();

		return inputPhonecat;
	}

}
