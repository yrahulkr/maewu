package com.saltlab.murun.runner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.junit.runner.Description;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.crawljax.fragmentation.Fragment;
import com.crawljax.util.DomUtils;
import com.saltlab.murun.runner.MutantGenerator.GENMODE;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.utils.Settings;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.operators.Operator;
import com.saltlab.webmutator.operators.dom.DomOperator;
import com.saltlab.webmutator.utils.XPathHelper;

import utils.TestCaseExecutor;

public class TestMutantPrioritization {
	@Test
	public void testCoverage() {
		
		Settings.traceDir = "testCrawljax";

		Settings.outputDir = Settings.traceDir;
		

		Settings.outputDir = Settings.traceDir;
		Settings.aspectActive = true;
		Settings.USE_CRAWLJAX = true;
		
		SUBJECT subject = SUBJECT.mantisbt;
		
//		TestCaseExecutor.restartDocker(subject, true);
		
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		String traceOutput = Paths.get(Settings.outputDir, subject.name()).toString();
		
			
		TraceSession session = TraceSession.getInstance();
		try {
			session.crawljaxSetup(traceOutput, "http://crawls:3000");
		}catch(Exception ex) {
			System.err.println("Exception setting up Crawljax : {}" + ex.getMessage());
			
		}finally {
//			DriverProvider.getInstance().getBrowser().close();
		}
		
		Settings.aspectActive = true;
		long start = System.currentTimeMillis();

		Settings.currResult = new MutationRunResult(null, Settings.outputDir);
		try {
			Optional<Description> failure = UtilsRunner.executeTests(subject, false);
			if(failure.isPresent()) {
				Settings.currResult.setFailed(true);
			}
		}catch(Exception ex) {
			Settings.currResult.setFailed(true);
			TestFailureDetail detail = new TestFailureDetail("overall", ex.toString(), ex.toString());
			Settings.currResult.addTestResult(detail);
			session = null;
		}
		
		System.out.println(session.getCoverageMap());
		System.out.println(session.getObserverCoverageMap());
		
//		session.stopTrace();
		System.out.println(Settings.currResult);
//		TestCaseExecutor.restartDocker(subject, false);
		
		MutantGenerator mutGen = new MutantGenerator(session, subject, GENMODE.NEW);
		List<MutationCandidateEx> mutationRecords = mutGen.mutateTrace();
		System.out.println(mutationRecords);
	}
	
	/*
	@Test
	public void testMutantConcreteInstances() throws XPathExpressionException, IOException {
		
		Settings.traceDir = "testCrawljax";

		Settings.outputDir = Settings.traceDir;
		

		Settings.outputDir = Settings.traceDir;
		
		
		SUBJECT subject = SUBJECT.addressbook;
		
		TestCaseExecutor.restartDocker(subject, true);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String traceOutput = Paths.get(Settings.outputDir, subject.name()).toString();
		
			
		TraceSession session = TraceSession.getInstance();
		Settings.aspectActive = true;
		long start = System.currentTimeMillis();

		Settings.currResult = new MutationRunResult(null, Settings.outputDir);
		try {
			Optional<Description> failure = UtilsRunner.executeTests(subject, false);
			if(failure.isPresent()) {
				Settings.currResult.setFailed(true);
			}
		}catch(Exception ex) {
			Settings.currResult.setFailed(true);
			TestFailureDetail detail = new TestFailureDetail("overall", ex.toString(), ex.toString());
			Settings.currResult.addTestResult(detail);
			session = null;
		}
		
		System.out.println(session.getCoverageMap());
		System.out.println(session.getObserverCoverageMap());
		
//		session.stopTrace();
		System.out.println(Settings.currResult);
		TestCaseExecutor.restartDocker(subject, false);
		
		MutantGenerator mutGen = new MutantGenerator(session, subject, GENMODE.NEW);

		List<MutationCandidateEx> candidatePool = new ArrayList<MutationCandidateEx>();
		
		Node origNode = XPathHelper.evaluateXpathExpression(session.allConcreteStates.get(0).getDocument(), "/HTML[1]/BODY[1]/DIV[1]/DIV[4]/FORM[1]/INPUT[2]").item(0);
		Document toMutate = DomUtils.asDocument(session.allConcreteStates.get(0).getStrippedDom());
		Node nodeToMutate = DomUtils.getElementByXpath(toMutate, XPathHelper.getXPathExpression(origNode));
		List<DomOperator> applicableOp = new ArrayList<DomOperator>();
		applicableOp.add(mutGen.webMutator.getDomOperator("EventHandlerAdd"));
		List<CoverageDetail> coverageDetails = mutGen.getCoverageDetails(origNode, session.allConcreteStates.get(0));
		Fragment closest = session.allConcreteStates.get(0).getClosestFragment(origNode);
		for (Operator applicable : applicableOp) {
			candidatePool.add(
					new MutationCandidateEx(origNode, nodeToMutate, applicable, closest, coverageDetails));
		}		
		MutationRecord record = mutGen.webMutator.applyMutationToCandidate(candidatePool.get(0));	
		mutGen.addMapforCandidates(candidatePool);
//		List<MutationCandidateEx> mutationRecords = mutGen.mutateTrace();
		System.out.println(record);
	}*/
}
