package com.crawljax.examples.ppma;

import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.Identification.How;
import com.crawljax.forms.FormInput;
import com.crawljax.forms.FormInput.InputType;

public class PpmaForms {

	static void login(InputSpecification inputAddressBook) {
		Form loginForm = new Form();

		FormInput username = loginForm.inputField(InputType.TEXT, new Identification(How.id, "LoginForm_username"));
		username.inputValues("admin");

		FormInput password = loginForm.inputField(InputType.TEXT, new Identification(How.id, "LoginForm_password"));
		password.inputValues("admin");

		inputAddressBook.setValuesInForm(loginForm).beforeClickElement("a").underXPath("//*[@id=\"login-form\"]/div[1]/div[2]/a[1]");
	}

	static void newEntry(InputSpecification inputPPMA) {
		Form entry = new Form();

		FormInput name = entry.inputField(InputType.TEXT, new Identification(How.id, "Entry_name"));
		name.inputValues("Random Website");
		
		FormInput username = entry.inputField(InputType.TEXT, new Identification(How.id, "Entry_username"));
		username.inputValues("jdoe");


		FormInput password = entry.inputField(InputType.PASSWORD, new Identification(How.id, "Entry_password"));
		password.inputValues("123456");

		FormInput address = entry.inputField(InputType.TEXTAREA, new Identification(How.name, "Entry_url"));
		address.inputValues("http://example.com");

		FormInput email = entry.inputField(InputType.TEXT, new Identification(How.name, "Entry_tagList"));
		email.inputValues("mytag1", "mytag2");

		FormInput comment = entry.inputField(InputType.SELECT, new Identification(How.name, "Entry_comment"));
		comment.inputValues("Random Comment 1");

		inputPPMA.setValuesInForm(entry).beforeClickElement("input").underXPath("/html[1]/body[1]/div[1]/div[1]/div[1]/form[1]/input[7]".toUpperCase());
	}

}
