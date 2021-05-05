package com.saltlab.murun.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.saltlab.murun.runner.MutantGenerator;
import com.saltlab.murun.runner.MutantGenerator.GENMODE;
import com.saltlab.murun.runner.MutationCandidateEx;
import com.saltlab.murun.runner.MutationRunResult;
import com.saltlab.murun.runner.MutationRunner;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.runner.TraceSession;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.operators.dom.MutationData;

public class MutationUtils {
	public static final Logger LOG = LoggerFactory.getLogger(MutationUtils.class);

	public static List<MutationCandidateEx> loadMutationMap(SUBJECT subject, boolean newSuite) {
		/*
		Gson gson = (new GsonBuilder())
		        .registerTypeAdapter(ImmutableMap.class, new GsonUtils.ImmutableMapDeserializer())
		        .registerTypeAdapter(ImmutableList.class, new GsonUtils.ImmutableListDeserializer())
		        .create();
		*/
		Gson gson = new Gson();
		String recordsFile = Settings.MUTATION_RECORDS_FILE;
		if(newSuite) {
			recordsFile = Settings.MUTATION_RECORDS_FILE_ADAPTED;
		}
		
		Path mutRecordsJson = Paths.get(Settings.MUTATION_DIR, subject.name(), Settings.MUTATION_RECORD_DIR, recordsFile);
	//	File mutRecordsJson = new File(Settings.outputDirSettings.MUTATION_DIR)
		Type recordMapType = new TypeToken<List<MutationCandidateEx>>(){}.getType();
		try {
			Settings.mutationRecords = gson.fromJson(new FileReader(mutRecordsJson.toFile()), recordMapType );
			deserializeMutationData(Settings.mutationRecords);
			return Settings.mutationRecords;
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			LOG.error("Could not load mutation records {}", mutRecordsJson);
			return null;
		}
		
	}
	

	public static void fixMutationRecordGson(MutationRecord recordToFix) {
		Object data = recordToFix.getData();
		if (data == null) {
			return;
		}
//		System.out.println(data.getClass());
		if(data instanceof LinkedTreeMap) {
			LinkedTreeMap data1 = (LinkedTreeMap) data;
			/*Set<String> keyset = ((LinkedTreeMap)data).keySet();
			for(String key: keyset) {
				System.out.println(key);
				System.out.println(((LinkedTreeMap) data).get(key));
			}*/
//			System.out.println(data1.get("key") + ":" + data1.get("value"));
			recordToFix.setData(new MutationData((String)data1.get("key"), data1.get("value")));
		}
	}
	
	public static void deserializeMutationData(List<MutationCandidateEx> mutationRecords) {
		for (MutationCandidateEx candidate : mutationRecords) {

			MutationRecord recordToFix = candidate.getRecord();
			fixMutationRecordGson(recordToFix);
			
			
			for(List<MutationRecord> recordListPerState: candidate.getMutantMap().values()) {
				for(MutationRecord record: recordListPerState) {
					fixMutationRecordGson(record);
				}
			}
		}
	}

	public static void loadBins(SUBJECT subject) {
		Gson gson = new Gson();
		Path binsJson = Paths.get(Settings.MUTATION_DIR, subject.name(), Settings.MUTATION_RECORD_DIR, Settings.BINS_FILE);
	//	File mutRecordsJson = new File(Settings.outputDirSettings.MUTATION_DIR)
		Type recordMapType = new TypeToken<Map<String, List<String>>>(){}.getType();
		try {
			Settings.bins = gson.fromJson(new FileReader(binsJson.toFile()), recordMapType );
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			LOG.error("Could not load Bins {}", binsJson);
			return;
		}
	}
	
