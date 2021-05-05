package com.saltlab.webmutator;

import java.awt.Rectangle;

import org.w3c.dom.Node;

import com.saltlab.webmutator.operators.Operator;
import com.saltlab.webmutator.utils.VipsUtils;
import com.saltlab.webmutator.utils.XPathHelper;

public abstract class MutationCandidate {
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public void setNode(Node node) {
		this.nodeToMutate = node;
	}

	public void setOpString(String opString) {
		this.opString = opString;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public MutationCandidate(Node nodeToMutate, Node origNode, Operator applicable, String stateName) {
		this.nodeToMutate= nodeToMutate;
		this.origNode = origNode;
		this.xpath = XPathHelper.getSkeletonXpath(nodeToMutate);
		this.operator = applicable;
		this.opString = operator.getClass().getSimpleName();
		this.stateName = stateName;
		if(origNode!=null) {
			populateNodeProperties();
		}
	}
	
	public static NodeProperties extractNodeProperties(Node node) {
		String display = null;
		String eventListener = null;
		Rectangle position= null;
		
		
		if(node!=null) {
			display = VipsUtils.isDisplayed(node);
			eventListener = VipsUtils.getEventListenerVal(node);
			position = VipsUtils.getRectangle(node);
		}
		
		 return new NodeProperties(display, position, eventListener);
	}
	private void populateNodeProperties() {
		origNodeProperties = extractNodeProperties(origNode);
	}

	public NodeProperties getOrigNodeProperties() {
		return origNodeProperties;
	}

	public void setOrigNodeProperties(NodeProperties origNodeProperties) {
		this.origNodeProperties = origNodeProperties;
	}

	private transient Node nodeToMutate;
	private transient Node origNode;

	private transient Operator operator;
	private String opString;
	private String xpath;
	private String stateName;
	
	//Node properties 
	NodeProperties origNodeProperties;
	
	
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	private MutationRecord record;
	
	public MutationRecord getRecord() {
		return record;
	}

	public void setRecord(MutationRecord record) {
		this.record = record;
	}

	public Node getNodeToMutate() {
		return nodeToMutate;
	}
	
	public String getOpString() {
		return opString;
	}
	
	public String getXpath() {
		return xpath;
	}
	
	public String toString() {
		String xpathId = xpath.replace("[", "").replace("]", "").replace("/", "-");
		return this.opString + "_" +this.stateName+"_"+xpathId   ;
	}
	
	public Node getOrigNode() {
		return origNode;
	}
}
