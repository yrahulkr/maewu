package com.saltlab.murun.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Singleton;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.utils.MutationUtils;
import com.saltlab.murun.utils.Settings;
import com.saltlab.webmutator.MutationMode;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.MutationRecordEx;
import com.saltlab.webmutator.WebMutator;
import com.saltlab.webmutator.plugins.DefaultNodePicker;

import utils.TestCaseExecutor;

@Singleton
public class MutationRunner {
	static final Logger LOG = LoggerFactory.getLogger(MutationRunner.class);

	static MutationRunner instance = null;
	
	
	SUBJECT subject = null;
	List<MutationCandidateEx> mutationCandidates = null;
	boolean newSuite;

	public MutationSession currentMutation = null;
	public WebMutator mutator = null;
	
	Map<MutationCandidateEx, MutationRunResult> mutationScoreMap = null;
	
	public static MutationRunner getInstance() {
		return instance;
	}
	
	public MutationRunner(SUBJECT subject, List<MutationCandidateEx> mutationCandidates, boolean newSuite) {
		this.subject = subject;
		this.mutationCandidates = mutationCandidates;
		this.newSuite = newSuite;
		
		this.mutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
		mutationScoreMap = new HashMap<>(); 
		
		instance = this;
	}
	
	public static List<String> getCandidatesFromResult(Map<String, MutationRunResult> allRecords, boolean failed){
		List<String> returnCandidates = new ArrayList<String>();
		
		for(MutationRunResult result: allRecords.values()) {
			if(failed && !result.isFailed()) {
				continue;
			}
			returnCandidates.add(result.getCandidate().toString());
		}
		return returnCandidates;
	}
	
	public static Options getCommandLineOptions() {
		Options options = new Options();

        Option subject = new Option("S", "subject", true, "Test Subject - (addressbook, mantisbt, mrbs, ppma, collabtive, claroline, dummy)");
        subject.setRequired(true);
        options.addOption(subject);

        Option newSuite = new Option("NS", "newsuite", true, "suite to run - true/false");
        newSuite.setRequired(true);
        options.addOption(newSuite);
        
        Option runCandidate = new Option("RC", "runcandidate", true, "run candidate - candidate ids available in mutationrecords.json");
        runCandidate.setRequired(false);
        runCandidate.setArgs(Option.UNLIMITED_VALUES);
        runCandidate.setValueSeparator(',');
        options.addOption(runCandidate);
        
        Option fromRun = new Option("R", "rerun", true, "run id - 0,1,2...");
        fromRun.setRequired(false);
        options.addOption(fromRun);
        
        Option runFailed = new Option("F", "runfailed", true, "rerun failed in the given run - true/false");
        runFailed.setRequired(false);
        options.addOption(runFailed);
        
        Option runOperator = new Option("RO", "runoperator", true, "Run all candidates for operator");
        runOperator.setRequired(false);
        runOperator.setArgs(Option.UNLIMITED_VALUES);
        runOperator.setValueSeparator(',');
        options.addOption(runOperator);
        
        Option maxRun = new Option("X", "maxrun", true, "Maximum mutants to analyze. Overrides Settings.MAX_RUN");
        maxRun.setRequired(false);
        options.addOption(maxRun);
        
        Option dryRun = new Option("D", "dryrun", true, "dryrun - true/false");
        dryRun.setRequired(false);
        options.addOption(dryRun);

        return options;
	}
	
	
	public static void main(String args[]) {
		
		Options options = getCommandLineOptions();
    	CommandLineParser parser = new DefaultParser();
    	HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        String subjectString = cmd.getOptionValue("subject");
        boolean newSuite = cmd.getOptionValue("newsuite").equalsIgnoreCase("true")?true:false;

		boolean dryrun = false;
		
		if(cmd.hasOption("dryrun"))
			dryrun = cmd.getOptionValue("dryrun").equalsIgnoreCase("true");
		
        
		int exitCode = 0;
//		if(args.length < 2) {
//			System.out.println("please provide subject, suite  " + 
//			"subjects : (addressbook, mantisbt, mrbs, ppma, collabtive, claroline)"+
//					"suite: old, new");
//			exitCode = -1;
//			System.exit(exitCode);
//		}
		SUBJECT subject = Stub.getSubject(subjectString);
		
		
//		boolean newSuite = args[1].trim().equalsIgnoreCase("0")?false: true;

		boolean rerun = false;
		String runId = null;
		boolean runFailed = false;
		String[] candidatesToRun = null;
		
		if(cmd.hasOption("rerun")) {
			rerun = true;
			runId = cmd.getOptionValue("rerun");
			if(cmd.hasOption("runfailed")) {
				runFailed = true;
			}
		}
		

		List<MutationCandidateEx> candidatesToAnalyze = new ArrayList<>();

		
		List<MutationCandidateEx> mutationCandidates = null;
		
		if(cmd.hasOption("runcandidate")) {
			candidatesToRun = cmd.getOptionValues("runcandidate");
		}
		else if(rerun) {
			HashMap<String, MutationRunResult> resultsJson = loadRunResult(subject, runId);
			List<String> fromResult = getCandidatesFromResult(resultsJson, runFailed);
			if(fromResult!=null) {
				candidatesToRun = fromResult.toArray(new String[0]);
			}
		}
		
		
		mutationCandidates = MutationUtils.loadMutationMap(subject, newSuite);
		
		
		if (candidatesToRun!=null && candidatesToRun.length>0) {
			// Found specific mutants to run
			Settings.MAX_RUN = candidatesToRun.length;

			Map<String, MutationCandidateEx> candidateMap = new HashMap<>();
			for(MutationCandidateEx candidate: mutationCandidates) {
				candidateMap.put(candidate.toString(), candidate);
			}
			
			
			for(String candidate: candidatesToRun) {
				if(candidateMap.containsKey(candidate)) {
					candidatesToAnalyze.add(candidateMap.get(candidate));
				}
				else {
					System.out.println("Unknown Candidate ID : " + candidate);
				}
			}
		}
		else {
			candidatesToAnalyze.addAll(mutationCandidates);
		}
		
		
		if(cmd.hasOption("runoperator")) {
			List<String> operatorsToRun = Arrays.asList(cmd.getOptionValues("runoperator"));
			List<MutationCandidateEx> filterList = new ArrayList<>();
			for(MutationCandidateEx candidate: candidatesToAnalyze) {
				System.out.println(candidate.getOpString());
				if(operatorsToRun.contains(candidate.getOpString())){
					filterList.add(candidate);
				}
			}
			candidatesToAnalyze = filterList;
		}
		
		if(dryrun) {
			LOG.info("newsuite {}", newSuite);
			LOG.info("subject {}", subject);
			LOG.info("Execution to rerun {}", rerun);
			LOG.info("{} candidates to analyse {}", candidatesToAnalyze.size(), candidatesToAnalyze);
			LOG.info("runFailed {}", runFailed);
			LOG.info("Stopping execution now");
			System.exit(0);
		}
		
		if(cmd.hasOption("maxrun")) {
			try {
				int maxRun = Integer.parseInt(cmd.getOptionValue("maxrun"));
				Settings.MAX_RUN = maxRun;
			}catch(Exception ex) {
				LOG.error("Cannot parse integer value for maxrun option");
				System.exit(-1);
			}
		}
			
		
		// start docker
		TestCaseExecutor.restartDocker(subject, true);
		
		try {
			Thread.sleep(10000); // Wait after restarting docker
		} catch (InterruptedException e) {
			LOG.error("Error sleeping after docker restart {} ", e.getMessage());
		}
		long start = System.currentTimeMillis();
		
		MutationRunner runner = new MutationRunner(subject, candidatesToAnalyze, newSuite);
		
//		MutationRunner.mutationAnalysis(candidatesToAnalyze, subject, newSuite);
		Map records = runner.runMutationAnalysis();
		if(records == null) {
			LOG.error("Error running mutation analysis");
			exitCode = -1;
		}
		
		long end = System.currentTimeMillis();
		long mutAnalysis = end-start;

		LOG.info("Time taken for analysis : " + mutAnalysis + "ms");

		// stop docker
		TestCaseExecutor.restartDocker(subject, false);
		System.exit(exitCode);
	}

	
	


