package com.saltlab.webmutator.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.saltlab.webmutator.operators.Operator;
import com.saltlab.webmutator.operators.dom.DomOperator;
import com.saltlab.webmutator.utils.DomUtils;

public class DefaultNodePicker implements NodePickerPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNodePicker.class
            .getName());

    Random random = new Random();
	
    public DefaultNodePicker() {
    	random.longs();
    }
    
	public static void populateLeafNodes(Document dom, List<Node> nodes) {
		try {
			NodeList leafNodeList = DomUtils.getAllLeafNodes(dom.getElementsByTagName("body").item(0));
			for(int i =0; i< leafNodeList.getLength(); i++) {
				Node item = leafNodeList.item(i);
				if(item.getTextContent().trim().isEmpty()) {
					continue;
				}
				nodes.add(item);
			}
		} catch (XPathExpressionException e) {
			LOGGER.error("Error populating leaf nodes");
		}
		
	}

	@Override
	public Node pickNode(Document dom, Operator domOperator) {
			
		List<String> tagList = ((DomOperator)domOperator).getTagList();
		if(tagList.isEmpty()) {
			tagList = DomOperator.getDefaultTags();
		}
		List<Node> nodes = new ArrayList<Node>();
		for(String tag: tagList) {
			if(tag.equalsIgnoreCase("#text")) {
				//	            populateTextNodes(dom, nodes);
				populateLeafNodes(dom, nodes);
			}
			else {

				NodeList tagNodes = dom.getElementsByTagName(tag);
				for(int i = 0; i<tagNodes.getLength(); i++)
					nodes.add(tagNodes.item(i));
			}
		}

		if(nodes.size() == 0) {
			return null;
		}

		int nodeNumber = Math.abs((int) (random.nextLong() % nodes.size()));
		System.out.println("nodes available " + nodes.size());

		System.out.println("Node number " + nodeNumber);
		System.out.println("Picked : " + nodes.get(nodeNumber));
		return nodes.get(nodeNumber);
	}
}
