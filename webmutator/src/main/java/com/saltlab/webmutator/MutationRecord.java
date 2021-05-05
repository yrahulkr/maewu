package com.saltlab.webmutator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class MutationRecord {
	String operator;
	boolean success;
	String error;
	transient String originalDom;
	String originalXpath;
	String mutatedXpath;
	transient Document doc;
	transient Node mutatedNode;
	transient String mutatedDom;
	Object data;
	NodeProperties origNodeProperties;
	
	public NodeProperties getOrigNodeProperties() {
		return origNodeProperties;
	}


	public void setOrigNodeProperties(NodeProperties origNodeProperties) {
		this.origNodeProperties = origNodeProperties;
	}


	private void init() {
		success = false;
		error = null;
		originalDom = null;
		originalXpath = null;
		mutatedXpath = null;
		doc = null;
		mutatedNode = null;
		mutatedDom = null;
		data = null;
		origNodeProperties = null;
	}
	

	public MutationRecord(Document doc, String operator, Node mutated, Object data) {
		init();
		this.doc = doc;
		this.operator = operator;
		this.mutatedNode = mutated;
		this.data = data;
	}
	
	public MutationRecord(Document doc, String operator, Node mutated, Object data, NodeProperties origNodeProperties) {
		init();
		this.doc = doc;
		this.operator = operator;
		this.mutatedNode = mutated;
		this.data = data;
		this.origNodeProperties = origNodeProperties;
	}
	
	public MutationRecord(boolean success, String error) {
		init();
		this.success = success;
		this.error = error;
	}
	
	
	public MutationRecord(String originalDom, String operator, String originalXpath, Object data) {
		init();
		this.originalDom = originalDom;
		this.operator = operator;
		this.originalXpath = originalXpath;
		this.data = data;
	}
	
	public String getOriginalXpath() {
		return originalXpath;
	}

	public void setOriginalXpath(String originalXpath) {
		this.originalXpath = originalXpath;
	}

	public String getMutatedXpath() {
		return mutatedXpath;
	}

	public void setMutatedXpath(String mutatedXpath) {
		this.mutatedXpath = mutatedXpath;
	}

	public Node getMutatedNode() {
		return mutatedNode;
	}

	public void setMutatedNode(Node mutatedNode) {
		this.mutatedNode = mutatedNode;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getOriginalDom() {
		return originalDom;
	}
	public void setOriginalDom(String originalDom) {
		this.originalDom = originalDom;
	}
	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	public Node getMutated() {
		return mutatedNode;
	}
	public void setMutated(Node mutated) {
		this.mutatedNode = mutated;
	}
	public String getMutatedDom() {
		return mutatedDom;
	}
	public void setMutatedDom(String mutatedDom) {
		this.mutatedDom = mutatedDom;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String toString() {
		return this.success + " : " +  this.operator + " : " + this.originalXpath;
	}


	public Object getData() {
		return data;
	}


	public void setData(Object string) {
		this.data = string;
	}
	
}
