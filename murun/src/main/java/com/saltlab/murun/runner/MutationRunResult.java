package com.saltlab.murun.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saltlab.webmutator.MutationRecordEx;

public class MutationRunResult {
	MutationCandidateEx candidate;
	List<TestFailureDetail> failedTests = new ArrayList<>();
	String runFolder;
	private boolean failed;
	long duration;
	
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}

	Map<String, List<MutationRecordEx>> mutationRecordMap = new HashMap<String, List<MutationRecordEx>>();
	String testSuiteName = null;
	
	private int runCount; // tests run

	public String getTestSuiteName() {
		return testSuiteName;
	}
	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}
	public Map<String, List<MutationRecordEx>> getMutationRecordMap() {
		return mutationRecordMap;
	}
	public void setMutationRecordMap(Map<String, List<MutationRecordEx>> mutationRecordMap) {
		this.mutationRecordMap = mutationRecordMap;
	}
	public boolean isFailed() {
		return failed;
	}
	public MutationRunResult(MutationCandidateEx candidate, String outputDir) {
		this.candidate = candidate;
		this.runFolder = outputDir;
	}
	public MutationCandidateEx getCandidate() {
		return candidate;
	}
	public void setCandidate(MutationCandidateEx mutatedState) {
		this.candidate = mutatedState;
	}
	public List<TestFailureDetail> getFailedTests() {
		return failedTests;
	}
	public void setFailedTests(List<TestFailureDetail> failedTests) {
		this.failedTests = failedTests;
	}
	public String getRunFolder() {
		return runFolder;
	}
	public void setRunFolder(String runFolder) {
		this.runFolder = runFolder;
	}
	public void setFailed(boolean b) {
		this.failed = b;
	}
	public void addMutationRecord(String state, MutationRecordEx recordEx) {
		if(!this.mutationRecordMap.containsKey(state)) {
			this.mutationRecordMap.put(state, new ArrayList<>());
		}
		this.mutationRecordMap.get(state).add(recordEx);
	}
	
	public void addTestResult(TestFailureDetail detail) {
		this.failedTests.add(detail);
	}
	public void setRunTests(int runCount) {
		this.runCount = runCount;
	}
	public int getRunCount() {
		return this.runCount;
	}
}
