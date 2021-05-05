package com.saltlab.webmutator.operators.dom;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.DomUtils;
import com.saltlab.webmutator.utils.StringMutator;
import com.saltlab.webmutator.utils.TextUtils;

public class TextDelete extends DomOperator{

	public TextDelete() {
		tagList.clear();
		tagList.add("#text");
//		tagList.add("p");
//		tagList.add("h1");
//		tagList.add("h2");
//		tagList.add("h3");
//		tagList.add("h4");
//		tagList.add("h5");
//		tagList.add("h6");

		/*
		<b> - Bold text
		<strong> - Important text
		<i> - Italic text
		<em> - Emphasized text
		<mark> - Marked text
		<small> - Small text
		<del> - Deleted text
		<ins> - Inserted text
		<sub> - Subscript text
		<sup> - Superscript text
		 */
	}
	
	@Override 
	public boolean isApplicable(Node node) {
		// Find if textmutator is applicable
		if(node==null || !DomUtils.isLeafNode(node) || TextUtils.excludeTags().contains(node.getNodeName().toLowerCase())) {
			return false;
		}

			if(!node.getTextContent().trim().isEmpty()) {
				return true;
			}
			return false;
	}
	
	public List<String> getTagList() {
		return tagList;
	}
	
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		if(toMutate.getTextContent().trim().isEmpty()) {
			return null;
		}
		toMutate.setTextContent("");
		return toMutate;
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		String text = toMutate.getText();
		String mutant = "";
		((JavascriptExecutor)driver).executeScript("arguments[0].textContent = arguments[1];", toMutate, mutant);
		return toMutate;
	}

}
