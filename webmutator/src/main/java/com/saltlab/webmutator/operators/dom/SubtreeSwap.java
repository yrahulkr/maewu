package com.saltlab.webmutator.operators.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.util.Combinations;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.XPathHelper;

public class SubtreeSwap extends DomOperator{
	
	public SubtreeSwap() {
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
	public static Pair<Node, Node> getSwapNodes(Node toMutate){
		NodeList childNodes = toMutate.getChildNodes();
		List<Node> available = new ArrayList<>();
		for(int i =0; i< childNodes.getLength(); i++) {
			if(DomOperator.getContainerNodes().contains(childNodes.item(i).getNodeName().toLowerCase())) {
				available.add(childNodes.item(i));
			}
		}
		if(available.size() < 2) {
			return null;
		}
		
		long max = CombinatoricsUtils.factorial(available.size())/(2* CombinatoricsUtils.factorial(available.size()-2));
		Combinations comb = new Combinations(available.size(), 2);
		Iterator<int[]> iterator  = comb.iterator();
		Random rand = new Random();
		rand.longs();
		int randomInt = Math.abs((int) (rand.nextLong()%max));
		
		int curr = 0;
		while (curr < randomInt && iterator.hasNext()) {
	        final int[] combination = iterator.next();
	        System.out.println(Arrays.toString(combination));
	    }
		
		int[] validComb = iterator.next();
		Pair<Node, Node> returnPair = Pair.of(available.get(validComb[0]), available.get(validComb[1]));
		return returnPair;
	}

	// Works on relative xpaths for each node 
	
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		if(record.getData() == null) {
			Pair<Node, Node> pairNodes  = getSwapNodes(toMutate);
			if(pairNodes == null) {
				System.out.println("Cannot apply treeswap to node");
				return null;
			}
			String leftXPath = XPathHelper.getXPathFromSpecificParent(pairNodes.getLeft(), toMutate);
			String rightXPath =  XPathHelper.getXPathFromSpecificParent(pairNodes.getRight(), toMutate);
			Pair<String, String> xpathPair = Pair.of(leftXPath, rightXPath);
			record.setData(xpathPair);
		}
		try {
//			Gson gson = new Gson();
//			Type pairType = new TypeToken<Pair<String, String>>() {}.getType();
//			
//			Pair<String, String> insertAttr = gson.fromJson(record.getData(), pairType);
			Pair<String, String> insertAttr = (Pair<String, String>) record.getData();

			Node leftNode = XPathHelper.getNodeFromSpecificParent(toMutate, insertAttr.getLeft());
			Node rightNode = XPathHelper.getNodeFromSpecificParent(toMutate, insertAttr.getRight());
			Node dummy = toMutate.getOwnerDocument().createElement("dummy");
			toMutate.insertBefore(dummy, leftNode);
			toMutate.insertBefore(leftNode, rightNode);
			toMutate.insertBefore(rightNode, dummy);
			toMutate.removeChild(dummy);
			return toMutate;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		String script = "try{c=arguments[0];"
				+ "a = document.evaluate(arguments[1], c, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;\n" + 
				"b=document.evaluate(arguments[2], c, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;\n" + 
				"d= document.createElement('dummy');\n" + 
				"c.insertBefore(d,a);c.insertBefore(a,b); c.insertBefore(b,d);c.removeChild(d); return true;}catch(ex){return false;}";
		
		try {
//			Gson gson = new Gson();
//			Type pairType = new TypeToken<Pair<String, String>>() {}.getType();
//			Pair<String, String> insertAttr = gson.fromJson(record.getData(), pairType);
			Pair<String, String> insertAttr = (Pair<String, String>) record.getData();
			boolean success = (boolean) ((JavascriptExecutor)driver).executeScript(script, toMutate, insertAttr.getLeft(), insertAttr.getRight());
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
