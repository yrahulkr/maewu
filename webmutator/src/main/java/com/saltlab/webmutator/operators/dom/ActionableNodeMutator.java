package com.saltlab.webmutator.operators.dom;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.StringMutator;

public class ActionableNodeMutator extends DomOperator{
	
	/*
	 *	Input Elements and Types 
		1 Hidden state (type=hidden)
		2 Text (type=text) state and Search state (type=search)
		3 Telephone state (type=tel)
		4 URL state (type=url)
		5 E-mail state (type=email)
		6 Password state (type=password)
		7 Date state (type=date)
		8 Month state (type=month)
		9 Week state (type=week)
		10 Time state (type=time)
		11 Local Date and Time state (type=datetime-local)
		12 Number state (type=number)
		13 Range state (type=range)
		14 Color state (type=color)
		15 Checkbox state (type=checkbox)
		16 Radio Button state (type=radio)
		17 File Upload state (type=file)
		18 Submit Button state (type=submit)
		19 Image Button state (type=image)
		20 Reset Button state (type=reset)
		21 Button state (type=button)
	 */
	String[] inputTypes = {"text", "hidden", "radio", "file", "submit", "image", "button", "number", "range", "color", "search"};
	
	/*
	 * Common input element attributes
		1 The maxlength and minlength attributes
		2 The size attribute
		3 The readonly attribute
		4 The required attribute
		5 The multiple attribute
		6 The pattern attribute
		7 The min and max attributes
		8 The step attribute
		9 The list attribute
		10 The placeholder attribute
	 */
	String[] inputAttributes = {"size", "list", "maxlength", "minlength", "readonly", "required", "multiple", "pattern", "step", "placeholder"};
	
	public ActionableNodeMutator() {
		tagList.clear();
		tagList.add("a");
		tagList.add("input");
		tagList.add("button");
	}

	public List<String> getTagList() {
		return tagList;
	}
	
	
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
//		if(toMutate.getChildNodes().getLength()>0) {
//			System.out.println("Not a leaf node");
//			return false;
//		}
		String tag = toMutate.getNodeName();
		switch(tag) {
		case "a":
			if(toMutate.hasAttributes()) {
				Node href = toMutate.getAttributes().getNamedItem("href");
				if(href!=null) {
					String mutant = StringMutator.getMutant(href.getNodeValue());
					href.setNodeValue(mutant);
					return toMutate;
				}
			}
			// Anchor Node has text content and is non empy. Its href has not been mutated
			if(!toMutate.getTextContent().trim().isEmpty()) {
				String mutant = StringMutator.getMutant(toMutate.getTextContent().trim());
				System.out.println("Mutating text of anchor node : " + mutant);
				toMutate.setTextContent(mutant);
				return toMutate;
			}
		case "input":
			AttributeMutator.mutateGlobalAttribute(toMutate);
		}
		
		return null;
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

}
