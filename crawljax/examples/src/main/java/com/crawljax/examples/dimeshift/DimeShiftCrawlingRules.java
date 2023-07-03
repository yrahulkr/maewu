package com.crawljax.examples.dimeshift;

import java.util.concurrent.TimeUnit;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawlRules.FormFillMode;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.examples.CoveragePlugin;
import com.crawljax.fragmentation.FragmentRules;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.plugins.crawloverview.CrawlOverview;

public class DimeShiftCrawlingRules {
	public static boolean VISUAL_DATA= true;
	/**
	 * List of crawling rules for the Angular Phonecat application.
	 */
	public static void setCrawlingRules(CrawljaxConfigurationBuilder builder) {

		/* crawling rules. */
		builder.crawlRules().clickElementsInRandomOrder(false);
		builder.crawlRules().clickDefaultElements();
		builder.crawlRules().crawlHiddenAnchors(false);
		builder.crawlRules().crawlFrames(false);
		builder.crawlRules().clickOnce(false);
		
		builder.crawlRules().enter("input").withAttribute("id", "add_transaction_text");
		builder.crawlRules().enter("input").withAttribute("id", "add_transaction_amount");
		
//		builder.crawlRules().applyNonSelAdvantage(true);
		builder.crawlRules().avoidDifferentBacktracking(true);
//		builder.crawlRules().useEquivalentReset(true);
		builder.crawlRules().avoidUnrelatedBacktracking(true);
//		builder.crawlRules().skipExploredActions(false, 2);
		builder.crawlRules().setUsefulFragmentRules(
				new FragmentRules(50, 50, 2, 4));
		builder.crawlRules().setFormFillMode(FormFillMode.RANDOM);

		/* do not click these. */
		builder.crawlRules().dontClick("a").underXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/ul[1]/li[10]/a[1]".toUpperCase());
		builder.crawlRules().dontClick("input").withAttribute("id", "demo_signup");
		builder.crawlRules().dontClick("input").withAttribute("value", "Yes, Remove");
		/* set timeouts. */
		// builder.setUnlimitedRuntime();
		builder.setMaximumRunTime(5, TimeUnit.MINUTES);
		builder.setUnlimitedCrawlDepth();
		builder.setUnlimitedStates();
		builder.crawlRules().waitAfterReloadUrl(DimeShiftRunner.WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(DimeShiftRunner.WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);

		/* set browser. */
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME, 1));

		/* input data. */
		builder.crawlRules().setInputSpec(DimeShiftCrawlingRules.dimeshiftInputSpecification());

		/* CrawlOverview. */
		builder.addPlugin(new CrawlOverview());
		builder.addPlugin(new CoveragePlugin());
	}

	/**
	 * List of inputs to crawl the Phonecat application.
	 */
	static InputSpecification dimeshiftInputSpecification() {

		InputSpecification inputDimeshift = new InputSpecification();

		DimeShiftForms.register(inputDimeshift);
		
		DimeShiftForms.login(inputDimeshift);

		return inputDimeshift;
	}

}
