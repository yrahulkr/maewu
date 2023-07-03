package com.saltlab.murun.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saltlab.murun.runner.MutationRunResult;
import com.saltlab.murun.runner.Stub;
import com.saltlab.murun.runner.TestFailureDetail;
import com.saltlab.murun.runner.Stub.SUBJECT;

import ch.qos.logback.core.util.FileUtil;

import com.saltlab.murun.runner.TraceSession;
import com.saltlab.murun.runner.UtilsRunner;

public class TraceUtils {
	static final Logger LOG = LoggerFactory.getLogger(TraceUtils.class);

	public static boolean traceExists(String rootDir) {
		File validCrawlFolder = Paths.get(Settings.traceDir, rootDir, Settings.REL_TRACE_LOC).toFile();
		if(validCrawlFolder.exists()) {
			File validCrawl = new File(validCrawlFolder, "result.json");
			if(validCrawl.exists()) {
				return true;
			}
			else {
				// Clean the folder
				try {
					FileUtils.forceDelete(validCrawlFolder);
					LOG.info("Folder {} deleted because it was not a valid crawl", validCrawlFolder);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LOG.error("Invalid Crawl, but failed to delete {}", validCrawlFolder);
				}
			}
		}
		
		return false;
	}
	
	
	
	public static TraceSession collectTrace(boolean rerun, SUBJECT subject) {
		
		if(rerun) {
			if(traceExists(subject.name()+"_new")) {
					System.err.println("New Test Trace already exists!! : " + subject);
					return null;
				}
		}else {
			if(traceExists(subject.name())) {
				System.err.println("Original Trace already exists!! : " + subject);
				return null;
			}
		}
		
		
		Settings.outputDir = Settings.traceDir;
		
		String traceOutput = Paths.get(Settings.outputDir, subject.name()).toString();
		if(rerun) {
			traceOutput = traceOutput += "_new";
		}
		
			
		TraceSession session = TraceSession.getInstance();
		

		Settings.USE_CRAWLJAX = true;
		
		try {
			session.crawljaxSetup(traceOutput, "http://crawls:3000");
		}catch(Exception ex) {
			LOG.error("Exception setting up Crawljax : {}", ex.getMessage());
			return null;
		}finally {
//			DriverProvider.getInstance().getBrowser().close();
		}
		
		
		
		Settings.aspectActive = true;
		long start = System.currentTimeMillis();

		Settings.currResult = new MutationRunResult(null, Settings.outputDir);
		try {
			Optional<Description> failure = UtilsRunner.executeTests(subject, rerun);
			if(failure.isPresent()) {
				Settings.currResult.setFailed(true);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			Settings.currResult.setFailed(true);
			TestFailureDetail detail = new TestFailureDetail("overall", ex.toString(), ex.toString());
			Settings.currResult.addTestResult(detail);
			session = null;
		}
		long end = System.currentTimeMillis();
		Settings.currResult.setDuration(end-start);
		MutationUtils.writeResult(Settings.currResult, Paths.get(traceOutput, "traceGenResult.json").toString());
		
		return session;
	}
}
