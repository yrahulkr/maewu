package com.crawljax.web.model;

import java.util.ArrayList;
import java.util.List;

public class TestImages {
	public List<DiffImages> diffImages = new ArrayList<DiffImages>();
	public int testId;
	
	public List<DiffImages> getDiffImages() {
		return diffImages;
	}
	public void setDiffImages(List<DiffImages> diffImages) {
		this.diffImages = diffImages;
	}
	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
	}
}
