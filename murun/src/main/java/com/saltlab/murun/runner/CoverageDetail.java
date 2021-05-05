package com.saltlab.murun.runner;

import com.crawljax.vips_selenium.VipsUtils.AccessType;
import com.crawljax.vips_selenium.VipsUtils.Coverage;

public class CoverageDetail {
	final String statementName;
	final Coverage coverage;
	final AccessType accessType;
	final String state;
	
	public CoverageDetail(String statementName, Coverage coverage, AccessType accessType, String state) {
		this.statementName = statementName;
		this.coverage = coverage;
		this.accessType = accessType;
		this.state = state;
	}
	public String getStatementName() {
		return statementName;
	}
	public Coverage getCoverage() {
		return coverage;
	}
	
	public String getState() {
		return state;
	}
	
	@Override 
	public String toString() {
		return statementName + " : " + coverage + " : " + state;
	}
}
