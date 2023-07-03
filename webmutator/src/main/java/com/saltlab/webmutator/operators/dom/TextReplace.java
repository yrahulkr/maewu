package com.saltlab.webmutator.operators.dom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.DomUtils;
import com.saltlab.webmutator.utils.StringMutator;
import com.saltlab.webmutator.utils.TextUtils;

public class TextReplace extends DomOperator {

	private Set<String> availableTokens = null;


	public TextReplace(Set<String> availableTokens) {
		this.availableTokens = availableTokens;
		tagList.clear();
		tagList.add("#text");
	}

	@Override
	public boolean isApplicable(Node node) {
		// Find if textmutator is applicable
		if(node==null || !DomUtils.isLeafNode(node) || TextUtils.excludeTags().contains(node.getNodeName().toLowerCase())) {
			return false;
		}

		if (!node.getTextContent().trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public List<String> getTagList() {
		return tagList;
	}

	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		if(record.getData() == null) {
			String currentText = toMutate.getTextContent().trim();
			String textToInsert = null;
			Set<String> available = null;
			if(availableTokens!=null && !availableTokens.isEmpty()) {
				available = availableTokens;
			}
			else{
				available = new HashSet<>();
				available.addAll(DomUtils.getTextTokens(toMutate.getOwnerDocument()));
			}
			available.remove(currentText);
			
			if(available.size()>0) {
				textToInsert = TextUtils.getRandomTextToken(available);
			}
			else {
				textToInsert = "dummy";
			}
			record.setData(new MutationData("TextInsert", textToInsert));
		}
		
		try {
//			Gson gson = new Gson();
//			MutationData insertAttr = gson.fromJson(record.getData(), MutationData.class);
			MutationData insertAttr = (MutationData) record.getData();

			toMutate.setTextContent((String)insertAttr.getValue());
			return toMutate;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
//		String text = toMutate.getText();
		try {
			String mutant = (String) ((MutationData)record.getData()).getValue();
			((JavascriptExecutor) driver).executeScript("arguments[0].textContent = arguments[1];", toMutate, mutant);
			return toMutate;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
