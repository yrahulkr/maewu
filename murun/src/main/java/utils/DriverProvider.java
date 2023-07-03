package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;

import com.saltlab.murun.runner.TraceSession;
import com.saltlab.murun.utils.Settings;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverProvider {

    private static DriverProvider ourInstance = new DriverProvider();
	

	private DriverProvider(){
        WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.silentOutput", "true");
        Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);
    }

    public static DriverProvider getInstance(){
        return ourInstance;
    }

    public WebDriver getDriver(){
    	ChromeOptions chromeOptions = new ChromeOptions();
        if(Boolean.valueOf(Properties.getInstance().getProperty("headless_browser"))){
        	chromeOptions.addArguments("--no-sandbox", "--headless", "--disable-gpu", "--window-size=1200x600");
        }
      
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        
        
        String pageLoadScript = null;
        if(Settings.mutationRun) {
        	pageLoadScript = Settings.PAGE_LOAD_SCRIPT;
        }
        else {
        	pageLoadScript = Settings.OBSERVER_SCRIPT;
        }
        
        if(pageLoadScript != null) {
        	Map<String, Object> parameters = new HashMap<>();
    		parameters.put("source", pageLoadScript);
            driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", parameters);
       }
        
        if(Settings.USE_CRAWLJAX) {
    		return TraceSession.getInstance().getDriver_Crawljax(driver);
    	}
        else {
        	return driver;
        }
    }
    
    /**
     * true for headless
     * @param b
     * @return
     */
    public WebDriver getDriver_noProps(boolean b){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox", "--disable-gpu", "--window-size=1200x600");

        if(b){
        	chromeOptions.addArguments("--headless");
        }
        return new ChromeDriver(chromeOptions);
    }

    
    
//    public WebDriver getDriver_Crawljax(boolean headless) {
//    	this.builder = CrawljaxConfiguration.builderFor("http://www.dummy.org");
//    	if(headless)
//    		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME_HEADLESS), new BrowserOptions(2));
//    	else
//    		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME),  new BrowserOptions(2));
//
//    	builder.setOutputDirectory(new File("testCrawljax"));
//    	builder.addPlugin(new CrawlOverview());
//    	builder.addPlugin(new FragmentationPlugin());
//    	this.config = builder.build();
//    	CoreModule core= new CoreModule(config);
//		this.context = Guice.createInjector(core).getInstance(CrawlerContext.class);
//		this.crawlSessionProvider = Guice.createInjector(core).getInstance(CrawlSessionProvider.class);
//		this.setPlugins(new Plugins(config, new MetricRegistry()));
//		this.browser = context.getBrowser();
//		if(this.context.getFragmentManager() == null) {
//			this.context.setFragmentManager(new FragmentManager(null));
//		}
//		factory = new HybridStateVertexFactory(0, builder, true);
//		plugins.runPreCrawlingPlugins(config);
//		return browser.getWebDriver();
//    }
    
    
}
