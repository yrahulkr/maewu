package com.saltlab.webmutator.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AttributeUtils {
	
	// Available attributes - id, class, name, href
	// For input - type, formaction, formmethod
	// For form - action, method
	// For button - type, formaction, formmethod
	
	public static List<String> getApplicableAttributes(String nodeName, String nodeType){
		List<String> availableList = new ArrayList<String>();
		switch(nodeName) {
		case "button":
		case "input":
			availableList.add("type");
			if(nodeType!=null && nodeType.equalsIgnoreCase("submit")) {
				availableList.add("formaction");
				availableList.add("formmethod");
			}
		case "form":
			availableList.add("action");
			availableList.add("method");
		case "a":
			availableList.add("href");
		default:
			availableList.add("id");
			availableList.add("class");
			availableList.add("name");
		}
		return availableList;
	}
	
	public static Set<String> getStandardAttributes(){
		Set<String>	attrSet = new HashSet<String>();

		if(attrSet.isEmpty()){ // Add common attributes
			attrSet.add("id");
			attrSet.add("class");
			attrSet.add("name");
			attrSet.add("href");
			attrSet.add("action");
			attrSet.add("formaction");
		}
		return attrSet;
	}
	
	public static List<String> getAttributeToReplace(Node toMutate) {
		if(toMutate==null || !toMutate.hasAttributes()) {
			return null;
		}
		
		String nodeName = toMutate.getNodeName().toLowerCase();
		String nodeType = null;
		try{nodeType = toMutate.getAttributes().getNamedItem("type").getNodeValue();}catch(Exception ex) {}

		List<String> applicableList = getApplicableAttributes(nodeName, nodeType);

		List<String> availableList = new ArrayList<String>();
		
		
		NamedNodeMap attributes = toMutate.getAttributes();
		for(int i =0; i< attributes.getLength(); i++) {
			String attribute = attributes.item(i).getNodeName().toLowerCase();
			if(!availableList.contains(attribute)) {
				availableList.add(attribute);
			}
		}
		
		List<String> returnList = new ArrayList<String>();
		returnList.addAll(applicableList);
		for(String applicable: applicableList) {
			if(!availableList.contains(applicable)) {
				returnList.add(applicable);
			}
		}
		
		return returnList;
	}
	
	public static List<String> getAttributeToAdd(Node toMutate) {
		if(toMutate==null || !toMutate.hasAttributes()) {
			return null;
		}
		
		String nodeName = toMutate.getNodeName().toLowerCase();
		String nodeType = null;
		try{nodeType = toMutate.getAttributes().getNamedItem("type").getNodeValue();}catch(Exception ex) {}

		List<String> applicableList = getApplicableAttributes(nodeName, nodeType);

		List<String> availableList = new ArrayList<String>();
		
		
		NamedNodeMap attributes = toMutate.getAttributes();
		for(int i =0; i< attributes.getLength(); i++) {
			String attribute = attributes.item(i).getNodeName().toLowerCase();
			if(!availableList.contains(attribute)) {
				availableList.add(attribute);
			}
		}
		
		List<String> returnList = new ArrayList<String>();
		returnList.addAll(applicableList);
		for(String applicable: applicableList) {
			if(availableList.contains(applicable)) {
				returnList.remove(applicable);
			}
		}
		
		return returnList;
	}

	public static void addStandardTokens(Map<String, Set<String>> availableTokens) {
		if(availableTokens == null) {
			return;
		}
		if(availableTokens.get("type") == null) {
			availableTokens.put("type", new HashSet<>());
		}
		availableTokens.get("type").add("submit");
		availableTokens.get("type").add("button");
		availableTokens.get("type").add("text");
		availableTokens.get("type").add("checkbox");
		availableTokens.get("type").add("radio");
		availableTokens.get("type").add("hidden");
		availableTokens.get("type").add("file");
		availableTokens.get("type").add("date");
		availableTokens.get("type").add("email");
		availableTokens.get("type").add("password");
		availableTokens.get("type").add("reset");
		availableTokens.get("type").add("number");
		

		if(availableTokens.get("method") == null) {
			availableTokens.put("method", new HashSet<>());
		}
		availableTokens.get("method").add("GET");
		availableTokens.get("method").add("POST");
		
		if(availableTokens.get("formmethod") == null) {
			availableTokens.put("formmethod", new HashSet<>());
		}
		availableTokens.get("formmethod").add("GET");
		availableTokens.get("formmethod").add("POST");
	}

	public static boolean isStandardInteractable(Node node) {
		if(node == null) {
			return false;
		}
		String nodeName = node.getNodeName().toLowerCase();
		
		switch(nodeName) {
		case "button":
		case "a":
		case "input":
			return true;
			default:
				return false;
		}
		
	}
}