	public static HashMap<String, MutationRunResult> loadRunResult(SUBJECT subject2, String runId) {
		Path resultsFile = Paths.get(Settings.MUTATION_DIR, subject2.name(), runId, Settings.MUTATION_REPORT_DIR, Settings.MUTATION_RESULTS_FILE);
		return loadRunResult(resultsFile);
	}

	public static HashMap<String, MutationRunResult> loadRunResult(Path resultsFile) {
		Gson gson = new Gson();
		Type resultType = new TypeToken<HashMap<String, MutationRunResult>>(){}.getType();
		try {
			return gson.fromJson(new FileReader(resultsFile.toFile()), resultType);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			LOG.error("Could not load mutation records {}", resultsFile);
			e.printStackTrace();
			return null;
		}
	}

	public Map<MutationCandidateEx, MutationRunResult> runMutationAnalysis() {
		if(mutationCandidates == null) {
			mutationCandidates = MutationUtils.loadMutationMap(subject, newSuite);
			if(mutationCandidates == null) {
				LOG.error("Cannot run mutation analysis of {} for new suite : {}", subject.name(), newSuite);
				LOG.info("Check if you have valid mutation records at mutant/<subject>/record/");
				return null;
			}
		}
		Settings.mutationRun = true;
		Settings.USE_CRAWLJAX = false;
		
		Settings.executionDir = Paths.get(Settings.MUTATION_DIR , subject.name(), 
				MutationUtils.getRunDir(Paths.get(Settings.MUTATION_DIR, subject.name()).toFile())
				).toString();
	
		int maxRun = Settings.MAX_RUN;
		if(Settings.MAX_RUN > mutationCandidates.size()) {
			maxRun = mutationCandidates.size();
		}
		
		int candidatesRun = 0;
		
		for(MutationCandidateEx candidate : mutationCandidates) {
			if(candidate == null|| candidate.getRecord() == null || !candidate.getRecord().isSuccess()) {
				// skip unsuccessful mutations
				LOG.info("Skipping candidate as no mutation record found {}", candidate);
				continue;
			}
			
			candidatesRun += 1;
			
			MutationSession session = new MutationSession(candidate);
			Settings.outputDir = Paths.get(Settings.executionDir, candidate.toString()).toString();
		
			currentMutation = session;
			MutationRunResult result = mutationRun();
			mutationScoreMap.put(session.getCandidate(), result);
			
			if(candidatesRun >= maxRun) {
				LOG.info("Stopping analysis. Reached max mutants to analyze {}", candidatesRun);
				break;
			}
		}
		
		generateReport();
		return mutationScoreMap;
	}


