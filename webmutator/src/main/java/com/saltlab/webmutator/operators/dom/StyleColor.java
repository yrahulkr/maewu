package com.saltlab.webmutator.operators.dom;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.StringMutator;
import com.saltlab.webmutator.utils.StyleMutationUtils;
import com.saltlab.webmutator.utils.VipsUtils;

public class StyleColor extends DomOperator{
	
	@Override
	public boolean isApplicable(Node node) {
		return VipsUtils.isDisplayed(node) != null && VipsUtils.isDisplayed(node).equalsIgnoreCase("true");
	}
	
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		if(!toMutate.hasAttributes()) {
			return null;
		}
		
		if(record.getData() == null) {
			String colorString = "#" + StyleMutationUtils.getRandomHexString(6);
			record.setData(new MutationData("RandomColor", colorString));
			System.out.println(colorString);
		}
		
		try {
			
			Gson gson = new Gson();
//			MutationData insertAttr = gson.fromJson(record.getData(), MutationData.class);
			MutationData insertAttr = (MutationData) record.getData();

			boolean appliedColor = false;
			
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
				if(keyValue.length == 2 && (keyValue[0].equalsIgnoreCase("color") || keyValue[0].equalsIgnoreCase("colour"))) {
					newStyleString += " ; color:"+ insertAttr.value;
					appliedColor = true;
				}
				else {
					newStyleString += ";" + style;
				}
			}
			if(!appliedColor) {
				newStyleString += " ; color:"+ (String)insertAttr.value;
			}
			
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

		
		((JavascriptExecutor)driver).executeScript("arguments[0].style.color = arguments[1];", toMutate, insertAttr.getValue());
		return toMutate;
	}

}
