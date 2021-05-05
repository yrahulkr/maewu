package com.saltlab.webmutator.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class EventListenerUtils {
	public static List<String> getAllEventListeners(Document dom){
		List<String> allListeners = new ArrayList<String>();
		try {
			NodeList nodesWithEvents = XPathHelper.evaluateXpathExpression(dom, "//*[@evlist='true']");
			for(int i = 0; i<nodesWithEvents.getLength(); i++) {
				String listener = nodesWithEvents.item(i).getAttributes().getNamedItem("evlistval").getNodeValue();
				allListeners.add(listener);
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allListeners;
	}

	public static String getRandomEventListener(Set<String> allListeners) {		
		if(allListeners.size()<1) {
			return null;
		}
		Random rand = new Random();
		rand.ints();
		int nextInt = Math.abs(rand.nextInt()%allListeners.size());
		List<String> list = new ArrayList<>();
		list.addAll(allListeners);
		return list.get(nextInt);
	}
}
