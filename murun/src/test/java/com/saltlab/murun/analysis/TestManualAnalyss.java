package com.saltlab.murun.analysis;

import java.util.Arrays;

import org.junit.Test;

import com.saltlab.murun.runner.AnalysisRecord;
import com.saltlab.murun.runner.AnalyzeMutants.MutantClass;

public class TestManualAnalyss {
	@Test
	public void testBugSeverity(){
		AnalysisRecord record = new AnalysisRecord();
		record.setCandidate("dummy");
		record.setKilled(false);
		record.setMutantClass(MutantClass.noneq);
		String[] reqList = {"executeAssertImmediate"};
		record.setReqTestList(Arrays.asList(reqList));
		String[] catList = {"brokenEventHandler"};
		record.setSelectedCategories(Arrays.asList(catList));
		String[] labelList = {"codeError"};
		record.setSelectedLabels(Arrays.asList(labelList));
		
		System.out.println(record.computeSeverityScore());
		System.out.println(record.computeStubbornScore());
	}
}
