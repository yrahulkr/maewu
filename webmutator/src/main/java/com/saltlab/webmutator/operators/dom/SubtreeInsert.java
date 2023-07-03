package com.saltlab.webmutator.operators.dom;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.saltlab.webmutator.MutationRecord;

public class SubtreeInsert extends DomOperator{
	
	public SubtreeInsert() {
		tagList.clear();
		tagList.add("div");
		tagList.add("span");
		
		tagList.add("p");
		
		tagList.add("table");
		tagList.add("tbody");
		tagList.add("tr");
		tagList.add("td");

		tagList.add("ul");
		tagList.add("li");
		
		tagList.add("select");
	}

	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		Node parent = toMutate.getParentNode();
		Node mutant = toMutate.cloneNode(true);
		parent.appendChild(mutant);
		Node attr = parent.getOwnerDocument().createAttribute("mutated");
		toMutate.getAttributes().setNamedItem(attr);
		return toMutate;
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		try {
			String script = "if(arguments[0].attributes.getNamedItem('mutated')==null) {" + 
					"	arguments[0].parentElement.appendChild(arguments[0].cloneNode(true));" + 
					"	arguments[0].attributes.setNamedItem(document.createAttribute('mutated'));}";
	//		String script = "arguments[0].parentNode.appendChild(arguments[0].cloneNode(true))";
			((JavascriptExecutor)driver).executeScript(script, toMutate);
			return toMutate;
		}catch(Exception ex) {
			return null;
		}
	}
}
