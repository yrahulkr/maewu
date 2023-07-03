package com.saltlab.murun.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saltlab.webmutator.MutationRecord;

public class MutationSession {
	private static final Logger LOG = LoggerFactory.getLogger(MutationSession.class);
	MutationRunResult result;
	private List<MutationCandidateEx> mutationMap;
	private MutationCandidateEx candidate;

	
	
	public MutationSession(MutationCandidateEx candidate) {
		this.candidate = candidate;
	}


	public MutationCandidateEx getCandidate() {
		return candidate;
	}

	public void setResult(MutationRunResult currResult) {
		this.result = currResult;
	}

	/*
	 * Returns all mutations to be performed on the state
	 */
	public List<MutationRecord> getRecords(String state) {
		if(!candidate.getMutantMap().containsKey(state)) {
			return null;
		}
		List<MutationRecord> records = candidate.getMutantMap().get(state);
		
		return records;
	}
	
}
