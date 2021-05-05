package com.saltlab.murun.runner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.joox.selector.CSS2XPath;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.browser.WebDriverBackedEmbeddedBrowser;
import com.crawljax.core.CandidateElement;
import com.crawljax.core.CandidateElementExtractor;
import com.crawljax.core.CrawlSession;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.ExitNotifier.ExitStatus;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.BrowserOptions;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.plugin.Plugins;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.Eventable.EventType;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.InMemoryStateFlowGraph;
import com.crawljax.core.state.StateVertex;
import com.crawljax.di.CoreModule;
import com.crawljax.di.CoreModule.CandidateElementExtractorFactory;
import com.crawljax.di.CrawlSessionProvider;
import com.crawljax.forms.FormInput;
import com.crawljax.fragmentation.FragmentManager;
import com.crawljax.fragmentation.FragmentationPlugin;
import com.crawljax.plugins.coverage.CoveragePlugin;
import com.crawljax.plugins.crawloverview.CrawlOverview;
import com.crawljax.stateabstractions.hybrid.HybridStateVertexFactory;
import com.crawljax.stateabstractions.hybrid.HybridStateVertexImpl;
import com.crawljax.vips_selenium.VipsUtils;
import com.crawljax.vips_selenium.VipsUtils.AccessType;
import com.crawljax.vips_selenium.VipsUtils.Coverage;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.saltlab.murun.utils.Settings;
import com.saltlab.webmutator.utils.DomUtils;
import com.saltlab.webmutator.utils.XPathHelper;


/*
 * To monitor a session of test execution 
 * 
 * 
 */
public class TraceSession {
	private static final Logger LOG = LoggerFactory.getLogger(TraceSession.class);

    private static TraceSession ourInstance = new TraceSession();
    
    Map<CoverageNode, List<CoverageDetail>> coverageMap = new HashMap<>(); 
    Map<CoverageNode, List<CoverageDetail>> observerCoverageMap = new HashMap<>();
	
    Map<String, String> statementToStateMap = new HashMap<String, String>();
    Map<String, List<String>> stateClusters = new HashMap<String, List<String>>();
    
    Map<String, StateVertex> allConcreteStates = new HashMap<String, StateVertex>();
	StateVertex currentState;
	private Plugins plugins;
	private CrawlerContext context;
	private CrawljaxConfiguration config;
	private EmbeddedBrowser browser;
	private CrawljaxConfigurationBuilder builder;
	private CrawlSessionProvider crawlSessionProvider;
	private HybridStateVertexFactory factory;
	private int stateIndex = 0;
	private CrawlSession session;
	private boolean firstState = true;
	Node lastNode = null;
	String lastBy = null;
	Eventable lastEvent = null;

	private FragmentManager fragmentManager;

	private long eventId = 0;

	private CandidateElementExtractor extractor;

	private int getStateNum() {
		stateIndex += 1;
		return stateIndex-1;
	}
	
	private long nextEventId() {
		eventId += 1 ;
		return eventId-1;
	}
	
	
    public CrawlerContext getContext() {
		return context;
	}

	public void setContext(CrawlerContext context) {
		this.context = context;
	}

	public CrawljaxConfiguration getConfig() {
		return config;
	}

	public void setConfig(CrawljaxConfiguration config) {
		this.config = config;
	}

	public EmbeddedBrowser getBrowser() {
		return browser;
	}

	public void setBrowser(EmbeddedBrowser browser) {
		this.browser = browser;
	}
	public void crawljaxSetup(String output, String url) {
    	this.builder = CrawljaxConfiguration.builderFor(url);
//    	if(headless)
//    		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME_HEADLESS), new BrowserOptions(2));
//    	else
		builder.setBrowserConfig(new BrowserConfiguration(BrowserType.CHROME),  new BrowserOptions(Settings.PIXEL_DENSITY));

    	builder.setOutputDirectory(new File(output));
    	builder.addPlugin(new CrawlOverview());
    	builder.addPlugin(new FragmentationPlugin());
    	builder.addPlugin(new CoveragePlugin());
    	this.config = builder.build();
    	CoreModule core= new CoreModule(config);
    	Injector injector = Guice.createInjector(core);
		this.context = injector.getInstance(CrawlerContext.class);
		this.browser = context.getBrowser();

		this.crawlSessionProvider = injector.getInstance(CrawlSessionProvider.class);
		this.extractor = injector.getInstance(CandidateElementExtractorFactory.class).newExtractor(browser);
//		this.setPlugins(new Plugins(config, new MetricRegistry()));
		plugins = injector.getInstance(Plugins.class);
		if(this.context.getFragmentManager() == null) {
			setFragmentManager(new FragmentManager(null));
			this.context.setFragmentManager(getFragmentManager());
		}
		else {
			setFragmentManager(this.context.getFragmentManager());
		}

		factory = new HybridStateVertexFactory(0, builder, false);
		// compare fast 
		HybridStateVertexImpl.FAST_COMPARE = true;
		FragmentationPlugin.COMPARE_FAST = true;
		FragmentationPlugin.DISABLE_STATE_COMP = true;
		plugins.runPreCrawlingPlugins(config);
		browser.close();
    }
	

    
    public WebDriver getDriver_Crawljax(WebDriver driver) {
    	browser = WebDriverBackedEmbeddedBrowser.withDriver(driver);
    	((WebDriverBackedEmbeddedBrowser)browser).setPixelDensity(Settings.PIXEL_DENSITY);
    	context.updateBrowser(browser);
    	return driver;
    }
    

