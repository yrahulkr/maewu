package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.saltlab.murun.runner.Stub.SUBJECT;

public class Properties {
    private static Properties ourInstance = null;
    public static String home_dir = System.getProperty("user.home");
    public static String file_separator = System.getProperty("file.separator");
    public static String javaHome = System.getProperty("java.home");
    public static String user_dir = System.getProperty("user.dir");
    public static String app_url;
    private java.util.Properties appProps;
    private String appPropertiesPath;
    SUBJECT subject = null;
	public static String wait = null;

    public static Properties getInstance() {
        return ourInstance;
    }

    public Properties(SUBJECT subject) {
//    	user_dir = absolutePath;
    	Path currentRelativePath = Paths.get("");
    	String currentDirectoryPath = currentRelativePath.toAbsolutePath().toString();

    	this.subject = subject;
    	initProperties(currentDirectoryPath);
    	ourInstance = this;
    }
    
//    public Properties() {
//        Path currentRelativePath = Paths.get("");
//        String currentDirectoryPath = currentRelativePath.toAbsolutePath().toString();
//        initProperties(currentDirectoryPath);
//    }

	private void initProperties(String currentDirectoryPath) {
		this.appPropertiesPath = currentDirectoryPath + "/src/main/resources/" + subject.name() + ".properties";
        try {
            FileInputStream fileInputStream = new FileInputStream(this.appPropertiesPath);
            this.appProps = new java.util.Properties();
            this.appProps.load(fileInputStream);
            this.loadAndCheckProperties();
            try{
                app_url = "http://localhost:"
                        + Integer.valueOf(this.getProperty("app_port")) + this.getProperty("landingURL");;
            } catch (NumberFormatException ex){
                throw new RuntimeException("Value " + this.getProperty("app_port") + " must be a number");
            }
            
            try {
				wait = this.getProperty("wait");
			} catch (Exception ex) {
				System.err.println("No property wait");
			}

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    private void loadAndCheckProperties(){
        String dbPort = this.getProperty("db_port");
        this.checkPropertyNotEmpty(dbPort);

        String appPort = this.getProperty("app_port");
        this.checkPropertyNotEmpty(appPort);

        String headlessBrowser = this.getProperty("headless_browser");
        this.checkPropertyNotEmpty(headlessBrowser);

        String numExecutionFlakyTestSuite = this.getProperty("num_execution_flaky_test_suite");
        this.checkPropertyNotEmpty(numExecutionFlakyTestSuite);
        this.checkPropertyIsNumber(numExecutionFlakyTestSuite);
    }

    private void checkPropertyNotEmpty(String property){
        if(property.isEmpty()) throw new IllegalArgumentException("Property " + property + " cannot be empty");
    }

    private void checkPropertyIsNumber(String property){
        try {
            Integer.valueOf(property);
        } catch (NumberFormatException ex){
            throw new IllegalArgumentException(ex);
        }
    }

    public String getProperty(String propertyName){
        String value = this.appProps.getProperty(propertyName);
        if(value == null) throw new IllegalStateException("getProperty: property with name " + propertyName + " does not exist in file " + this.appPropertiesPath);
        else return value;
    }

    public String getProperty(String propertyName, String defaultValue){
        return this.appProps.getProperty(propertyName, defaultValue);
    }
}
