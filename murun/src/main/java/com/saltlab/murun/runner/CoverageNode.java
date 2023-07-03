package com.saltlab.murun.runner;

public class CoverageNode {
	final String xpath;
	final int state;
	
	public CoverageNode(int state, String xpath) {
		this.xpath = xpath;
		this.state = state;
	} 
	
	public String getXpath() {
		return xpath;
	}
	public int getState() {
		return state;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof CoverageNode)) {
			return false;
		}
		CoverageNode otherNode = (CoverageNode)other;
		return state==otherNode.getState() && xpath.equalsIgnoreCase(otherNode.getXpath());
	}
	
	@Override
	public int hashCode() {
		return xpath.hashCode() + state;
	}
	
	@Override 
	public String toString() {
		return "state" + state + " : " + xpath;
	}
}
