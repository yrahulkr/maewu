package com.saltlab.webmutator.operators.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.operators.Operator;

public abstract class DomOperator implements Operator{
	List<String> tagList = new ArrayList<String>();
	static String[] containerNodes = {"div", "span", "table", "td", "tr", "select", "ul", "li", "tbody", "p"};
	/**
	 * HTML tags to manipulate
	 * BODY
	 * DIV
	 * SPAN
	 * TABLE
	 * TD
	 * TR
	 * A
	 * INPUT
	 * BUTTON
	 * P
	 */
	
	public DomOperator() {
		tagList.addAll(getDefaultTags());
	}
	
	public List<String> getTagList() {
		return tagList;
	}

	public abstract Node applyOperator(Node toMutate, MutationRecord record);
	
	public abstract WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record);

	public static List<String> getDefaultTags() {
		List<String> defaultTags = new ArrayList<String>();
		defaultTags.add("body");
		defaultTags.add("div");
		defaultTags.add("span");
		defaultTags.add("table");
		defaultTags.add("td");
		defaultTags.add("tr");
		defaultTags.add("a");
		defaultTags.add("input");
		defaultTags.add("button");
		defaultTags.add("p");
		defaultTags.add("h1");
		defaultTags.add("h2");
		defaultTags.add("h3");
		defaultTags.add("h4");
		defaultTags.add("h5");
		defaultTags.add("h6");
		defaultTags.add("ul");
		defaultTags.add("li");
		defaultTags.add("img");
		return defaultTags;
	}

	public static List<String> getContainerNodes() {
		return Arrays.asList(containerNodes);
	}

	public boolean isApplicable(Node node) {
		if(tagList.contains(node.getNodeName().toLowerCase())) {
			return true;
		}
		return false;
	}

}