	public boolean shouldMutate(String state) {
		
		Set<String> statesToMutate = currentMutation.getCandidate().getMutantMap().keySet();
		if(statesToMutate.contains(state)) {
			return true;
		}
		return false;
	}

	public MutationRecordEx applyMRToBrowserState(WebDriver d, MutationRecord record) {
		// Checking if mutation preserved 
		MutationRecordEx recordEx = null;
		try {
			d.findElement(By.xpath(record.getOriginalXpath()));
			recordEx = mutator.transferMutation(record, d); 
		}catch(Exception ex) {
			// Original xpath is invalid
			ex.printStackTrace();
			try {
				WebElement el = d.findElement(By.xpath(record.getMutatedXpath()));
				MutationUtils.LOG.info("Mutation already preserved {}", record.getMutatedXpath());
				recordEx = new MutationRecordEx(false, "Skipped : Mutation Already Exists");
				recordEx.setMutatedXpath(record.getMutatedXpath());
				recordEx.setOriginalXpath(record.getMutatedXpath());
				recordEx.setVisible(el.isDisplayed());
			}catch(Exception ex1) {
				MutationUtils.LOG.error("The mutation can't be performed ", ex1);
				recordEx = new MutationRecordEx(false, "Exception while finding target");
			}
		}
		return recordEx;
	}

	public void mutateBrowserState(WebDriver d, String state) {
		List<MutationRecord> records = currentMutation.getRecords(state);
//		WebMutator mutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
	
		for(MutationRecord record: records) {
			if(record == null || !record.isSuccess()) {
				MutationUtils.LOG.error("skipping mutation record {} for {}", record, state);
				continue;
			}
			MutationUtils.LOG.info("Mutating {} {} {}", state, record.getOperator(), record.getOriginalXpath());
			MutationRecordEx recordEx = applyMRToBrowserState(d, record);
			
			if(Settings.currResult !=null) {
				Settings.currResult.addMutationRecord(state, recordEx);
			}
		}
	}

	public void generateReport() {
		// Write a Json report first 
		MutationUtils.writeResultMap(mutationScoreMap, subject);

		/*String traceFolder = Paths.get(Settings.traceDir, subject.name(), Settings.REL_TRACE_LOC).toString();

		if(newSuite) {
			traceFolder = Paths.get(Settings.traceDir, subject.name()+"_new", Settings.REL_TRACE_LOC).toString();
		}*/
		
		String testSuiteName = subject.name();
		if(newSuite)
			testSuiteName += "_new";
				
		MutationReport report = new MutationReport(mutationScoreMap, subject, testSuiteName);
		report.generateReport();
	}



	public MutationRunResult mutationRun() {
	
			long start = System.currentTimeMillis();
//			Settings.currentMutation = session;
			Settings.USE_CRAWLJAX = false;
	//		Settings.outputDir = Paths.get(Settings.MUTATION_DIR , subject.name(), 
	//				getRunDir(Paths.get(Settings.MUTATION_DIR, subject.name()).toFile())
	//				).toString();
			File outputFolder = new File(Settings.outputDir);
			if(! outputFolder.exists()) {
				outputFolder.mkdirs();
			}
			Settings.currResult = new MutationRunResult(currentMutation.getCandidate(), Settings.outputDir);
			String testSuiteName = subject.name();
			if(newSuite)
				testSuiteName += "_new";
			Settings.currResult.testSuiteName = testSuiteName;
			currentMutation.setResult(Settings.currResult);
			try {
				Optional<Description> failure = UtilsRunner.executeTests(subject, newSuite);
	
				if(failure.isPresent()) {
					Settings.currResult.setFailed(true);
				}
			}catch(Exception ex) {
				Settings.currResult.setFailed(true);
				TestFailureDetail detail = new TestFailureDetail("overall", ex.toString(), ex.toString());
				Settings.currResult.addTestResult(detail);
			}
			long end = System.currentTimeMillis();
			Settings.currResult.setDuration(end-start);
			MutationUtils.writeResult(Settings.currResult, Paths.get(Settings.outputDir, "mutationResult.json").toString());
			return Settings.currResult;
		}
}
