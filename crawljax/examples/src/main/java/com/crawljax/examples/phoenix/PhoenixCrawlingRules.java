package com.crawljax.examples.phoenix;

import java.util.concurrent.TimeUnit;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawlRules.FormFillMode;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Identification;
import com.crawljax.forms.FormInput;
import com.crawljax.plugins.crawloverview.CrawlOverview;

public class PhoenixCrawlingRules {
	
	static boolean AVOID_UNRELATED_BACKTRACKING = true;

	/**
	 * List of crawling rules for the Angular Phonecat application.
	 */
	public static void setCrawlingRules(CrawljaxConfigurationBuilder builder) {

//		/* crawling rules. */
		builder.crawlRules().clickElementsInRandomOrder(false);
//		builder.crawlRules().clickDefaultElements();
		builder.crawlRules().crawlHiddenAnchors(false);
		builder.crawlRules().crawlFrames(false);
		builder.crawlRules().clickOnce(false);
//
		builder.crawlRules().setFormFillMode(FormFillMode.RANDOM);
		
		builder.crawlRules().avoidUnrelatedBacktracking(AVOID_UNRELATED_BACKTRACKING);
		builder.crawlRules().skipExploredActions(false, 2);
		builder.crawlRules().setRestoreConnectedStates(true);
//		builder.crawlRules().avoidUnrelatedBacktracking(true);
		
//		builder.crawlRules().click("div").withAttribute("class", "board");
//		builder.crawlRules().click("div").withAttribute("class", "card");
//		builder.crawlRules().click("div").withAttribute("class", "add-new");
//
//		builder.crawlRules().click("div").withAttribute("class", "card-content");
//		

		
		/* do not click these. */

		/* set timeouts. */
		// builder.setUnlimitedRuntime();
		builder.setMaximumRunTime(1, TimeUnit.MINUTES);
		builder.setUnlimitedCrawlDepth();
		builder.setUnlimitedStates();
		builder.crawlRules().waitAfterReloadUrl(PhoenixRunner.WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(PhoenixRunner.WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);

		/* set browser. */
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME, 1));

		/* input data. */
//		builder.crawlRules().setInputSpec(PhoenixCrawlingRules.phoenixSpecification());
		danteConfig(builder, PhoenixCrawlingRules.phoenixSpecification());