    public StateVertex getState(String stateName) {
    	long start = System.currentTimeMillis();
    	int stateNum = getStateNum();
		StateVertex newState = factory.newStateVertex(stateNum, browser.getCurrentUrl(), stateName, browser.getStrippedDom(), browser.getStrippedDom(), browser);
		long end = System.currentTimeMillis();
		LOG.info("creating new state took {} ms", end-start);
		start = System.currentTimeMillis();
		allConcreteStates.put(stateName, newState);
		if(firstState) {
			extractor.extract(newState);
			crawlSessionProvider.setup(newState, context);
			session = crawlSessionProvider.get();
			firstState = false;
			statementToStateMap.put(newState.getName(), newState.getName());
			stateClusters.put(newState.getName(), new ArrayList<String>());
		}
		else {
			StateVertex added = ((InMemoryStateFlowGraph)session.getStateFlowGraph()).putIfAbsent(newState);
			if(added!=null) {
				statementToStateMap.put(newState.getName(), added.getName());
				if(!stateClusters.containsKey(added.getName())) {
					stateClusters.put(added.getName(), new ArrayList<String>());
				}
				stateClusters.get(added.getName()).add(newState.getName());
				newState.setCluster(added.getId());
				return added;
			}
			else {
				// Extract candidates if new state
				extractor.extract(newState);
				statementToStateMap.put(newState.getName(), newState.getName());
				stateClusters.put(newState.getName(), new ArrayList<String>());
			}
		}
		end = System.currentTimeMillis();
		LOG.info("Extracting new state took {} ms", end-start);
		plugins.runOnNewStatePlugins(context, newState);
		
		fragmentManager.setAccess(newState);
		return newState;
    }

	public Plugins getPlugins() {
		return plugins;
	}

	public void setPlugins(Plugins plugins) {
		this.plugins = plugins;
	}

	public CrawljaxConfigurationBuilder getBuilder() {
		return this.builder;
	}

	public CrawlSessionProvider getSessionProvider() {
		return this.crawlSessionProvider;
	}
	
	/**
	 * Called after URL load etc. no need to add an edge for this 
	 * @param stateName
	 */
	public void newNavState(String stateName) {
		currentState = getState(stateName);
		lastEvent = null;
	}
	
	/**
	 * Called after action
	 * @param stateName
	 * @return
	 */
	public StateVertex newState(String stateName) {
		long start = System.currentTimeMillis();
		StateVertex newState = getState(stateName);
//		lastEvent.setSource(currentState);
//		lastEvent.setTarget(newState);
		try {
			boolean addedEdge = ((InMemoryStateFlowGraph)session.getStateFlowGraph()).addEdge(currentState, newState, lastEvent);
			if(!addedEdge) {
				LOG.warn("Edge not added states {} -> {} : {}", currentState.getId(), newState.getId(), lastEvent);
			}
		}catch(Exception ex) {
			LOG.error("Exception adding edge {}", ex.getMessage());
		}
		
		currentState = newState;
		long end = System.currentTimeMillis();
		LOG.info("Took {} ms to get new state", end-start);
		return newState;
	}
	
