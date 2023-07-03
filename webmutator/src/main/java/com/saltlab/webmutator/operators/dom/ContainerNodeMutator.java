package com.saltlab.webmutator.operators.dom;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.StringMutator;

public class ContainerNodeMutator extends DomOperator{
	public ContainerNodeMutator() {
		tagList.clear();
		tagList.add("body");
		tagList.add("div");
		tagList.add("span");
		tagList.add("table");
		tagList.add("td");
		tagList.add("tr");
		tagList.add("ul");
		tagList.add("li");
	}

	public List<String> getTagList() {
		return tagList;
	}
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		if(toMutate.getChildNodes().getLength()==0) {
			if(toMutate.getTextContent().trim().isEmpty()) {
				return toMutate;
			}
			else {
				String text = toMutate.getTextContent();
				String mutant = StringMutator.getMutant(text);
				toMutate.setTextContent(mutant);
				System.out.println("Applied text mutation : " + mutant + "on container node : " + toMutate);
				return toMutate;
			}
		}
		return toMutate;
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

}
