package com.crawljax.web.fs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.web.di.CrawljaxWebModule.OutputFolder;
import com.crawljax.web.model.Configuration;
import com.crawljax.web.model.CrawlRecord;
import com.crawljax.web.model.CrawlRecord.CrawlStatusType;
import com.crawljax.web.model.TestRecord;
import com.crawljax.web.model.TestRecord.TestStatusType;

@Singleton
public class WorkDirManager {

	private static final Logger LOG = LoggerFactory.getLogger(WorkDirManager.class);

	private final File configFolder;
	private final File recordFolder;
	private final File testFolder;
	private final ObjectMapper mapper;

	@Inject
	public WorkDirManager(@OutputFolder File outputFolder, ObjectMapper mapper) {
		LOG.debug("Initiating the Workdir manager.");
		this.configFolder = new File(outputFolder, "configurations");
		this.recordFolder = new File(outputFolder, "crawl-records");
		this.testFolder = new File(outputFolder, "test-records");
		this.mapper = mapper;
		if (!this.configFolder.exists())
			this.configFolder.mkdirs();
		if (!this.recordFolder.exists())
			this.recordFolder.mkdirs();
		if (!this.testFolder.exists())
			this.testFolder.mkdirs();
	}

	public Map<String, Configuration> loadConfigurations() {
		Map<String, Configuration> configs = new ConcurrentHashMap<>();
		File[] configFiles = configFolder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		});
		for (File f : configFiles) {
			String id = f.getName().substring(0, f.getName().indexOf(".json"));
			Configuration c = loadConfiguration(id);
			configs.put(c.getId(), c);
		}
		return configs;
	}

	public Configuration loadConfiguration(String id) {
		Configuration config = null;
		File configFile = new File(configFolder, id + ".json");
		try {
			config = mapper.readValue(configFile, Configuration.class);
		} catch (IOException e) {
			LOG.error("Could not load config {}", configFile.getName());
		}
		return config;
	}

	public void saveConfiguration(Configuration config) {
		File configFile = new File(configFolder, config.getId() + ".json");
		try {
			if (!configFile.exists()) {
				configFile.createNewFile();
			}
			mapper.writeValue(configFile, config);
		} catch (IOException e) {
			LOG.error("Could not save config {}", config);
		}
	}

	public void deleteConfiguration(Configuration config) {
		File configFile = new File(configFolder, config.getId() + ".json");
		try {
			configFile.delete();
		} catch (Exception e) {
			LOG.error("Could not delete config {}", config);
		}
	}

	public List<CrawlRecord> loadCrawlRecords() {
		List<CrawlRecord> records = new ArrayList<CrawlRecord>();
		File[] recordFiles = recordFolder.listFiles();
		for (File f : recordFiles) {
			if (f.isDirectory()) {
				File record = new File(f, "crawl.json");
				if (record.exists()) {
					CrawlRecord c = loadCrawlRecord(record);

					// clean up records that crashed unexpectedly
					if (c.getCrawlStatus() != CrawlStatusType.success
					        && c.getCrawlStatus() != CrawlStatusType.failure)
						c.setCrawlStatus(CrawlStatusType.failure);

					int length = records.size();
					if (length > 0) {
						for (int i = 0; i < length; i++) {
							if (records.get(i).getId() < c.getId()) {
								records.add(i, c);
								break;
							}
						}
					} else
						records.add(c);
				}
			}
		}
		return records;
	}

	private CrawlRecord loadCrawlRecord(File recordFile) {
		CrawlRecord record = null;
		try {
			record = mapper.readValue(recordFile, CrawlRecord.class);
		} catch (IOException e) {
			LOG.error("Could not load record {}", recordFile.getName());
		}
		return record;
	}

	public void saveCrawlRecord(CrawlRecord record) {
		File recordFile =
		        new File(recordFolder, Integer.toString(record.getId()) + File.separatorChar
		                + "crawl.json");
		try {
			if (!recordFile.exists()) {
				recordFile.getParentFile().mkdirs();
				recordFile.createNewFile();
				record.setOutputFolder(recordFile.getParent());
			}
			mapper.writeValue(recordFile, record);
		} catch (IOException e) {
			LOG.error("Could not save crawl record {}", record);
		}
	}

	public void saveTestRecord(TestRecord record) {
		File recordFile =
		        new File(testFolder, Integer.toString(record.getId()) + File.separatorChar
		                + "test.json");
		try {
			if (!recordFile.exists()) {
				recordFile.getParentFile().mkdirs();
				recordFile.createNewFile();
				record.setOutputFolder(recordFile.getParent());
			}
			mapper.writeValue(recordFile, record);
		} catch (IOException e) {
			LOG.error("Could not save crawl record {}", record);
		}
	}

	public List<TestRecord> loadTestRecords() {
		List<TestRecord> testRecords = new ArrayList<TestRecord>();
		File[] testFiles = testFolder.listFiles();
		for (File f : testFiles) {
			if (f.isDirectory()) {
				File record = new File(f, "test.json");
				if (record.exists()) {
					TestRecord testRecord = loadTestRecord(record);

					// clean up records that crashed unexpectedly
					if (testRecord.getTestStatus() != TestStatusType.success
					        && testRecord.getTestStatus() != TestStatusType.failure)
						testRecord.setTestStatus(TestStatusType.failure);
					
					
					int length = testRecords.size();
					if (length > 0) {
						for (int i = 0; i < length; i++) {
							if (testRecords.get(i).getId() < testRecord.getId()) {
								testRecords.add(i, testRecord);
								break;
							}
						}
					} else
						testRecords.add(testRecord);
				}
			}
		}
		
		return testRecords;

	}
	private static void writeThumbNail(File target, BufferedImage screenshot) throws IOException {
		int THUMBNAIL_WIDTH = 100;
		int THUMBNAIL_HEIGHT = 100;
		BufferedImage resizedImage =
		        new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(screenshot, 0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, Color.WHITE, null);
		g.dispose();
		ImageIO.write(resizedImage, "JPEG", target);
	}

	
	
	public TestRecord loadTestRecord(File testFile) {
		TestRecord testRecord = null;
		try {
			testRecord = mapper.readValue(testFile, TestRecord.class);
		} catch (IOException e) {
			LOG.error("Could not load test {}", testFile.getName());
		}
		return testRecord;
	}

}
