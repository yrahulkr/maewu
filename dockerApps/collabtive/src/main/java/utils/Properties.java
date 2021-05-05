package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Properties {
	private static Properties ourInstance = new Properties();

	public static String home_dir = System.getProperty("user.home");
	public static String file_separator = System.getProperty("file.separator");
	public static String javaHome = System.getProperty("java.home");
	public static String user_dir = System.getProperty("user.dir");
	public static String app_url;
	public static String wait;
	private java.util.Properties appProps;
	private String appPropertiesPath;

	public static Properties getInstance() {
		return ourInstance;
	}

	private Properties() {
		Path currentRelativePath = Paths.get("");
		String currentDirectoryPath = currentRelativePath.toAbsolutePath().toString();
		this.appPropertiesPath = currentDirectoryPath + "/src/main/resources/app.properties";
		try {
			FileInputStream fileInputStream = new FileInputStream(this.appPropertiesPath);
			this.appProps = new java.util.Properties();
			this.appProps.load(fileInputStream);
			this.loadAndCheckProperties();
			
			try {
				app_url = "http://localhost:" + Integer.valueOf(this.getProperty("app_port")) + "/collabtive/";
			} catch (NumberFormatException ex) {
				throw new RuntimeException("Value " + this.getProperty("app_port") + " must be a number");
			}
			
			try {
				wait = this.getProperty("wait");
			} catch (NumberFormatException ex) {
				throw new RuntimeException("Value " + this.getProperty("wait") + " must be a number");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadAndCheckProperties() {
		String dbPort = this.getProperty("db_port");
		this.checkPropertyNotEmpty(dbPort);

		String appPort = this.getProperty("app_port");
		this.checkPropertyNotEmpty(appPort);

		String headlessBrowser = this.getProperty("headless_browser");
		this.checkPropertyNotEmpty(headlessBrowser);

		String numExecutionFlakyTestSuite = this.getProperty("num_execution_flaky_test_suite");
		this.checkPropertyNotEmpty(numExecutionFlakyTestSuite);
		this.checkPropertyIsNumber(numExecutionFlakyTestSuite);

		String waitValue = this.getProperty("wait");
		this.checkPropertyNotEmpty(waitValue);
		this.checkPropertyIsLongNumber(waitValue);
	}

	private void checkPropertyNotEmpty(String property) {
		if (property.isEmpty())
			throw new IllegalArgumentException("Property " + property + " cannot be empty");
	}

	private void checkPropertyIsNumber(String property) {
		try {
			Integer.valueOf(property);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
	
	private void checkPropertyIsLongNumber(String property) {
		try {
			Long.valueOf(property);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public String getProperty(String propertyName) {
		String value = this.appProps.getProperty(propertyName);
		if (value == null)
			throw new IllegalStateException("getProperty: property with name " + propertyName
					+ " does not exist in file " + this.appPropertiesPath);
		else
			return value;
	}

	public String getProperty(String propertyName, String defaultValue) {
		return this.appProps.getProperty(propertyName, defaultValue);
	}
}
