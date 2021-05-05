package com.saltlab.webmutator.operators.dom;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.saltlab.webmutator.MutationRecord;

public class TagMutator extends DomOperator {
	
	HashMap<String, String> switchMap = new HashMap<>();
	/**
	 * Mutate dom tags to equivalent tags
	 * 
	 * Headers
	 * h2 -> h3
	 * h1 -> h2
	 * h3 -> h2
	 * h4 -> h3
	 * h5 -> h4
	 * h6 -> h5
	 * 
	 * internal text
	 * p -> div
	 * span -> div
	 * b -> i
	 * i -> b
	 * strong -> b
	 * em -> i
	 * 
	 */
	
	public TagMutator() {
		tagList.clear();
//		tagList.add("div");
		tagList.add("span");
		tagList.add("p");
		tagList.add("h1");
		tagList.add("h2");
		tagList.add("h3");
		tagList.add("h4");
		tagList.add("h5");
		tagList.add("h6");

		switchMap.put("h2", "h3");
		switchMap.put("h1", "h2");
		switchMap.put("h3", "h2");
		switchMap.put("h4", "h3");
		switchMap.put("h5", "h4");
		switchMap.put("h6", "h5");
		switchMap.put("p", "div");
		switchMap.put("span", "div");
		switchMap.put("b", "i");
		switchMap.put("i", "b");
		switchMap.put("strong", "b");
		switchMap.put("em", "i");
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
	
	public List<String> getTagList() {
		return tagList;
	}
	
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		String currentTag = toMutate.getNodeName().toLowerCase().trim();
		
		if(switchMap.keySet().contains(currentTag)) {
			String mutant = switchMap.get(currentTag);
			Node newNode = toMutate.getOwnerDocument().createElement(mutant);
			Node parent = toMutate.getParentNode();
			Node replaced = parent.replaceChild(newNode, toMutate);
			NamedNodeMap attr = replaced.getAttributes();
			if(attr!=null) {
				for(int i = 0; i< attr.getLength(); i++) {
					String attr1Name = attr.item(i).getNodeName();
					Node attr1 = replaced.getAttributes().removeNamedItem(attr1Name);
					newNode.getAttributes().setNamedItem(attr1);
				}	
			}
			NodeList children = replaced.getChildNodes();
			while(children!=null) {
				if(children.getLength()>0) {
					newNode.appendChild(children.item(0));
				}
				else {
					break;
				}
			}
			
			
			return newNode;
		}
		else {
			return null;
		}
		
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		String script = 
				"var d = document.createElement(arguments[1]);"  
				+ "var i = 0; while(i<arguments[0].attributes.length) {d.setAttribute(arguments[0].attributes.item(i).name, arguments[0].attributes.item(i).value); i = i+1;} \n"
				+ "d.innerHTML = arguments[0].innerHTML;"  
				+ "arguments[0].parentNode.replaceChild(d, arguments[0]);"
				+ "return d;";
		
		String newTag = switchMap.get(toMutate.getTagName().toLowerCase());
		
		return (WebElement) ((JavascriptExecutor)driver).executeScript(script, toMutate, newTag);
	}

}
