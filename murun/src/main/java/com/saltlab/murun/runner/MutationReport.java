package com.saltlab.murun.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.utils.Settings;
import com.saltlab.webmutator.NodeProperties;
import com.saltlab.webmutator.operators.dom.MutationData;

/**
 * Generates a report for the mutation analysis. Contains HTML page showing
 * overall mutation score. Each individual mutation can be examined. Shows the
 * captured state and coverage of affected element etc.
 */

public class MutationReport {

	public static String TEMPLATE = "src/main/resources/MutationReport.html.vm";

	private Map<MutationCandidateEx, MutationRunResult> scoreMap;
	private String subject;
	private final VelocityEngine ve;

	private String testSuiteName;

	private void configureVelocity() {
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");
		// ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	}

	public MutationReport(Map<MutationCandidateEx, MutationRunResult> mutationScoreMap, SUBJECT subject,
			String testSuiteName) {
		this.scoreMap = mutationScoreMap;
		this.subject = subject.name();
		this.testSuiteName = testSuiteName;
		ve = new VelocityEngine();
		configureVelocity();
	}

	public void writeReport(Template template, VelocityContext context) {
		// File reportFile = Paths.get(Settings.MUTATION_DIR, subject,
		// Settings.MUTATION_RECORD_DIR, Settings.MUTATION_REPORT_FILE).toFile();
		File reportDir = Paths.get(Settings.executionDir, Settings.MUTATION_REPORT_DIR).toFile();
		if (!reportDir.exists()) {
			reportDir.mkdirs();
		}

		File reportFile = Paths.get(Settings.executionDir, Settings.MUTATION_REPORT_DIR, Settings.MUTATION_REPORT_FILE)
				.toFile();

		try {
			FileWriter writer = new FileWriter(reportFile);
			template.merge(context, writer);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Object formatData(Object data) {
		if (data == null || !(data instanceof String))
			return data;

		String textToken = (String) data;
		String returnToken = textToken.trim().replace("\"", "'").replace('{', '<').replace('}', '>').replace('[', '<')
				.replace(']', '>').replace("\n", "").replace("\r", "");
		return returnToken;
	}

	public JsonSerializer MutationDataAdapter = new JsonSerializer<MutationData>() {
		@Override
		public JsonElement serialize(MutationData src, Type typeOfSrc, JsonSerializationContext context) {
			MutationData newSrc = new MutationData(src.getKey(), formatData(src.getValue()));
			JsonObject element = new JsonObject();
			element.addProperty("key", newSrc.getKey());
			element.add("value", context.serialize(newSrc.getValue()));
			return element;
		}

	};

	public JsonSerializer NodePropertiesAdapter = new JsonSerializer<NodeProperties>() {

		@Override
		public JsonElement serialize(NodeProperties src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject element = new JsonObject();
			element.addProperty("display", src.getDisplay());
			element.add("position", context.serialize(src.getPosition()));
			element.addProperty("eventListener", (String) formatData(src.getEventListener()));
			return element;
		}
	};

	public String getGsonForScoreMap() {

		Gson gson = (new GsonBuilder()).registerTypeAdapter(MutationData.class, MutationDataAdapter)
				.registerTypeAdapter(NodeProperties.class, NodePropertiesAdapter).create();

		String pureGson = new Gson().toJson(scoreMap);
		String escaped = StringEscapeUtils.escapeJavaScript(pureGson);
		return escaped;

	}

	public void generateReport() {
		// Velocity.init("src/main/resources/velocity.properties");

		Template template = ve.getTemplate(TEMPLATE);
		VelocityContext context = new VelocityContext();
		context.put("subject", subject);
		context.put("testSuiteName", testSuiteName);
		context.put("diff_json", getGsonForScoreMap());
		writeReport(template, context);
	}

	public static void main(String args[]) {
		if(args.length<3) {
			System.out.println("Usage - subject, testsuitename, runResultsJson");
		}
		HashMap<String, MutationRunResult> resultMap = MutationRunner.loadRunResult(Paths.get(args[2]));
		HashMap<MutationCandidateEx, MutationRunResult> resultMap2 = new HashMap<>();
		for(MutationRunResult result: resultMap.values()) {
			resultMap2.put(result.candidate, result);
		}
		SUBJECT subject = Stub.getSubject(args[0]);
		String testSuiteName = args[1];
		MutationReport report = new MutationReport(resultMap2, subject, testSuiteName);
		report.generateReport();
	}
	
}
