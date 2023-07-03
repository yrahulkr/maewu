package com.saltlab.webmutator.operators.dom;

import java.awt.Point;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.GsonUtils;
import com.saltlab.webmutator.utils.StyleMutationUtils;
import com.saltlab.webmutator.utils.VipsUtils;

public class StyleSize extends DomOperator{
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
//			Gson gson = new Gson();
			Point point = StyleMutationUtils.getRandomCoordinates();
			record.setData(new MutationData("RandomSize", point));
			System.out.println(point);
		}
		
		try {
			
//			Gson gson = new Gson();
//			MutationData insertAttr = gson.fromJson(record.getData(), MutationData.class);
			MutationData insertAttr = (MutationData) record.getData();

			Point point = (Point)insertAttr.value;
			
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
						(keyValue[0].equalsIgnoreCase("width") || 
								keyValue[0].equalsIgnoreCase("height") ||
								keyValue[0].equalsIgnoreCase("display") )) {
					//ignore
				}
				else {
					newStyleString += ";" + style;
				}
			}
		
			newStyleString += " ; display: block; width:"+ point.getX() + "px; height:" + point.getY() + "px";
			
			
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

		Point point = null;
		if(insertAttr.value instanceof LinkedTreeMap) {
			point = GsonUtils.deserializePoint((LinkedTreeMap) insertAttr.value);
		}
		else {
			point = (Point)insertAttr.value;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("arguments[0].style.display = 'block';");
		sb.append("arguments[0].style.width = '" + point.getX() + "px';");
		sb.append("arguments[0].style.height = '" + point.getY() + "px';");

		((JavascriptExecutor)driver).executeScript(sb.toString(), toMutate);
		return toMutate;
	}
}
