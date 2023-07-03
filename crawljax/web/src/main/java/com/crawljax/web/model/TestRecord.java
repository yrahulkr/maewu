package com.crawljax.web.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestRecord {
	private int id;
	private Date createTime;
	private Date startTime;
	private long duration;
	private String testSrcPath;
	private String outputFolder;
	private TestStatusType testStatus = TestStatusType.idle;
	private List<TestImageDiff> diffs = new ArrayList<TestImageDiff>();

	public enum TestStatusType {
		idle, queued, initializing, running, success, failure
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * return the path to the test's generated source code
	 */
	public String getTestSrcPath() {
		return testSrcPath;
	}

	/**
	 * @param testSrcPath
	 *            the path to the test's generated source code
	 */
	public void setTestSrcPath(String testSrcPath) {
		this.testSrcPath = testSrcPath;
	}

	/**
	 * @return the outputFolder
	 */
	public String getOutputFolder() {
		return outputFolder;
	}

	/**
	 * @param outputFolder
	 *            the outputFolder to set
	 */
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	/**
	 * @return the crawlStatus
	 */
	public TestStatusType getTestStatus() {
		return testStatus;
	}

	/**
	 * @param testStatus
	 *            the test status to set
	 */
	public void setTestStatus(TestStatusType testStatus) {
		this.testStatus = testStatus;
	}

	public List<TestImageDiff> getDiffs() {
		return diffs;
	}

	public void setDiffs(List<TestImageDiff> diffs) {
		this.diffs = diffs;
	}

	public void addDiff(TestImageDiff diff) {
		this.diffs.add(diff);
	}
}