	public void saveCoverageMap() {
		File outputDir = context.getSession().getConfig().getOutputDir();
		Gson gson = new Gson();
		
		File jsonFile = new File(outputDir, "testCoverageMap.json");
		try {
			FileWriter writer = new FileWriter(jsonFile);
			writer.write(gson.toJson(coverageMap));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jsonFile = new File(outputDir, "observerCoverageMap.json");
		try {
			FileWriter writer = new FileWriter(jsonFile);
			writer.write(gson.toJson(observerCoverageMap));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeStatementToStateMap() {
		File outputDir = context.getSession().getConfig().getOutputDir();
		Gson gson = new Gson();
		
		File jsonFile = new File(outputDir, "statementMap.json");
		try {
			FileWriter writer = new FileWriter(jsonFile);
			writer.write(gson.toJson(statementToStateMap));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopTrace() {
		writeStatementToStateMap();
		saveCoverageMap();
		try {
			plugins.runPostCrawlingPlugins(session, ExitStatus.STOPPED);
		}catch(Exception ex) {
			ex.printStackTrace();
			LOG.error("Error running post crawl plugins");
		}
//		context.stop();
		context.getBrowser().close();
	}
	
	public static TraceSession getInstance() {
		return ourInstance;
	}
	
	public void recordFindElement(String statementName, String byString) {
		lastBy = byString;
		Node node = findNode(byString);
		lastNode =node;
		try {
		getFragmentManager().recordCoverage(node, currentState, Coverage.find);
		
		CoverageNode coverageNode = new CoverageNode(currentState.getId(), com.crawljax.util.XPathHelper.getSkeletonXpath(node));

		if(!coverageMap.containsKey(coverageNode)) {
			coverageMap.put(coverageNode, new ArrayList<>());
		}
		CoverageDetail coverageDetail = new CoverageDetail(statementName, Coverage.find, AccessType.direct, currentState.getName());	
		coverageMap.get(coverageNode).add(coverageDetail);
		}catch(Exception ex) {
			LOG.error("Cannot record coverage for {}", node);
		}
	}
	
	private Node findNode(String how, String value) throws XPathExpressionException, IOException {

		String xpath = "";
		switch(how) {
		case "xpath":
			xpath = XPathHelper.formatXPath(value);
			break;
		case "id":
		case "name":
			xpath = "//*" + "[@name='" + value
			+ "' or @id='" + value + "']";
			break;
		case "linktext":
			xpath = "//*[normalize-space(text())='" + value + "']";
			break;
		case "partiallinktext":
			xpath = "//*[contains(text(),'" + value + "')]";
			break;
		case "cssselector":
		case "css selector":
			xpath = CSS2XPath.css2xpath(value);
			xpath = XPathHelper.formatXPath(xpath);
			break;
		default:
			xpath = "//*[@" + how + "='" + value+ "']";
			break;
		}
		LOG.info("Trying to find {} ", xpath);
		
		NodeList list = XPathHelper.evaluateXpathExpression(currentState.getDocument(), xpath);
		
//		System.out.println(XPathHelper.evaluateXpathExpression(currentState.getDocument(), "//BUTTON[@class='loginbutn'] or starts-with(@class, 'loginbutn ') or ' loginbutn' = substring(@class, string-length(@class) - string-length(' loginbutn') + 1) or contains(@class, ' loginbutn ')] ").getLength());
//		NodeList anotherList = XPathHelper.evaluateXpathExpression(currentState.getStrippedDom(), xpath);
//		System.out.println(currentState.getStrippedDom());
		LOG.info("Found {} nodes", list.getLength());
		
		if(list.getLength() >= 1) {
			return list.item(0);
		}
		return null;
	}
	
	private Node findNode(String byString) {
		try {
			LOG.info("finding {}", byString);

			String[] split = byString.split(":");
			String how = split[0].split("\\.")[1].toLowerCase();
			String value = split[1].trim();
			return findNode(how, value);
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void loadURL() {
		lastNode = null;
		currentState = null;
	}
	
	/**
	 * Called before event is executed
	 * @param elem
	 */
	public void recordEvent(String statementName, WebElement elem) {
		if(currentState == null) {
			LOG.error("Calling recordEvent without state");
			return;
		}
		
		if(elem==null) {
			LOG.error("webelement is null");
			return;
		}
		
		
		if(lastNode == null) {
			// Try to find the document node using byString of RemoteWebElement
			String byString = elem.toString().split("->")[1].trim();
			try {
				byString = byString.substring(0, byString.length()-1);
				lastNode = findNode(byString.split(":")[0].trim().toLowerCase(), byString.split(":")[1].trim());
			}catch(Exception ex) {
				ex.printStackTrace();
			}finally {
				if(lastNode == null) {
					LOG.warn("Cannot record event : failed to get node for {} with tag {}", lastBy, elem.getTagName());
					return;
				}
			}
		}
		try {
			List<CandidateElement> el = currentState.getCandidateElement(lastNode);
			CoverageNode coverageNode = null;
			if(el==null || el.size() == 0) {
				String xpath = XPathHelper.getSkeletonXpath(lastNode);
				LOG.error("Could not record event for elem", xpath);
				coverageNode = 	new CoverageNode(currentState.getId(), XPathHelper.getSkeletonXpath(lastNode));
				CandidateElement cand = new CandidateElement((Element)lastNode, new Identification(Identification.How.xpath, xpath), "", new ArrayList<FormInput>());
				lastEvent = new Eventable(cand, EventType.click, nextEventId());
			}
			else {
				getFragmentManager().recordAccess(el.get(0), currentState);
				coverageNode = 	new CoverageNode(currentState.getId(), el.get(0).getIdentification().getValue());
				lastEvent = new Eventable(el.get(0), EventType.click, nextEventId());
			}
	
			if(!coverageMap.containsKey(coverageNode)) {
				coverageMap.put(coverageNode, new ArrayList<>());
			}
			CoverageDetail coverageDetail = new CoverageDetail(statementName, Coverage.action, AccessType.direct, currentState.getName());	
			coverageMap.get(coverageNode).add(coverageDetail);
			
			/* Get JavaScript Observer coverage before navigating away from state*/			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			long start = System.currentTimeMillis();
			populateObserverCoverageMap(statementName);
//			System.out.println(observerCoverageMap);
			long end = System.currentTimeMillis();
			LOG.info("Took {} ms to find coverage", start-end);
			
		}catch(Exception ex) {
			ex.printStackTrace();
			LOG.error("Could not get observer coverage");
		}
	}

	public FragmentManager getFragmentManager() {
		return fragmentManager;
	}

	public void setFragmentManager(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}

	public InMemoryStateFlowGraph getStateFlowGraph() {
		// TODO Auto-generated method stub
		return (InMemoryStateFlowGraph) session.getStateFlowGraph();
	}

	public Map<CoverageNode, List<CoverageDetail>> getCoverageMap() {
		return coverageMap;
	}
	
	public void populateObserverCoverageMap(String statementName){
//		Map<CoverageNode, List<CoverageDetail>> covMap = new HashMap<CoverageNode, List<CoverageDetail>>();
		String coverageRaw = recordObserverCoverage(statementName);
		if(coverageRaw!=null && !coverageRaw.trim().isEmpty()) {
			String[] split = coverageRaw.trim().split(";");
			for(String covItem: split) {
				if(covItem == null || covItem.trim().isEmpty()) {
					continue;
				}
				try {
					String[] itemSplit = covItem.split("-");
					String covType = itemSplit[0];
					Coverage type = VipsUtils.getCoverageTypeFromString(covType);
					if(type==Coverage.none) {
						type = Coverage.implicit;
					}
					String xpath = itemSplit[1];
					CoverageNode covNode = new CoverageNode(currentState.getId(), xpath);
					CoverageDetail detail = new CoverageDetail(statementName, type, AccessType.js, currentState.getName());
					if(!observerCoverageMap.containsKey(covNode)) {
						observerCoverageMap.put(covNode, new ArrayList<CoverageDetail>());
					}
					observerCoverageMap.get(covNode).add(detail);
					
//				try {
					 Node node = DomUtils.getElementByXpath(currentState.getDocument(), xpath);
					
					if(node == null) {
						LOG.error("Could not find node for JS Observer node {}", xpath);
						/*NodeList nodes= XPathHelper.evaluateXpathExpression(currentState.getDocument(), xpath);
						if(nodes == null || nodes.getLength()==0) {
							LOG.error("No luck iwth XpathHelper");
						}*/
						continue;
					}
					VipsUtils.setCoverage(node, AccessType.js, type);
				} catch (XPathExpressionException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(Exception ex) {
					LOG.error("Could not use for JS Observer item {}", covItem);
					ex.printStackTrace();
				}
			}
		}
	}
	
	public String recordObserverCoverage(String statementName) {
		String recordedAccess = (String)((org.openqa.selenium.JavascriptExecutor)browser.getWebDriver()).executeScript("return localStorage.getItem('mutationData')");
//		System.out.println(recordedAccess);
		resetObserverCoverage();
		return recordedAccess;
	}

	private void resetObserverCoverage() {
		((org.openqa.selenium.JavascriptExecutor)browser.getWebDriver()).executeScript("localStorage.setItem('mutationData', ''); observedElements=[]");
	}

	public Map<CoverageNode, List<CoverageDetail>> getObserverCoverageMap() {
		// TODO Auto-generated method stub
		return observerCoverageMap;
	}
	
}
