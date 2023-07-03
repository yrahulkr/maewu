package com.saltlab.webmutator.operators.dom;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.AttributeUtils;

public class AttributeDelete extends DomOperator{
	
	@Override
	public boolean isApplicable(Node node) {
		List<String> availableAttr = AttributeUtils.getAttributeToReplace(node);
		return availableAttr!=null && !availableAttr.isEmpty();
	}

	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		if(record.getData() == null) {
			List<String> toDelete = AttributeUtils.getAttributeToReplace(toMutate);
			if(toDelete.size()>0) {
				record.setData(toDelete.get(0));
			}
			else {
				
			}
		}
		try {
			toMutate.getAttributes().removeNamedItem((String)record.getData());
			return toMutate;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		String script ="try{arguments[0].attributes.removeNamedItem(arguments[1]); return true;}catch(ex){return false;}";
		boolean executed = (Boolean)((JavascriptExecutor)driver).executeScript(script, toMutate, record.getData());
		if(executed)
			return toMutate;
		
		return null;
	}

}
