package com.saltlab.webmutator.operators.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.DomUtils;
import com.saltlab.webmutator.utils.XPathHelper;

public class SubtreeMove extends DomOperator{
	public SubtreeMove() {
		tagList.clear();
		tagList.add("div");
		tagList.add("span");
		
		tagList.add("p");
		
		tagList.add("table");
		tagList.add("tbody");
		tagList.add("tr");
		tagList.add("td");

		tagList.add("ul");
		tagList.add("li");
		
		tagList.add("select");
	}
	
	public static Node getMoveTarget(Node toMutate){
		Document dom = toMutate.getOwnerDocument();
		List<Node> nodes = new ArrayList<Node>();
		for(String tag: containerNodes) {
			if(tag.equalsIgnoreCase("p")) {
				// excluding P from the subtree move
				continue;
			}
			NodeList tagNodes = dom.getElementsByTagName(tag);
			for(int i = 0; i<tagNodes.getLength(); i++)
				nodes.add(tagNodes.item(i));
		}
		
		List<Node> available = new ArrayList<>();
		for(Node node : nodes) {
			if(node.isSameNode(toMutate) || node.isSameNode(toMutate.getParentNode())) {
				// excluding the nodetoMutate and its parent from available
				continue;
			}
			
			if(DomUtils.contains(toMutate, node)) {
				// excluding children of the node to be mutated
				continue;
			}
			available.add(node);
		}
		if(available.size() == 0) {
			return null;
		}
		
		Random rand = new Random();
		rand.longs();
		int randomInt = Math.abs((int) (rand.nextLong()%available.size()));
		return available.get(randomInt);
	}

	// Works on relative xpaths for each node 
	
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		if(record.getData() == null) {
			Node target = getMoveTarget(toMutate);
			String xpath = XPathHelper.getSkeletonXpath(target);
			record.setData(xpath);
		}
		try {
			Node targetNode = DomUtils.getElementByXpath(toMutate.getOwnerDocument(), (String)record.getData());
			targetNode.appendChild(toMutate);
			return toMutate;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		String script = 
				"try{c=arguments[0];"
				+ "a = document.evaluate(arguments[1], document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;\n" + 
				"if(a.attributes.getNamedItem('mutated')) {return true;}" +
				"	a.appendChild(c);" +
				"	a.attributes.setNamedItem(document.createAttribute('mutated'));" +
				"return true;}" +
				"catch(ex){return false;}";
		try {
			boolean success = (boolean) ((JavascriptExecutor)driver).executeScript(script, toMutate, record.getData());
			if(success) {
				return toMutate;
			}
			return null;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
