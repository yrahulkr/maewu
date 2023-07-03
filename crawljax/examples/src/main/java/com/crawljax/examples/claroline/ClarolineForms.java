package com.crawljax.examples.claroline;

import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.Identification.How;
import com.crawljax.forms.FormInput;
import com.crawljax.forms.FormInput.InputType;

public class ClarolineForms {

	static void login(InputSpecification inputClaroline) {
		Form loginForm = new Form();

		FormInput username = loginForm.inputField(InputType.TEXT, new Identification(How.name, "login"));
		username.inputValues("astocco");

		FormInput password = loginForm.inputField(InputType.PASSWORD, new Identification(How.name, "password"));
		password.inputValues("password");

		inputClaroline.setValuesInForm(loginForm).beforeClickElement("button").withAttribute("type", "submit");
	}

	static void newEntry(InputSpecification inputClaroline) {
		Form owner = new Form();

		FormInput username = owner.inputField(InputType.TEXT, new Identification(How.name, "firstname"));
		username.inputValues("Andrea");

		FormInput password = owner.inputField(InputType.TEXT, new Identification(How.name, "lastname"));
		password.inputValues("Stocco");

		FormInput address = owner.inputField(InputType.TEXTAREA, new Identification(How.name, "address"));
		address.inputValues("1165 The Castings");

		FormInput email = owner.inputField(InputType.TEXT, new Identification(How.name, "email"));
		email.inputValues("astocco@ece.ubc.ca");

		FormInput bday = owner.inputField(InputType.SELECT, new Identification(How.name, "bday"));
		bday.inputValues("18");

		FormInput bmonth = owner.inputField(InputType.SELECT, new Identification(How.name, "bmonth"));
		bmonth.inputValues("August");

		FormInput byear = owner.inputField(InputType.TEXT, new Identification(How.name, "byear"));
		byear.inputValues("August");

		inputClaroline.setValuesInForm(owner).beforeClickElement("input").withAttribute("value", "Enter");
	}

	public static void newUser(InputSpecification inputClaroline) {
		Form newUserForm = new Form();

		FormInput username = newUserForm.inputField(InputType.TEXT, new Identification(How.name, "email"));
		username.inputValues("tsigalko18@gmail.com");

		FormInput password = newUserForm.inputField(InputType.TEXT, new Identification(How.name, "password"));
		password.inputValues("123456");

		inputClaroline.setValuesInForm(newUserForm).beforeClickElement("input").withAttribute("value", "Sign-Up");
	}
	
	public static void newCourse(InputSpecification inputClaroline) {
		Form newCourseForm = new Form();
		FormInput courseEmail = newCourseForm.inputField(InputType.TEXT, new Identification(How.id, "course_email"));
		courseEmail.inputValues("jdoe@mydomain.net");
		inputClaroline.setValuesInForm(newCourseForm).beforeClickElement("input").withAttribute("value", "Ok");
	}

}
