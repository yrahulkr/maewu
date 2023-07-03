package com.crawljax.examples.pagekit;

import java.util.concurrent.TimeUnit;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawlRules.FormFillMode;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.fragmentation.FragmentRules;
import com.crawljax.plugins.crawloverview.CrawlOverview;

public class PageKitCrawlingRules {

	/**
	 * List of crawling rules for the Angular Phonecat application.
	 */
	public static void setCrawlingRules(CrawljaxConfigurationBuilder builder) {

		/* crawling rules. */
		builder.crawlRules().clickElementsInRandomOrder(false);
		builder.crawlRules().clickDefaultElements();
		builder.crawlRules().crawlHiddenAnchors(true);
		builder.crawlRules().crawlFrames(false);
		builder.crawlRules().clickOnce(false);

		builder.crawlRules().setFormFillMode(FormFillMode.RANDOM);
		builder.crawlRules().setUsefulFragmentRules(new FragmentRules(50, 50, 2, 4));

		/* do not click these. */
		builder.crawlRules().dontClick("a").withAttribute("title", "Logout");
		builder.crawlRules().dontClick("a").withAttribute("title", "Visit Site");
//		builder.crawlRules().dontClick("a").withText("Request Password");
		// Changes password and cannot login after that
		builder.crawlRules().dontClick("a").underXPath("/HTML[1]/BODY[1]/MAIN[1]/FORM[1]/DIV[1]/DIV[2]/BUTTON[1]");
		builder.crawlRules().dontClick("a").withText("Logout");
		/* set timeouts. */
		// builder.setUnlimitedRuntime();
		builder.setMaximumRunTime(5, TimeUnit.MINUTES);
		builder.setUnlimitedCrawlDepth();
		builder.setUnlimitedStates();
		builder.crawlRules().waitAfterReloadUrl(PageKitRunner.WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(PageKitRunner.WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);

		/* set browser. */
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME, 1));

		/* input data. */
		builder.crawlRules().setInputSpec(PageKitCrawlingRules.pagekitInputSpecification());

		/* CrawlOverview. */
		builder.addPlugin(new CrawlOverview());
	}

	/**
	 * List of inputs to crawl the Phonecat application.
	 */
	static InputSpecification pagekitInputSpecification() {

		InputSpecification inputPagekit = new InputSpecification();

		PageKitForms.register(inputPagekit);
		
		PageKitForms.login(inputPagekit);
		
		PageKitForms.redirect(inputPagekit);

		return inputPagekit;
	}

}