		/* CrawlOverview. */
		builder.addPlugin(new CrawlOverview());
	}

	/**
	 * List of inputs to crawl the Phonecat application.
	 */
	static InputSpecification phoenixSpecification() {

		InputSpecification inputPhoenix = new InputSpecification();

//		PageKitForms.register(inputDimeshift);
		
		PhoenixForms.login(inputPhoenix);

		return inputPhoenix;
	}
	
	static void danteConfig(CrawljaxConfigurationBuilder builder, InputSpecification inputSpecification) {

      builder.crawlRules().click("a", "button");

      // board
      builder.crawlRules().click("div")
              .withAttribute("class", "board add-new");
      // list
      builder.crawlRules().click("div")
              .withAttribute("class", "list add-new");
      // created boards and lists
      builder.crawlRules().click("div")
              .withAttribute("class", "list");
      // created card
      builder.crawlRules().click("div")
              .withAttribute("class", "card-content");
      // delete card
      builder.crawlRules().click("i")
              .withAttribute("class", "fa fa-trash-o");

      // trello website
      builder.crawlRules().dontClick("a")
              .withAttribute("href", "https://trello.com");
      // diacode website
      builder.crawlRules().dontClick("a")
              .withAttribute("href", "https://diacode.com");
      // twitter handle
      builder.crawlRules().dontClick("a")
              .withAttribute("href", "https://twitter.com/bigardone");

      builder.crawlRules().dontClick("a").withAttribute("id", "crawler-sign-out");
      
//      builder.crawlRules().dontClick("a")
//              .withAttribute("href","/sign_up");

//      InputSpecification inputSpecification = new InputSpecification();

//      Form signInForm = new Form();
//      FormInput usernameInput = signInForm.inputField(FormInput.InputType.EMAIL,
//              new Identification(Identification.How.xpath,
//                      "/html[1]/body[1]/main[1]/div[1]/div[1]/main[1]/form[1]/div[1]/input[1]".toUpperCase()));
//      usernameInput.inputValues("john@phoenix-trello.com");
//      FormInput passwordInput = signInForm.inputField(FormInput.InputType.PASSWORD,
//              new Identification(Identification.How.xpath,
//                      "/html[1]/body[1]/main[1]/div[1]/div[1]/main[1]/form[1]/div[2]/input[1]".toUpperCase()));
//      passwordInput.inputValues("12345678");
//      inputSpecification.setValuesInForm(signInForm)
//              .beforeClickElement("button").withText("Sign in");

      Form signUpForm = new Form();
      FormInput firstNameInput = signUpForm.inputField(FormInput.InputType.TEXT,
              new Identification(Identification.How.name, "crawljax_user_first_name"));
      firstNameInput.inputValues("foo");
      FormInput lastNameInput = signUpForm.inputField(FormInput.InputType.TEXT,
              new Identification(Identification.How.name, "crawljax_user_last_name"));
      lastNameInput.inputValues("bar");
      FormInput usernameInputSignUp = signUpForm.inputField(FormInput.InputType.EMAIL,
              new Identification(Identification.How.name, "crawljax_user_email"));
      usernameInputSignUp.inputValues("foo@bar.com");
      FormInput passwordInputSignUp = signUpForm.inputField(FormInput.InputType.PASSWORD,
              new Identification(Identification.How.name, "crawljax_user_password"));
      passwordInputSignUp.inputValues("foobar123");
      FormInput passwordInputConfirmationSignUp = signUpForm.inputField(FormInput.InputType.PASSWORD,
              new Identification(Identification.How.name, "crawljax_user_password_confirmation"));
      passwordInputConfirmationSignUp.inputValues("foobar123");
      inputSpecification.setValuesInForm(signUpForm)
              .beforeClickElement("button").withText("Sign up");

//      Form boardForm = new Form();
//      FormInput boardNameInput = boardForm.inputField(FormInput.InputType.TEXT,
//              new Identification(Identification.How.id, "board_name"));
//      boardNameInput.inputValues("board","new board","just a board","board name","delivering the goods");
//      inputSpecification.setValuesInForm(boardForm)
//              .beforeClickElement("button").withText("Create board");

      Form addNewMembersForm = new Form();
      FormInput memberEmailInput = addNewMembersForm.inputField(FormInput.InputType.EMAIL,
              new Identification(Identification.How.id,
                      "crawljax_member_email"));
      memberEmailInput.inputValues("john@phoenix-trello.com","foo@bar.com");
      inputSpecification.setValuesInForm(addNewMembersForm)
              .beforeClickElement("button").withText("Add member");

      Form listForm = new Form();
      FormInput listNameInput = listForm.inputField(FormInput.InputType.TEXT,
              new Identification(Identification.How.id, "list_name"));
      listNameInput.inputValues("list","new list","just a list","list name","standard tuning");
      inputSpecification.setValuesInForm(listForm)
              .beforeClickElement("button").withText("Save list");

      Form updateListForm = new Form();
      FormInput updateListNameInput = updateListForm.inputField(FormInput.InputType.TEXT,
              new Identification(Identification.How.id, "list_name"));
      updateListNameInput.inputValues("updated list","updated new list","just updated a list","updated list name","phantom of the opera");
      inputSpecification.setValuesInForm(updateListForm)
              .beforeClickElement("button").withText("Update list");

      Form addNewCardForm = new Form();
      FormInput newCardInput = addNewCardForm.inputField(FormInput.InputType.TEXTAREA,
              new Identification(Identification.How.id, "card_name"));
      newCardInput.inputValues("card", "new card", "just a card", "card name", "surfing with the alien");
      inputSpecification.setValuesInForm(addNewCardForm)
              .beforeClickElement("button").withText("Add");

//      Form editCardForm = new Form();
//      FormInput titleInput = editCardForm.inputField(FormInput.InputType.TEXT,
//              new Identification(Identification.How.xpath,
//                      "/html[1]/body[1]/main[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/header[1]/form[1]/input[1]".toUpperCase()));
//      titleInput.inputValues("updated card", "updated new card", "just updated a card", "updated card name", "if I could fly");
//      FormInput descriptionInput = editCardForm.inputField(FormInput.InputType.TEXTAREA,
//              new Identification(Identification.How.xpath,
//                      "/html[1]/body[1]/main[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/header[1]/form[1]/textarea[1]".toUpperCase()));
//      descriptionInput.inputValues("description", "new description", "just a description", "long description", "crowd chant");
//      inputSpecification.setValuesInForm(editCardForm)
//              .beforeClickElement("button").withText("Save card");

//      Form cardCommentForm = new Form();
//      FormInput commentInput = cardCommentForm.inputField(FormInput.InputType.TEXTAREA,
//              new Identification(Identification.How.xpath,
//                      "/html[1]/body[1]/main[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/form[1]/div[2]/textarea[1]".toUpperCase()));
//      commentInput.inputValues("comment", "new comment", "just a comment", "long comment", "what happens next");
//      inputSpecification.setValuesInForm(cardCommentForm)
//              .beforeClickElement("button").withText("Save comment");

      builder.crawlRules().setInputSpec(inputSpecification);

	}

}