	public static String getRunDir(File parentDir) {
		FileFilter dirFilter = new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		};
		int size = parentDir.listFiles(dirFilter).length;
		return "" + size;
	}

	
	public static void writeResult(MutationRunResult currResult, String outputDir) {
		try {
			FileOutputStream out = new FileOutputStream(new File(outputDir));
			out.write(new Gson().toJson(currResult).getBytes());
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeResultMap(Map<MutationCandidateEx, MutationRunResult> mutationScoreMap, SUBJECT subject) {
//		File resultsJson = Paths.get(Settings.MUTATION_DIR, subject.name(), Settings.MUTATION_RECORD_DIR, Settings.MUTATION_RESULTS_FILE).toFile();
		File reportDir =  Paths.get(Settings.executionDir, Settings.MUTATION_REPORT_DIR).toFile();
		if(!reportDir.exists()) {
			reportDir.mkdirs();
		}
		File resultsJson = Paths.get(Settings.executionDir, Settings.MUTATION_REPORT_DIR, Settings.MUTATION_RESULTS_FILE).toFile();
		
		try {
			FileOutputStream out = new FileOutputStream(resultsJson);
			out.write(new Gson().toJson(mutationScoreMap).getBytes());
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getBin(String state) {
		String returnBin = null;
		for(String bin: Settings.bins.keySet()) {
			List<String> statesToMutate = Settings.bins.get(bin);
			if(statesToMutate.contains(state)) {
				returnBin = bin;
				break;
			}
		}
		return returnBin;
	}

	
	
	public static List<MutationCandidateEx> mutateFragmentedTrace(TraceSession trace, boolean overwrite, SUBJECT subject) {
		if(trace == null) {
			LOG.error("No trace session provided");
			return null;
		}
		
		File mutationFolder = Paths.get(Settings.MUTATION_DIR, subject.name(), Settings.MUTATION_RECORD_DIR).toFile();
		
		File mutationRecordsFile = new File(mutationFolder, Settings.MUTATION_RECORDS_FILE);
		if (mutationRecordsFile.exists() && !overwrite) {
			LOG.info("Mutation already exists!! {} . No overwrite!! ", mutationFolder.getAbsolutePath());
			return null;
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
			MutantGenerator mutGen = new MutantGenerator(trace, subject, GENMODE.NEW);
			List<MutationCandidateEx> mutationRecords = mutGen.mutateTrace();
			writeMutationRecords(subject, mutationRecords, false);
			return mutationRecords;
		} catch(Exception ex){
			ex.printStackTrace();
			LOG.error("Exception creating mutation records");
		}finally {
			
		}
		return null;
	}

	
	public static void writeMutationCandidates(SUBJECT subject, List<MutationCandidateEx> candidates) {
		File mutationFolder = Paths.get(Settings.traceDir, subject.name(), Settings.MUTATION_RECORD_DIR).toFile();
		if(!mutationFolder.exists()) {
			mutationFolder.mkdirs();
		}
		File recordsFile = new File(mutationFolder, Settings.CANDIDATES_FILE);
		writeRecordsToFile(candidates, recordsFile);
	}


	public static void writeRecordsToFile(List<MutationCandidateEx> candidates, File recordsFile) {
		FileOutputStream fmR;
		try {
			fmR = new FileOutputStream(recordsFile.getAbsolutePath());
			fmR.write((new Gson()).toJson(candidates).getBytes());
			fmR.close();
			LOG.info("mutation Candidates written to : " + recordsFile.getAbsolutePath());
		} catch (IOException e) {
			LOG.error("Error writing Mutation Candidates {}", recordsFile.toPath());
		}
	}
	
	public static void writeMutationRecords_random(SUBJECT subject, List<MutationCandidateEx> mutationRecords) {
		File mutationFolder = Paths.get(Settings.MUTATION_DIR, subject.name(), Settings.MUTATION_RECORD_DIR).toFile();
		if(!mutationFolder.exists()) {
			mutationFolder.mkdirs();
		}
		
		File recordsFile = new File(mutationFolder, Settings.MUTATION_RECORDS_FILE_RANDOM);
		
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
	
	public static void writeMutationRecords(SUBJECT subject, List<MutationCandidateEx> mutationRecords, boolean adapted) {
		File mutationFolder = Paths.get(Settings.MUTATION_DIR, subject.name(), Settings.MUTATION_RECORD_DIR).toFile();
		if(!mutationFolder.exists()) {
			mutationFolder.mkdirs();
		}
		
		File recordsFile = new File(mutationFolder, Settings.MUTATION_RECORDS_FILE);
		if(adapted) {
			recordsFile = new File(mutationFolder, Settings.MUTATION_RECORDS_FILE_ADAPTED);
		}
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
	
	/*static Map<String, MutationRecord> mutateStates(File mutationFolder, Map<String, String> stateFileMap, Map<String, List<String>> bins) {
	
	Map<String, MutationRecord> mutationRecords = new HashMap<>();
	WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
	

	for (String bin : bins.keySet()) {
		
		List<Path> domFiles = null;
		
		Path domFile = Paths.get(stateFileMap.get(bins.get(bin).get(0)) + ".html"); 
		
		String dom = null;
		try {
			dom = DomUtils.getStrippedDom(FileUtils.readFileToString(domFile.toFile()));
		} catch (IOException e1) {
			Stub.LOG.error("Error getting dom {}", domFile);
			continue;
		}
		MutationRecord record = webMutator.mutateDomString(dom);
		if (record.isSuccess()) {
			String mutantName = bin;

			mutationRecords.put(mutantName, record);
			Path toWrite = Paths.get(mutationFolder.getAbsolutePath(),
					bins.get(bin).get(0) + ".html");
			try {
				DomUtils.writeDocumentToFile(record.getDoc(), toWrite.toAbsolutePath().toString(),
						"html", 2);
			} catch (TransformerException|IOException e) {
				Stub.LOG.error("Error writing {} {}", toWrite, e.getMessage());
			} 
		}

	}
	return mutationRecords;
}*/

/*static void findBins(File mutationFolder, List<File> testFolders, Map<String, String> stateFileMap,
		Map<String, List<String>> bins) {
	for (File testFolder : testFolders) {
		List<Path> domFiles = null;
		try {
			domFiles = Files.list(testFolder.toPath()).filter(
					path -> FilenameUtils.getExtension(path.getFileName().toString()).equalsIgnoreCase("html"))
					.collect(Collectors.<Path>toList());
			} catch (IOException e) {
				Stub.LOG.error("Error finding DOM files in {}", testFolder);
				Stub.LOG.error("Error finding DOM files {}", e.getMessage());
				continue;
			} 
		for (Path domFile : domFiles) {
			System.out.println(domFile);

			// Check if its same as any other mutated state
			String stateName = testFolder.getName() + "_" +FilenameUtils.removeExtension(domFile.getFileName().toString());
			stateFileMap.put(stateName, FilenameUtils.removeExtension(domFile.toString()));
			MutationSession.findBin(stateName, stateFileMap, bins);
			
		}
	}
	Path binsFile = Paths.get(mutationFolder.toPath().toString(), Settings.BINS_FILE);
	try {
		FileOutputStream stream = new FileOutputStream(binsFile.toFile());
		stream.write((new Gson()).toJson(bins).getBytes());
		stream.flush();
		stream.close();
		
	} catch (IOException e) {
		Stub.LOG.error("Error writing bins {} \n {}", binsFile, e);
	}

	Path stateMapFile = Paths.get(mutationFolder.toPath().toString(), Settings.STATE_MAP_FILE);
	try {
		FileOutputStream stream = new FileOutputStream(stateMapFile.toFile());
		stream.write((new Gson()).toJson(stateFileMap).getBytes());
		stream.flush();
		stream.close();
	} catch (IOException e) {
		Stub.LOG.error("Error writing bins {} \n {}", stateMapFile, e);
	}
}*/

/*static void findBin(String stateName, Map<String, String> stateFileMap, Map<String, List<String>> bins) {
	String stateFile = stateFileMap.get(stateName);
	for(String bin: bins.keySet()) {
		if(bins.get(bin) ==null || bins.get(bin).isEmpty()) {
			Stub.LOG.warn("{} has no states", bin);
			continue;
		}
		String toCompare = stateFileMap.get(bins.get(bin).get(0));
		boolean comp = MutationSession.isEquivalent(stateFile, toCompare);
		if(comp) {
			bins.get(bin).add(stateName);
			Stub.LOG.info("Found {} for {}", bin, stateName);
			return;
		}
	}
	
	String newBin = "" + bins.keySet().size();
	List<String> newList = new ArrayList<>();
	newList.add(stateName);
	bins.put(newBin, newList);
	Stub.LOG.info("Create new bin {} for {}", newBin, stateName);
}*/

/*static boolean isEquivalent(String stateFile, String toCompare) {
	String dom1 = null, dom2= null;
	try {
		dom1 = DomUtils.getStrippedDom(FileUtils.readFileToString(new File(stateFile+".html")));
		dom2 = DomUtils.getStrippedDom(FileUtils.readFileToString(new File(toCompare+".html")));
	} catch (IOException e1) {
		Stub.LOG.error("Error getting dom {}", stateFile, toCompare);
		return false;
	}
	
	dom1 = DomUtils.getDomWithoutHead(dom1);
	dom2 = DomUtils.getDomWithoutHead(dom2);
	if(dom1.equals(dom2)) {
		return true;
	}
	
	// Visual Comparison
	
	return false;
}*/
	
}
