package com.crawljax.web.model;

import java.io.File;
import java.util.List;

import com.crawljax.web.fs.WorkDirManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TestRecords {
	private final List<TestRecord> testList;
	private final WorkDirManager workDirManager;
	private int identity = 0;

	@Inject
	public TestRecords(WorkDirManager workDirManager) {
		this.workDirManager = workDirManager;
		testList = this.workDirManager.loadTestRecords();
		
		if (testList.size() > 0)
			identity = testList.get(0).getId();
	}

	/**
	 * @return the testRecords
	 */
	public List<TestRecord> getTestList() {
		return testList;
	}

	public TestRecord findByID(int id) {
		TestRecord record = null;
		for (TestRecord r : testList) {
			if (r.getId() == id) {
				record = r;
				break;
			}
		}
		return record;
	}

	public TestRecord add(String currentCrawlPath) {
		File testSrcFile = new File(currentCrawlPath + File.separatorChar + "plugins/test-gen/src/test/java/generated/GeneratedTests.java");
		TestRecord r = new TestRecord();
		r.setTestSrcPath(testSrcFile.getAbsolutePath());
		r.setId(++identity);
		testList.add(0, r);
		workDirManager.saveTestRecord(r);

		return r;
	}

	public TestRecord update(TestRecord record) {
		// assuming we are not updating from client side and can use same reference
		workDirManager.saveTestRecord(record);
		return record;
	}
	
	/*
	 * reset the test record in the memory
	 */
	public void setTestRecord(TestRecord record) {
		int index = 0;
		for(TestRecord testRecord : testList) {
			if(testRecord.getId() == record.getId())
				break;
			index ++;
		}
		
		testList.set(index, record);
	}
	
	public TestRecord reloadTestRecord(TestRecord record) {
		TestRecord reloaded = workDirManager.loadTestRecord(new File(record.getOutputFolder(),"test.json"));
		setTestRecord(reloaded);
		return reloaded;
	}
}
