package com.saltlab.murun;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;
import org.openqa.selenium.Rectangle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.saltlab.murun.runner.MutationCandidateEx;
import com.saltlab.murun.runner.MutationReport;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.utils.Settings;
import com.saltlab.webmutator.NodeProperties;
import com.saltlab.webmutator.operators.dom.MutationData;

public class TestGsonSerialization {
	
	
	@Test
	public void testMutationRecords() {
		SUBJECT subject = SUBJECT.dummy;
	
		Gson gson = new Gson();
		
		Path mutRecordsJson = Paths.get(Settings.MUTATION_DIR, subject.name(), Settings.MUTATION_RECORD_DIR,
				Settings.MUTATION_RECORDS_FILE);
		// File mutRecordsJson = new File(Settings.outputDirSettings.MUTATION_DIR)
		Type recordMapType = new TypeToken<List<MutationCandidateEx>>() {
		}.getType();
		try {
			List<MutationCandidateEx> mutationRecords = gson.fromJson(new FileReader(mutRecordsJson.toFile()),
					recordMapType);
			for (MutationCandidateEx candidate : mutationRecords) {

				Object data = candidate.getRecord().getData();
				if (data == null) {
					continue;
				}
//				System.out.println(data.getClass());
				if(data instanceof LinkedTreeMap) {
					LinkedTreeMap data1 = (LinkedTreeMap) data;
					/*Set<String> keyset = ((LinkedTreeMap)data).keySet();
					for(String key: keyset) {
						System.out.println(key);
						System.out.println(((LinkedTreeMap) data).get(key));
					}*/
//					System.out.println(data1.get("key") + ":" + data1.get("value"));
					if(data1.get("value") instanceof LinkedTreeMap) {
						LinkedTreeMap data11 = (LinkedTreeMap) data1.get("value");
						System.out.println(data11.keySet());
						System.out.println(data11.get("x") + ":" + data11.get("y"));
						
						new Point(((Double)data11.get("x")).intValue(), ((Double)data11.get("y")).intValue());
					}
				}
			}
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			System.out.println("Could not load mutation records {}" + mutRecordsJson);
		}

	}
	
	@Test
	public void TestGsonReportAdapters() {
		MutationData data =new MutationData("test", "\"test}");
		NodeProperties props = new NodeProperties("true", new java.awt.Rectangle(0, 0,0,0), "function onclick(event) {DeleteSel()}");
		MutationReport report = new MutationReport(null, SUBJECT.dummy, null);
		Gson gson = (new GsonBuilder())
		        .registerTypeAdapter(MutationData.class, report.MutationDataAdapter)
		        .registerTypeAdapter(NodeProperties.class, report.NodePropertiesAdapter)
		        .create();
		
		Gson gson2 = new Gson();
		
		String datajsonString1 = gson.toJson(data);
		String dataJsonString2 = gson2.toJson(data);
		System.out.println(gson.toJson(data));
		System.out.println(gson2.toJson(data));
//		System.out.println(gson.fromJson(datajsonString1, MutationData.class));
//		System.out.println(gson.fromJson(dataJsonString2, MutationData.class));
//		
		System.out.println(StringEscapeUtils.escapeJavaScript(datajsonString1));
		System.out.println(StringEscapeUtils.escapeJavaScript(dataJsonString2));

	}
}
