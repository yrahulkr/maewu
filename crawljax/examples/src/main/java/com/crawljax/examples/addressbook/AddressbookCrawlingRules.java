package com.crawljax.examples.addressbook;

import java.util.concurrent.TimeUnit;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.BrowserOptions;
import com.crawljax.core.configuration.CrawlRules.FormFillMode;
import com.crawljax.core.configuration.CrawlRules.FormFillOrder;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.Identification.How;
import com.crawljax.forms.FormInput;
import com.crawljax.forms.FormInput.InputType;
import com.crawljax.plugins.crawloverview.CrawlOverview;

public class AddressbookCrawlingRules {

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
		builder.crawlRules().setFormFillOrder(FormFillOrder.VISUAL);
		builder.crawlRules().avoidDifferentBacktracking(true);
		builder.crawlRules().setRestoreConnectedStates(true);
		builder.crawlRules().skipExploredActions(false, 3);
//		builder.crawlRules().click("a", "button", "input");

		/* do not click these. */
		builder.crawlRules().crawlHiddenAnchors(false);
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
		builder.setMaximumRunTime(120, TimeUnit.MINUTES);
		builder.crawlRules().waitAfterReloadUrl(AddressbookRunner.WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(AddressbookRunner.WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);

		/* set browser. */
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME, 1, new BrowserOptions()));

		/* input data. */
		builder.crawlRules().setInputSpec(AddressbookCrawlingRules.AddressBookInputSpecification());

		/* CrawlOverview. */
		builder.addPlugin(new CrawlOverview());
		builder.addPlugin(new AddressbookCleanup());
	}

	/**
	 * List of inputs to crawl the AddressBook application.
	 */
	static InputSpecification AddressBookInputSpecification() {

		InputSpecification inputAddressBook = new InputSpecification();

		AddressbookForms.login(inputAddressBook);
		AddressbookForms.newEntry(inputAddressBook);
		AddressbookForms.newUser(inputAddressBook);
		
		FormInput search = new FormInput(InputType.TEXT, new Identification(How.name, "searchstring" ));
		search.inputValues("andrea");
		inputAddressBook.inputField(search);
		
//		FormInput selectBox = new FormInput(InputType.SELECT, new Identification(How.name, "group" ));
//		selectBox.inputValues("all");
//		inputAddressBook.inputField(selectBox);

		return inputAddressBook;
	}

}
