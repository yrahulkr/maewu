package com.saltlab.webmutator.operators.dom;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.google.gson.Gson;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.AttributeUtils;
import com.saltlab.webmutator.utils.StringMutator;

public class AttributeAdd extends DomOperator{
	Map<String, Set<String>> availableTokens = null;
	
	@Override
	public boolean isApplicable(Node node) {
		List<String> availableAttr = AttributeUtils.getAttributeToAdd(node);
		return availableAttr!=null && !availableAttr.isEmpty();
	}
	
	public AttributeAdd(Map<String, Set<String>> availableAttributes) {
		this.availableTokens = availableAttributes;
		AttributeUtils.addStandardTokens(availableTokens);
	}
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		if(record.getData() == null) {
			List<String> available = AttributeUtils.getAttributeToAdd(toMutate);
			/*for(String attr: AttributeMutator.getGlobalAttributes()) {
				if(!available.contains(attr)) {
					attrToInsert = attr;
					break;
				}
			}*/
			if(available!=null && available.size()>0) {
				String attrToInsert = available.get(0);
				
				Set<String> stringTokens = null;

				if(availableTokens!=null && !availableTokens.isEmpty()) {
					if(attrToInsert.equalsIgnoreCase("id") || attrToInsert.equalsIgnoreCase("class") || attrToInsert.equalsIgnoreCase("name")) {
						stringTokens = new HashSet<>();
						if(availableTokens.containsKey("id"))
							stringTokens.addAll(availableTokens.get("id"));
						if(availableTokens.containsKey("class"))
							stringTokens.addAll(availableTokens.get("class"));
						if(availableTokens.containsKey("name"))
							stringTokens.addAll(availableTokens.get("name"));
					}
					else {
						stringTokens = availableTokens.get(attrToInsert);
					}
				}
				if(stringTokens!=null) {
					String mutant = StringMutator.getRandomString(stringTokens);
					record.setData(new MutationData(attrToInsert, mutant));
				}
				else {
					String mutant = StringMutator.getMutant(attrToInsert);
					record.setData(new MutationData(attrToInsert, mutant));
				}
			}
			else {
				return null;
			}
			
		}
		try {
			Gson gson = new Gson();
//			MutationData insertAttr = (record.getData(), MutationData.class);
			MutationData insertAttr = (MutationData) record.getData();
			Attr toInsert = toMutate.getOwnerDocument().createAttribute(insertAttr.getKey());
			toInsert.setNodeValue((String)insertAttr.getValue());
			toMutate.getAttributes().setNamedItem(toInsert);
			return toMutate;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		String script = "try{arguments[0].attributes.setNamedItem(attr = document.createAttribute(arguments[1])); attr.value= arguments[2]; arguments[0].attributes.setNamedItem(attr); return true;}catch(ex){return false;}";
		try {
			Gson gson = new Gson();
//			MutationData insertAttr = gson.fromJson(record.getData(), MutationData.class);
			MutationData insertAttr = (MutationData) record.getData();

			boolean executed = (Boolean)((JavascriptExecutor)driver).executeScript(script, toMutate, insertAttr.key, insertAttr.value);
			if(executed)
				return toMutate;
			return null;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
