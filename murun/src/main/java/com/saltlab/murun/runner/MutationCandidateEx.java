package com.saltlab.murun.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.crawljax.core.state.StateVertex;
import com.crawljax.fragmentation.Fragment;
import com.crawljax.vips_selenium.VipsUtils.AccessType;
import com.crawljax.vips_selenium.VipsUtils.Coverage;
import com.saltlab.webmutator.MutationCandidate;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.operators.Operator;

public class MutationCandidateEx extends MutationCandidate {
	public Map<String, List<MutationRecord>> getMutantMap() {
		return mutantMap;
	}

	public void setMutantMap(Map<String, List<MutationRecord>> mutantMap) {
		this.mutantMap = mutantMap;
	}
	List <CoverageDetail> coverageMap = new ArrayList<>();
	private transient Fragment fragment ;
	private Map<String, List<MutationRecord>> mutantMap;
	
	private double priorityScore = -1.0;
	
	private double operatorScore = -1.0;
	
	private double elementScore = -1.0;
	
	// To be used when computing priority based on already selected mutants
	private double currentScore = -1.0;
	
	public void setRecord(MutationRecord record) {
		super.setRecord(record);
		mutantMap = new HashMap<String, List<MutationRecord>>();
		try {
			List<MutationRecord> recordsForState = new ArrayList<MutationRecord>();
			recordsForState.add(record);
			mutantMap.put(fragment.getReferenceState().getName(), recordsForState);
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Could not add node to mutant map");
		}
	}
	
	public MutationCandidateEx(Node node, Node nodeToMutate, Operator applicable, Fragment closest) {
		super(nodeToMutate, node, applicable, closest.getReferenceState().getName());
		this.setFragment(closest);
	}
	
	public MutationCandidateEx(Node node, Node nodeToMutate, Operator applicable, Fragment closest, List<CoverageDetail> coverageDetails) {
		super(nodeToMutate, node, applicable, closest.getReferenceState().getName());
		this.setFragment(closest);
		this.coverageMap = coverageDetails;
	}
	
	public MutationCandidateEx(Node node, Operator operator) {
		super(node, node, operator, "unknown");
		this.setFragment(null);
	}

	public boolean addToMutantMap(String stateName, MutationRecord record) {
		try {
			if(!mutantMap.containsKey(stateName)) {
				mutantMap.put(stateName, new ArrayList<MutationRecord>());
			}
			mutantMap.get(stateName).add(record);
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void replicateRecord(StateVertex existingState, StateVertex newState) {
		if(mutantMap.containsKey(existingState.getName())) {
			mutantMap.put(newState.getName(), mutantMap.get(existingState.getName()));
		}
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	
	
	public void addCoverageInfo(String testId, Coverage coverage, AccessType accessType) {
		coverageMap.add(new CoverageDetail(testId, coverage, accessType, null));
	}

	public double getPriorityScore() {
		return priorityScore;
	}

	public void setPriorityScore(double priorityScore) {
		this.priorityScore = priorityScore;
		this.currentScore = priorityScore;
	}

	public double getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(double currentScore) {
		this.currentScore = currentScore;
	}

	public double getOperatorScore() {
		return operatorScore;
	}

	public void setOperatorScore(double operatorScore) {
		this.operatorScore = operatorScore;
	}

	public double getElementScore() {
		return elementScore;
	}

	public void setElementScore(double elementScore) {
		this.elementScore = elementScore;
	}
}
