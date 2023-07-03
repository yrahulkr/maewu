package com.saltlab.murun.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.saltlab.murun.runner.MutationCandidateEx;
import com.saltlab.murun.runner.MutationRunResult;

public class Settings {

	public static final boolean VERBOSE = true;
	public static boolean aspectActive = true;
	public static String traceDir = "trace";
	public static final String REL_TRACE_LOC = Paths.get("crawls", "crawl0").toString();
	public static String outputDir = "";
	public static String executionDir = "";
	public static String MUTATION_DIR = "mutant";
	public static final String MUTATION_RECORD_DIR = "record";
	public static final String MUTATION_REPORT_DIR = "report";
	public static final String sep = File.separator;
	public static final int PIXEL_DENSITY = 2;
	public static final boolean OVERWRITE_MUTATION = false;
	public static final boolean OVERWRITE_TRACE = false;
	public static final String MUTATION_RECORDS_FILE = "mutationRecords.json";
	public static final String MUTATION_RECORDS_FILE_RANDOM = "mutationRecords_random.json";
	public static final String MUTATION_RECORDS_FILE_ADAPTED = "mutationRecords_new.json";
	public static final String CANDIDATES_FILE = "mutationCandidates.json";

	public static boolean mutationRun = false;
	public static boolean USE_CRAWLJAX = false;
	
//	public static MutationSession currentMutation = null;
	public static Map<String, List<String>> bins = null;
	public static List<MutationCandidateEx> mutationRecords = null;
	
	public static MutationRunResult currResult = null;
	
	public static final String DOCKER_LOCATION = "/Users/rahulkrishna/git/comparator-fork/dockerApps";
	public static final String DOCKER_SCRIPT = "run-docker.sh";
	public static final String MUTATION_RESULTS_FILE = "mutationResults.json";
	public static final String MUTATION_REPORT_FILE = "mutationReport.html";
	public static final String BINS_FILE = "bins.json";
	public static final String STATE_MAP_FILE = "stateFileMap.json";
	public static final String PAGE_LOAD_SCRIPT = 
//			null; 
			"HTMLElement.prototype.onEvent = HTMLElement.prototype.addEventListener; HTMLElement.prototype.addEventListener = function (eventType,callBack,useCapture) {this.onEvent(eventType, callBack, useCapture);if (!this.myListeners) {this.myListeners = [];};console.log(callBack);this.myListeners.push({ eType: eventType, callBack: callBack });return this;}; HTMLElement.prototype.removeListenerType = function(eventType){console.log(eventType); if (this.myListeners) {for (var i = 0; i < this.myListeners.length; i++) {if(this.myListeners[i].eType == eventType){console.log(this.myListeners[i].callBack);this.removeEventListener(this.myListeners[i].eType, this.myListeners[i].callBack);}};};};";
	
	
	public static String readObserverScript(){
		try {
			return FileUtils.readFileToString(new File("src/main/resources/observerScript.js"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "dummy";
		}
	}
	
	public static final String OBSERVER_SCRIPT = readObserverScript();
	public static final double SCORE_THRESHOLD = 2.0;
	public static final String MUTANT_ANALYSIS_FILE = "manualAnalysis.json";
	public static final String MUTANT_ANALYSIS_FILE_BACKUP = "manualAnalysis_backup.json";
	public static int MAX_MUTANTS = 100;
	public static int MAX_RUN=10;
	
	/* file extensions. */
	public static String PNG_EXT = ".png";
	public static String HTML_EXT = ".html";
	public static String JAVA_EXT = ".java";
	public static String JSON_EXT = ".json";
}
