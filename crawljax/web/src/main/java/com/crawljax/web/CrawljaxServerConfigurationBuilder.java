package com.crawljax.web;

import java.io.File;

public class CrawljaxServerConfigurationBuilder {

	private int port;
	private File outputDir;

	public CrawljaxServerConfigurationBuilder() {
		port = 0;
		outputDir = new File("out");
	}

	public int getPort() {
		return port;
	}

	public CrawljaxServerConfigurationBuilder setPort(int port) {
		this.port = port;
		return this;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public CrawljaxServerConfigurationBuilder setOutputDir(File outputDir) {
		this.outputDir = outputDir;
		return this;
	}

}
