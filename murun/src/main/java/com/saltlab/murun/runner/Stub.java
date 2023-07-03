package com.saltlab.murun.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.utils.MutationUtils;
import com.saltlab.murun.utils.Settings;
import com.saltlab.murun.utils.TraceUtils;

import utils.TestCaseExecutor;

public class Stub {

	static final Logger LOG = LoggerFactory.getLogger(Stub.class);
	public static SUBJECT subject = SUBJECT.claroline;

	public enum SUBJECT {
		addressbook, mantisbt, mrbs, ppma, collabtive, claroline, dummy
	}

	public static void printUsage() {
		System.err.println("Usage : <subject : (addressbook, mantisbt, mrbs, ppma, collabtive, claroline)>");
	}

	public static void main(String args[]) {
		
		if(args.length!=0) {
			subject = getSubject(args[0]);
		}
		
		// start docker
		TestCaseExecutor.restartDocker(subject, true);
		
		try {
			Thread.sleep(10000); // Wait after restarting docker
		} catch (InterruptedException e) {
			LOG.error("Error sleeping after docker restart {} ", e.getMessage());
		}
		long start = System.currentTimeMillis();
		TraceSession session = TraceUtils.collectTrace(Settings.OVERWRITE_TRACE, subject);
		long end = System.currentTimeMillis();
		long traceGen = end-start;
		
		
		start = System.currentTimeMillis();
		List<MutationCandidateEx> mutationRecords = null;
		if(session!=null) {
			// Trace session available. Mutate it!!
			mutationRecords = MutationUtils.mutateFragmentedTrace(session, Settings.OVERWRITE_MUTATION, subject);
		}
		end = System.currentTimeMillis();
		long mutGen = end-start;

//
		end = System.currentTimeMillis();
		boolean newSuite = false;
		
		MutationRunner runner = new MutationRunner(subject, mutationRecords, newSuite);
		runner.runMutationAnalysis();
		
		end = System.currentTimeMillis();
		long mutAnalysis = end-start;
//		
		LOG.info("Time taken for trace generation : " + traceGen + "ms");
		LOG.info("Time taken for trace generation : " + mutGen + "ms");
		LOG.info("Time taken for analysis : " + mutAnalysis + "ms");

		// stop docker
		TestCaseExecutor.restartDocker(subject, false);
		System.exit(0);
	}

	
	
	
	
	/*private static void mutationAnalysis() {
		Settings.mutationRun = true;
		MutationUtils.loadMutationMap(subject);
		MutationUtils.loadBins(subject);
		if (Settings.mutationRecords == null) {
			LOG.error("No Available Mutations");
			return;
		}
		LOG.info("Available Mutations : {}", Settings.mutationRecords.size());
		
		File resultFile = Paths.get(Settings.MUTATION_DIR, subject.name(), Settings.MUTATION_RECORD_DIR, Settings.MUTATION_RESULTS_FILE).toFile();
		
		if(resultFile.exists()) {
			LOG.info("Analysis results already exists {}", resultFile.toPath());
			return;
		}
		
		Map<String, MutationRunResult> mutationScoreMap = new HashMap<>(); 
		for(String state: Settings.recordMap.keySet()) {
			long start = System.currentTimeMillis();
			Settings.currentMutation = state;
			Settings.outputDir = Paths.get(Settings.MUTATION_DIR , subject.name(), 
					getRunDir(Paths.get(Settings.MUTATION_DIR, subject.name()).toFile())
					).toString();
			Settings.currResult = new MutationRunResult(state, Settings.outputDir);
			try {
				Optional<Description> failure = executeTests();
				mutationScoreMap.put(state, Settings.currResult);
	
				if(failure.isPresent()) {
					Settings.currResult.setFailed(true);
				}
			}catch(Exception ex) {
				Settings.currResult.setFailed(true);
				Settings.currResult.addTestResult("overall", ex.toString());
			}
			long end = System.currentTimeMillis();
			Settings.currResult.setDuration(end-start);
			MutationUtils.writeResult(Settings.currResult, Paths.get(Settings.outputDir, "mutationResult.json").toString());
		}
		MutationUtils.writeResultMap(mutationScoreMap, subject);
	}
*/
	
	
	
//	private static void mutateTrace(TraceSession session) {
//		MutationSession mutationSession = new MutationSession();
//		mutationSession.mutateTrace(session);
//	}
	
/*	private static void mutateTrace(boolean overwrite) {
		File traceFolder = new File(Settings.traceDir, subject.name());
		File mutationFolder = Paths.get(Settings.MUTATION_DIR, subject.name(), Settings.MUTATION_RECORD_DIR).toFile();

		List<File> testFolders = new ArrayList<File>();

		
		File mutationRecordsFile = new File(mutationFolder, Settings.MUTATION_RECORDS_FILE);
		if (mutationRecordsFile.exists() && !overwrite) {
			LOG.info("Mutation already exists!! {} . No overwrite!! ", mutationFolder.getAbsolutePath());
			return;
		} else {
			
			if(mutationFolder.exists()) {
				LOG.info("Removing all files in {}", mutationFolder.toPath());
				String[]entries = mutationFolder.list();
				for(String s: entries){
				    File currentFile = new File(mutationFolder,s);
				    currentFile.delete();
				}
			}
			else {
				mutationFolder.mkdirs();
			}
		}

		try {
			Files.list(traceFolder.toPath()).filter(Files::isDirectory)
					.forEach(path -> testFolders.add(path.toFile()));
		} catch (IOException e) {
			LOG.error("Error finding test folders {}", e.getMessage());
		}
		
		Map<String, String> stateFileMap = new HashMap<>();
		Map<String, List<String>> bins = new HashMap<>();
		
		Map<String, MutationRecord> mutationRecords =	null;
		try {
			MutationSession.findBins(mutationFolder, testFolders, stateFileMap, bins);
			mutationRecords =	MutationSession.mutateStates(mutationFolder, stateFileMap, bins);
		} finally {
			File recordsFile = new File(mutationFolder, Settings.MUTATION_RECORDS_FILE);
			FileOutputStream fmR;
			try {
				fmR = new FileOutputStream(recordsFile.getAbsolutePath());
				fmR.write((new Gson()).toJson(mutationRecords).getBytes());
				fmR.close();
				LOG.info("mutation Records written to : " + recordsFile.getAbsolutePath());
			} catch (IOException e) {
				LOG.error("Error writing Mutation Record {}", recordsFile.toPath());
			}
		}
	}*/

	

	

	

	static SUBJECT getSubject(String arg) {
		SUBJECT subject = null;
		arg = arg.trim().toLowerCase();
		if (arg.equals("addressbook")) {
			subject = SUBJECT.addressbook;
		} else if (arg.equals("mantisbt")) {
			subject = SUBJECT.mantisbt;
		} else if (arg.equals("mrbs")) {
			subject = SUBJECT.mrbs;
		} else if (arg.equals("ppma")) {
			subject = SUBJECT.ppma;
		} else if (arg.equals("collabtive")) {
			subject = SUBJECT.collabtive;
		} else if (arg.equals("claroline")) {
			subject = SUBJECT.claroline;
		} else if (arg.equals("dummy")) {
			subject = SUBJECT.dummy;
		} else {
			System.err.println(
					"Unknown subject!! available : (addressbook, mantisbt, mrbs, ppma, collabtive, claroline)");
			subject = null;
		}
		return subject;
	}
}
