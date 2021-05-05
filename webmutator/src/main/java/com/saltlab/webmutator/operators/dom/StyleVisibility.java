package com.saltlab.webmutator.operators.dom;

import java.awt.Point;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.google.gson.Gson;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.VipsUtils;

public class StyleVisibility extends DomOperator{

	
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		if(!toMutate.hasAttributes()) {
			return null;
		}
		
		if(record.getData() == null) {
//			Gson gson = new Gson();
			boolean isDisplayed = record.getOrigNodeProperties().isDisplayed();
;
			String recordData = isDisplayed?"none":"block";
			record.setData(new MutationData("VisibilityToggle", recordData));
		}
		
		try {
			
//			Gson gson = new Gson();
//			MutationData insertAttr = gson.fromJson(record.getData(), MutationData.class);
			MutationData insertAttr = (MutationData) record.getData();

			if(toMutate.getAttributes().getNamedItem("style") == null) {
				Attr styleNode = toMutate.getOwnerDocument().createAttribute("style");
				toMutate.getAttributes().setNamedItem(styleNode);
			}
			
			Node styleAttribute = toMutate.getAttributes().getNamedItem("style");
			
			String styleString = styleAttribute.getNodeValue();
			String newStyleString = "";
			String[] styles = styleString.split(";");
			for(String style: styles) {
				String[] keyValue = style.split(":");
				if(keyValue.length == 2 && 
						keyValue[0].equalsIgnoreCase("display")) {
					//ignore
				}
				else {
					newStyleString += ";" + style;
				}
			}
		
			newStyleString += " ; display:"+ insertAttr.getValue();
			
			
			styleAttribute.setNodeValue(newStyleString);

			return toMutate;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
//		Gson gson = new Gson();
//		MutationData insertAttr = gson.fromJson(record.getData(), MutationData.class);
		MutationData insertAttr = (MutationData) record.getData();

		StringBuilder sb = new StringBuilder();
		sb.append("arguments[0].style.display = '"+ insertAttr.getValue() +"'");

		((JavascriptExecutor)driver).executeScript(sb.toString(), toMutate);
		return toMutate;
	}
}
