package com.saltlab.murun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.crawljax.core.state.StateVertex;
import com.saltlab.murun.runner.MutationCandidateEx;
import com.saltlab.murun.runner.MutationRunResult;
import com.saltlab.murun.runner.MutationSession;
import com.saltlab.murun.runner.Stub;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.runner.TraceSession;
import com.saltlab.murun.utils.MutationUtils;
import com.saltlab.murun.utils.Settings;
import com.saltlab.murun.utils.TraceUtils;
import com.saltlab.webmutator.MutationMode;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.WebMutator;
import com.saltlab.webmutator.operators.Operator;
import com.saltlab.webmutator.operators.dom.TextNodeMutator;
import com.saltlab.webmutator.plugins.DefaultNodePicker;
import com.saltlab.webmutator.utils.XPathHelper;

public class TestMutationReport {
//	@Test
//	public void createReport() throws XPathExpressionException, IOException {
//		SUBJECT subject= SUBJECT.dummy;
//		TraceSession traceSession = TraceUtils.collectTrace(true, subject);
//		StateVertex state = traceSession.getStateFlowGraph().getAllStates().asList().get(0);
//		Document doc = state.getDocument();
//		Node node = XPathHelper.evaluateXpathExpression(doc, "/HTML[1]/BODY[1]/DIV[1]").item(0);
//		Operator operator = new TextNodeMutator();
//		MutationCandidateEx candidate = new MutationCandidateEx(node, node, operator, state.getClosestFragment(node));
//		WebMutator mutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
//		MutationRecord record = mutator.applyMutationToCandidate(candidate);
//		List<MutationCandidateEx> recordList = new ArrayList<>();
//		recordList.add(candidate);
//		MutationUtils.writeMutationRecords(subject, recordList);
//		
////		MutationRunResult result = new MutationRunResult(candidate, "testReport");
////		Settings.currResult = result;
////		MutationSession session = new MutationSession(candidate);
////		session.setResult(result);
////		MutationRunResult runResult = MutationUtils.mutationRun(session, SUBJECT.dummy);
//		MutationUtils.mutationAnalysis(recordList, SUBJECT.dummy);
////		System.out.println(runResult);
//	}
	
	@Test
	public void callMain() {
		Settings.MAX_RUN = 2;
		Stub.subject = SUBJECT.dummy;
		String[] args = {};
		Stub.main(args);
	}
}
