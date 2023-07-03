package com.crawljax.examples.retroboard;

import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.Identification.How;
import com.crawljax.forms.FormInput;
import com.crawljax.forms.FormInput.InputType;

public class RetroBoardForms {
	static void login(InputSpecification inputAddressBook) {
		Form loginForm = new Form();

		FormInput username = loginForm.inputField(InputType.INPUT, new Identification(How.xpath, "//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"));
		username.inputValues("johndoe");

//		FormInput password = loginForm.inputField(InputType.PASSWORD, new Identification(How.id, "user_password"));
//		password.inputValues("12345678");

		inputAddressBook.setValuesInForm(loginForm).beforeClickElement("button").underXPath("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
	}
//	
//	
//	static void register(InputSpecification inputAddressBook) {
//		Form registerForm = new Form();
//
//		FormInput username = registerForm.inputField(InputType.TEXT, new Identification(How.id, "input_login"));
//		username.inputValues("example");
//
//		FormInput email = registerForm.inputField(InputType.TEXT, new Identification(How.id, "input_email"));
//		email.inputValues("example@example.com");
//		
//		FormInput password = registerForm.inputField(InputType.PASSWORD, new Identification(How.id, "input_password"));
//		password.inputValues("example123");
//
//		inputAddressBook.setValuesInForm(registerForm).beforeClickElement("input").withAttribute("id", "registration_modal_form_submit");
//	}
}
