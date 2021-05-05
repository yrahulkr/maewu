package com.saltlab.murun.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.saltlab.murun.runner.AnalyzeMutants.MutantClass;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.utils.BugLabels;
import com.saltlab.murun.utils.Settings;

public class AnalysisRecord {
	MutantClass mutantClass;
	String candidate;
	SUBJECT subject;
	boolean liveAnalysis;
	List<String> selectedCategories;
	List<String> selectedLabels;
	List<String> reqTestList;
	double severityScore = -1;
	double stubbornScore = -1;
	boolean killed;
	
	
	public boolean isKilled() {
		return killed;
	}
	public void setKilled(boolean killed) {
		this.killed = killed;
	}
	
	public double getSeverityScore() {
		return severityScore;
	}
	public void setSeverityScore(double severityScore) {
		this.severityScore = severityScore;
	}
	public double getStubbornScore() {
		return stubbornScore;
	}
	public void setStubbornScore(double stubbornScore) {
		this.stubbornScore = stubbornScore;
	}
	public List<String> getReqTestList() {
		return reqTestList;
	}
	public void setReqTestList(List<String> reqTestList) {
		this.reqTestList = reqTestList;
	}
	public List<String> getSelectedCategories() {
		return selectedCategories;
	}
	public void setSelectedCategories(List<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
	public List<String> getSelectedLabels() {
		return selectedLabels;
	}
	public void setSelectedLabels(List<String> selectedLabels) {
		this.selectedLabels = selectedLabels;
	}
	public MutantClass getMutantClass() {
		return mutantClass;
	}
	public void setMutantClass(MutantClass mutantClass) {
		this.mutantClass = mutantClass;
	}
	public String getCandidate() {
		return candidate;
	}
	public void setCandidate(String candidate) {
		this.candidate = candidate;
	}
	public SUBJECT getSubject() {
		return subject;
	}
	public void setSubject(SUBJECT subject) {
		this.subject = subject;
	}
	public boolean isLiveAnalysis() {
		return liveAnalysis;
	}
	public void setLiveAnalysis(boolean liveAnalysis) {
		this.liveAnalysis = liveAnalysis;
	}
	
	public double computeSeverityScore() {
		this.severityScore = new BugLabels(selectedLabels).getSeverity();
		return this.severityScore;
	}
	
	public double computeStubbornScore() {
		if(this.reqTestList==null){
			return stubbornScore;
		}
		for(String testAc: this.reqTestList) {
			switch(testAc.trim().toLowerCase()) {
			case "none":
				stubbornScore = stubbornScore < 0 ? 0: stubbornScore;
				break;
			case "find":
				stubbornScore = stubbornScore < 1 ? 1: stubbornScore;
				break;
			case "execute":
				stubbornScore = stubbornScore < 2 ? 2: stubbornScore;
				break;
			case "assert":
				stubbornScore = stubbornScore < 2 ? 2: stubbornScore;
				break;
			case "executeassertimmediate":
				stubbornScore = stubbornScore < 3 ? 3: stubbornScore;
				break;
			case "executeassertpropagated":
				stubbornScore = stubbornScore < 4 ? 4: stubbornScore;
				break;
			default:
				break;
			}
		}
		return stubbornScore;
	}
	
	@Override
	public String toString() {
		return mutantClass + ":" + liveAnalysis + " - " + candidate + "_" + subject ;
	}
	
	public static void main(String args[]) {
		SUBJECT subject = SUBJECT.addressbook;
		if(args.length==1) {
			subject = Stub.getSubject(args[0]);
		}
		String runFolder = "2";

		HashMap<String, MutationRunResult> runResults = MutationRunner.loadRunResult(subject, runFolder);
		

		List<AnalysisRecord> analysisRecords =null;
		

		Gson gson = new Gson();

		File analysisFile = Paths.get(Settings.MUTATION_DIR, subject.name(), runFolder, Settings.MUTATION_REPORT_DIR,
				Settings.MUTANT_ANALYSIS_FILE).toFile();

		if (analysisFile.exists()) {
			java.lang.reflect.Type recordListType = new TypeToken<List<AnalysisRecord>>() {
			}.getType();
			try {
				analysisRecords = (List<AnalysisRecord>) gson.fromJson(new FileReader(analysisFile), recordListType);
			} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(analysisRecords!=null) {
			/*backup first*/
			File analysisBackup = Paths.get(Settings.MUTATION_DIR, subject.name(), runFolder, Settings.MUTATION_REPORT_DIR,
					Settings.MUTANT_ANALYSIS_FILE_BACKUP).toFile();
			try {
				List<AnalysisRecord> existing = null;
				if (analysisBackup.exists()) {
					java.lang.reflect.Type recordListType = new TypeToken<List<AnalysisRecord>>() {
					}.getType();
					existing = (List<AnalysisRecord>) gson.fromJson(new FileReader(analysisBackup), recordListType);
				} else {
					existing = new ArrayList<>();
				}
				existing.addAll(analysisRecords);
				FileWriter writer = new FileWriter(analysisBackup);
				writer.append(gson.toJson(existing));
				writer.flush();
				writer.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			/* Compute Scores*/
			for(AnalysisRecord record: analysisRecords) {
				if(record.mutantClass == null) {
					continue;
				}
//				if(record.getSeverityScore()==-1) {
					record.computeSeverityScore();
//				}
//				if(record.getStubbornScore()==-1) {
					record.computeStubbornScore();
//				}
				record.killed = runResults.get(record.candidate).isFailed();
			}
			
			/*Store files*/
			try {
				FileWriter writer = new FileWriter(analysisFile);
				writer.write(gson.toJson(analysisRecords));
				writer.flush();
				writer.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		
	}
}
