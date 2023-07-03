package com.saltlab.webmutator.operators.dom;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.saltlab.webmutator.MutationRecord;

public class SubtreeDelete extends DomOperator{
	
	public SubtreeDelete() {
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
		// Delete all children of the node
		NodeList children = toMutate.getChildNodes();
		List<Node> toRemove = new ArrayList<Node>();
		for(int i=0; i<children.getLength(); i++) {
			if(children.item(i).getNodeName().equalsIgnoreCase("#text")) {
				continue;
			}
			toRemove.add(children.item(i));
		}
		
		if(toRemove.isEmpty()) {
			return null;
		}
		for(Node node: toRemove) {
			toMutate.removeChild(node);
		}
		
		Node attr = toMutate.getOwnerDocument().createAttribute("mutated");
		toMutate.getAttributes().setNamedItem(attr);

		return toMutate;
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		String script = "if(arguments[0].attributes.getNamedItem('mutated')==null) {" + 
						"while(arguments[0].firstElementChild){arguments[0].removeChild(arguments[0].lastElementChild);}"+
						"arguments[0].attributes.setNamedItem(document.createAttribute('mutated'));}";
		((JavascriptExecutor)driver).executeScript(script, toMutate);
		return toMutate;
	}

}
