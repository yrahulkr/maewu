package com.saltlab.webmutator.operators.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.StringMutator;

public class AttributeMutator extends DomOperator {
	/*
	 * The following attributes are common to and may be specified on all HTML elements (even those not defined in this specification):
		accesskey
		class
		contenteditable
		contextmenu
		dir
		draggable
		dropzone
		hidden
		id
		lang
		spellcheck
		style
		tabindex
		title
	 */
	static String[] globalAttributes = {"id", "name", "class"};
	
	/*
	 * The following event handler content attributes may be specified on any HTML element:

		onabort
		onblur*
		oncanplay
		oncanplaythrough
		onchange
		onclick
		oncontextmenu
		oncuechange
		ondblclick
		ondrag
		ondragend
		ondragenter
		ondragleave
		ondragover
		ondragstart
		ondrop
		ondurationchange
		onemptied
		onended
		onerror*
		onfocus*
		oninput
		oninvalid
		onkeydown
		onkeypress
		onkeyup
		onload*
		onloadeddata
		onloadedmetadata
		onloadstart
		onmousedown
		onmousemove
		onmouseout
		onmouseover
		onmouseup
		onmousewheel
		onpause
		onplay
		onplaying
		onprogress
		onratechange
		onreadystatechange
		onreset
		onscroll*
		onseeked
		onseeking
		onselect
		onshow
		onstalled
		onsubmit
		onsuspend
		ontimeupdate
		onvolumechange
		onwaiting
	 */
	String[] eventHandlerAttributes = {"onclick", "onmousedown", "onsubmit", "onload"};
	
	public static List<String> getGlobalAttributes(){
		return Arrays.asList(globalAttributes);
	}
	
	public static Node mutateGlobalAttribute(Node toMutate) {
		if(!toMutate.hasAttributes()) {
			return null;
		}
		
		NamedNodeMap attributes = toMutate.getAttributes();
		for(int i =0; i< attributes.getLength(); i++) {
			String attribute = attributes.item(i).getNodeName().toLowerCase();
			String value = attributes.item(i).getNodeValue();
			switch(attribute) {
			//Global Attributes	
			case "id":
			case "class":
			case "title":
				value = StringMutator.getMutant(value);
				attributes.item(i).setNodeValue(value);
				return toMutate;
			case "hidden":
				attributes.removeNamedItem("hidden");
				return toMutate;
			default:
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Gloabl attributes manipulated so far
	 * mutate existing attribute value
	 */
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		return mutateGlobalAttribute(toMutate);
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		String script = "var i = 0; var success = false; "
				+ "while(i<arguments[0].attributes.length) {"
				+ " var attrName = arguments[0].attributes.item(i).name;"
				+ " switch(attrName) {"
				+ "		case 'id':"
				+ "		case 'class':"
				+ "		case 'title':"
				+ "			arguments[0].attributes.item(i).value += arguments[1];"
				+ "			success = true;"
				+ "			break;"
				+ "	}"
				+ " if(success){break;}"
				+ " i = i+1;} \n";

		((JavascriptExecutor)driver).executeScript(script, toMutate, "_mut");
		return toMutate;
	}

	


	
	
	

}
