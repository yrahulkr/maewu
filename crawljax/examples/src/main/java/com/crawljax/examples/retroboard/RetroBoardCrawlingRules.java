package com.crawljax.examples.retroboard;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.CrawlRules.FormFillMode;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.state.Identification;
import com.crawljax.forms.FormInput;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.plugins.crawloverview.CrawlOverview;

public class RetroBoardCrawlingRules {

    private static final String USER_1 = "admin";
    private static final String USER_2 = "user";

    public static final List<String> users = Arrays.asList(USER_1,USER_2);
    public static final String usersXPath = "/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]".toUpperCase();

	/**
	 * List of crawling rules for the Angular Phonecat application.
	 */
	public static void setCrawlingRules(CrawljaxConfigurationBuilder builder) {

		

		builder.crawlRules().clickElementsInRandomOrder(false);
		builder.crawlRules().clickDefaultElements();
		builder.crawlRules().crawlHiddenAnchors(true);
		builder.crawlRules().crawlFrames(false);
		builder.crawlRules().clickOnce(false);

		builder.crawlRules().setFormFillMode(FormFillMode.RANDOM);

		/* crawling rules. */

        builder.crawlRules().click("a", "button");

        // navigation bar links
        builder.crawlRules().click("label").withText("Create");
        builder.crawlRules().click("label").withText("Previous");
        builder.crawlRules().click("label").withText("Advanced");
        
        builder.crawlRules().enter("input").underXPath("//div".toUpperCase() + 
        		"[@id=\"content\"]" +"/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]".toUpperCase());
		builder.crawlRules().enter("input").underXPath("//div".toUpperCase() +
				"[@id=\"content\"]" +"/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]".toUpperCase());
		builder.crawlRules().enter("input").underXPath("//div".toUpperCase() +
				"[@id=\"content\"]" +"/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]".toUpperCase());

        // language select
        builder.crawlRules().click("div")
                .withAttribute("class", "theme__templateValue___3if5o theme__value___mflIw");
        // retrospective board title
        builder.crawlRules().click("span")
                .withAttribute("class", "SessionName__name___3g4PI");
        // summary toggle in menu
        builder.crawlRules().click("span")
                .withAttribute("class", "theme__off___Ih3qa");
        // previous retrospective
        builder.crawlRules().click("span")
                .withAttribute("class","theme__item___QgVrb theme__selectable___pSlvM");

        // github link
        builder.crawlRules().dontClick("a")
                .withAttribute("href", "https://github.com/antoinejaussoin/retro-board");
        
        
//        builder.crawlRules().dontClick("a")
//                .underXPath("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[1]/HEADER[1]/DIV[1]/A[1]");
        // invite button opens modal
//        builder.crawlRules().dontClick("button")
//                .withAttribute("title", "Invite");
        // menu button opens modal
//        builder.crawlRules().dontClick("button")
//                .withAttribute("id", "crawljax-menu");

		/* do not click these. */

		/* set timeouts. */
		// builder.setUnlimitedRuntime();
		builder.setMaximumRunTime(1, TimeUnit.MINUTES);
		builder.setUnlimitedCrawlDepth();
		builder.setUnlimitedStates();
		builder.crawlRules().waitAfterReloadUrl(RetroBoardRunner.WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(RetroBoardRunner.WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);

		/* set browser. */
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME, 1));

		/* input data. */
//		builder.crawlRules().setInputSpec(RetroBoardCrawlingRules.retroBoardSpecification());
//		builder.crawlRules().setInputSpec(builder);
//        builder.crawlRules().setInputFieldIdsWithEnterClick(inputFieldIdsWithEnterClick);
		setInputSpec(builder);
		/* CrawlOverview. */
		builder.addPlugin(new CrawlOverview());
	}

	/**
	 * List of inputs to crawl the Phonecat application.
	 */
	static InputSpecification retroBoardSpecification() {

		InputSpecification inputPhoenix = new InputSpecification();

//		PageKitForms.register(inputDimeshift);
		
		RetroBoardForms.login(inputPhoenix);

		return inputPhoenix;
	}
	
	static void setInputSpec(CrawljaxConfigurationBuilder builder) {
		  InputSpecification inputSpecification = new InputSpecification();

	        Form login = new Form();
	        FormInput inputLogin = login.inputField(FormInput.InputType.INPUT, new Identification(Identification.How.xpath, usersXPath));
	        inputLogin.inputValues(USER_1, USER_2);
	        inputSpecification.setValuesInForm(login).beforeClickElement("button").withText("\"Let's start\"");

//	        Form retro = new Form();

	        Identification wellIdentification = new Identification(Identification.How.xpath,
	        		"//div".toUpperCase() + "[@id=\"content\"]" +"/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]".toUpperCase());
	        inputSpecification.inputField(FormInput.InputType.INPUT, wellIdentification).inputValues("Work","All the rest");
//	        FormInput well = retro.inputField(FormInput.InputType.INPUT, wellIdentification);
//	        well.inputValues("Work","All the rest");
	        Form well = new Form();
	        
	        FormInput wellInput = well.inputField(FormInput.InputType.INPUT, wellIdentification);
	        wellInput.inputValues("Social life", "Time off");
	        inputSpecification.setValuesInForm(well).beforeEnterElement("input").underXPath("//div[@id='content']/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]".toUpperCase());
	        
	        
	        
	        
	        Identification improvedIdentification = new Identification(Identification.How.xpath,
	        		"//div".toUpperCase() + "[@id='content']" +"/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]".toUpperCase());
//	        inputSpecification.inputField(FormInput.InputType.INPUT, improvedIdentification).inputValues("Social life", "Time off");
	        
	        Form improved = new Form();
	        
	        FormInput improvedInput = improved.inputField(FormInput.InputType.INPUT, improvedIdentification);
	        improvedInput.inputValues("Social life", "Time off");
	        inputSpecification.setValuesInForm(improved).beforeEnterElement("input").underXPath("//div[@id=\"content\"]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]".toUpperCase());
	        

	        Identification ideaIdentification = new Identification(Identification.How.xpath,
	                "//div".toUpperCase() + "[@id=\"content\"]" +"/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]".toUpperCase());
//	        inputSpecification.inputField(FormInput.InputType.INPUT, ideaIdentification).inputValues("Brilliant idea", "Work remotely");
//	        FormInput idea = retro.inputField(FormInput.InputType.INPUT, ideaIdentification);
//	        idea.inputValues("Brilliant idea", "Work remotely");
	        Form idea = new Form();
	        
	        FormInput ideaInput = improved.inputField(FormInput.InputType.INPUT, ideaIdentification);
	        ideaInput.inputValues("Brilliant idea", "Work remotely");
	        inputSpecification.setValuesInForm(idea).beforeEnterElement("input").underXPath("//div[@id=\\\"content\\\"]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]".toUpperCase());
	        
	        
	        
//	        // fake element (retro grid) to make the form work
//	        inputSpecification.setValuesInForm(retro).beforeClickElement("div")
//	                .withAttribute("class", "PostBoard__board___1QU4l grid");

//	        return inputSpecification;
	        
	        builder.crawlRules().setInputSpec(inputSpecification);
//
//	        Set<Identification> inputFieldIdsWithEnterClick = new HashSet<>();
//	        inputFieldIdsWithEnterClick.add(wellIdentification);
//	        inputFieldIdsWithEnterClick.add(improvedIdentification);
//	        inputFieldIdsWithEnterClick.add(ideaIdentification);
//
//	        builder.crawlRules().setInputFieldIdsWithEnterClick(inputFieldIdsWithEnterClick);

	}

}
