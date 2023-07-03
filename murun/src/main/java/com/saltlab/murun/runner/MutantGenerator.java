package com.saltlab.murun.runner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.state.StateVertex;
import com.crawljax.fragmentation.Fragment;
import com.crawljax.fragmentation.FragmentManager;
import com.crawljax.fragmentation.FragmentationPlugin;
import com.crawljax.stateabstractions.hybrid.HybridStateVertexFactory;
import com.crawljax.stateabstractions.hybrid.HybridStateVertexImpl;
import com.crawljax.util.DomUtils;
import com.crawljax.vips_selenium.VipsUtils;
import com.crawljax.vips_selenium.VipsUtils.AccessType;
import com.crawljax.vips_selenium.VipsUtils.Coverage;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.utils.MutationUtils;
import com.saltlab.murun.utils.Settings;
import com.saltlab.webmutator.MutationCandidate;
import com.saltlab.webmutator.MutationMode;
import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.NodeProperties;
import com.saltlab.webmutator.WebMutator;
import com.saltlab.webmutator.operators.Operator;
import com.saltlab.webmutator.operators.dom.DomOperator;
import com.saltlab.webmutator.plugins.DefaultNodePicker;
import com.saltlab.webmutator.utils.AttributeUtils;
import com.saltlab.webmutator.utils.EventListenerUtils;
import com.saltlab.webmutator.utils.XPathHelper;

import utils.Properties;

public class MutantGenerator {
	private static final Logger LOG = LoggerFactory.getLogger(MutantGenerator.class);

	WebMutator webMutator;
	CandidateScore candidateScore;
	TraceSession traceSession;
	SUBJECT subject;
	GENMODE mode;
	String SUBJECT_URL = null;

	static double ELEMENT_CONSTANT = 0.2;
	static double OPERATOR_CONSTANT = 1.0;
	static double REPEAT_CONSTANT = 0.2;

	public enum GENMODE {
		NEW, ADAPT
	}

	public MutantGenerator(TraceSession traceSession, SUBJECT subject, GENMODE mode) {
		this.subject = subject;
		this.traceSession = traceSession;
		this.mode = mode;
		initWebMutator();
		Properties subjectProperties = new Properties(subject);
		// SUBJECT_URL = Properties.app_url;
		SUBJECT_URL = "http://www.google.ca";
		this.candidateScore = new CandidateScore(ELEMENT_CONSTANT, OPERATOR_CONSTANT, REPEAT_CONSTANT);
	}

	public void initWebMutator() {
		this.webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
		if (mode == GENMODE.NEW) {
			populateAvailableEventHandlers();
			populateAvailableAttributes();
			populateAvailableTextStrings();
		}
		webMutator.initOperators();
	}

	private void populateAvailableEventHandlers() {
		Set<String> eventListeners = new HashSet<>();
		for (StateVertex state : traceSession.getStateFlowGraph().getAllStates()) {
			try {
				List<String> stateEvList = EventListenerUtils.getAllEventListeners(state.getDocument());
				eventListeners.addAll(stateEvList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		webMutator.setEventListeners(eventListeners);
	}

	private void populateAvailableTextStrings() {
		Set<String> textTokens = new HashSet<>();
		for (StateVertex state : traceSession.getStateFlowGraph().getAllStates()) {
			try {
				List<String> stateTextTokens = DomUtils.getTextTokens(state.getDocument());
				textTokens.addAll(stateTextTokens);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/*Set<String> returnTokens = new HashSet<>();
		for (String textToken : textTokens) {
			returnTokens.add(formatToken(textToken));
		}*/
		webMutator.setTextTokens(textTokens);
	}

	private String formatToken(String textToken) {
		if (textToken == null)
			return null;
		String returnToken = textToken.trim().replace("\n", "").replace("\r", "");
		if(returnToken.toLowerCase().contains("body")) {
			System.out.println("found it");
			System.out.println(returnToken);
		}
		return returnToken;
	}

	private void populateAvailableAttributes() {
		HashMap<String, Set<String>> stateTextTokens = new HashMap<String, Set<String>>();
		for (StateVertex state : traceSession.getStateFlowGraph().getAllStates()) {
			try {
				DomUtils.getAllAttributes(DomUtils.asDocument(state.getStrippedDom()), stateTextTokens,
						AttributeUtils.getStandardAttributes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		webMutator.setAttributeTokens(stateTextTokens);
	}

	public static List<DomOperator> getApplicableOperators(Node node, List<DomOperator> operators) {
		List<DomOperator> returnList = new ArrayList<>();
		for (DomOperator operator : operators) {
			if (operator instanceof DomOperator) {
				DomOperator domOp = (DomOperator) operator;
				if (domOp.isApplicable(node)) {
					returnList.add(domOp);
				}

			}
		}
		return returnList;
	}

	private void loadHybridState(StateVertex state, FragmentManager fragmentManager, File screenshotFile, File fragDom,
			EmbeddedBrowser driver, File tempFolder) throws IOException {

		if (driver != null && loadOldPage(state.getStrippedDom(), state.getUrl(), driver)) {
			LOG.info("Loading dom and using new screenshot for {}", state.getName());
			FragmentationPlugin.fragmentState(state, fragmentManager, driver, tempFolder, true);
		} else {
			LOG.warn("Could not load dom in browser for vertex {}", state.getName());
			if (Files.notExists(fragDom.toPath(), LinkOption.NOFOLLOW_LINKS)) {
				System.err.println("No Frag Dom found for Hybrid State : " + fragDom.toPath());
				return;
			}
			String domString = FileUtils.readFileToString(fragDom, Charset.defaultCharset());

			BufferedImage screenshot = ImageIO.read(screenshotFile);
			FragmentationPlugin.loadFragmentState(state, fragmentManager, DomUtils.asDocument(domString), screenshot);
			LOG.info("Loaded {} offline", state.getName());
		}

	}

	public boolean loadOldPage(String domString, String url, EmbeddedBrowser driver) {
		if (subject == SUBJECT.dummy) {
			File file = new File("src/main/resources/dummyWebPage.html");
			driver.getWebDriver().get("file://" + file.getAbsolutePath());
		} else {
			driver.getWebDriver().navigate().to(url);
		}
		try {
			((JavascriptExecutor) driver.getWebDriver())
					.executeScript("document.getElementsByTagName('html')[0].innerHTML=arguments[0]", domString);
		} catch (Exception ex) {
			LOG.error("Could not load the old page for the given dom");
			return false;
		}
		return true;
	}

	public StateVertex getHybridStateVertex(HybridStateVertexFactory stateVertexFactory, String domLocation, int id,
			String name) throws IOException {
		String dom = FileUtils.readFileToString(new File(domLocation));
		StateVertex state = stateVertexFactory.newStateVertex(id, SUBJECT_URL, name, dom, dom, null);

		// FragmentationPlugin.fragmentState(state, fragmentManager, browser, new
		// File(outputPath), true);
		return state;
	}

	int ID_SEED = 100000;
	int id = ID_SEED;

	int getNextID() {
		id += 1;
		return id;
	}

	public List<MutationCandidateEx> getAdaptedCandidates(List<MutationCandidateEx> originalCandidates,
			EmbeddedBrowser browser) {
		Map<String, StateVertex> loadedOldStates = new HashMap<String, StateVertex>();
		boolean APTED_VISUAL_DATA = true;
		HybridStateVertexFactory stateVertexFactory = new HybridStateVertexFactory(0,
				CrawljaxConfiguration.builderFor("http://crawls:3000"), APTED_VISUAL_DATA);

		List<MutationCandidateEx> newCandidates = new ArrayList<MutationCandidateEx>();

		for (MutationCandidateEx oldCandidate : originalCandidates) {
			String stateName = oldCandidate.getStateName();
			String domLocation = Paths
					.get(Settings.traceDir, subject.name(), Settings.REL_TRACE_LOC, "doms", stateName + ".html")
					.toString();
			String fragDomLocation = Paths.get(Settings.traceDir, subject.name(), Settings.REL_TRACE_LOC, "doms",
					"frag_" + stateName + ".html").toString();
			String screenShotLocation = Paths
					.get(Settings.traceDir, subject.name(), Settings.REL_TRACE_LOC, "screenshots", stateName + ".png")
					.toString();

			StateVertex stateVertex = null;
			if (loadedOldStates.containsKey(stateName)) {
				stateVertex = loadedOldStates.get(stateName);
			} else {
				try {
					stateVertex = getHybridStateVertex(stateVertexFactory, domLocation, getNextID(),
							"old_" + stateName);
					loadHybridState(stateVertex, traceSession.getFragmentManager(), new File(screenShotLocation),
							new File(fragDomLocation), browser, new File("temp"));
					loadedOldStates.put(stateName, stateVertex);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (stateVertex == null) {
				LOG.error("Cannot adapt candidate {}", oldCandidate);
				continue;
			}

			Node candidateNode = null;
			try {
				candidateNode = DomUtils.getElementByXpath(stateVertex.getDocument(), oldCandidate.getXpath());
			} catch (XPathExpressionException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (candidateNode == null) {
				LOG.error("Cannot adapt candidate - cannot find node for xpath in original state {}", oldCandidate);
				continue;
			}

			Fragment closest = ((HybridStateVertexImpl) stateVertex).getClosestFragment(candidateNode);
			if (closest == null || !FragmentManager.usefulFragment(closest)) {
				LOG.error("Cannot adapt candidate - cannot find useful closest fragment for xpath in original state {}",
						oldCandidate);
				continue;
			}

			// closestGlobal is the fragment inside trace. So we can get the coverage data
			// from it.

			List<CoverageDetail> coverageMap = null;

			List<Fragment> traceFragments = getRelatedTraceFragments(closest);
			if (traceFragments != null && !traceFragments.isEmpty()) {
				Fragment closestGlobal = traceFragments.get(0);
				Node eqNode = closestGlobal.getEquivalentNode(candidateNode, closest);
				StateVertex eqState = closestGlobal.getReferenceState();
				coverageMap = getCoverageDetails(eqNode, eqState);
			}
			// if(!closest.isGlobal()){
			// There is a global fragment that is equivalent

			// if(closest.getDuplicateFragments()!=null &&
			// !closest.getDuplicateFragments().isEmpty()) {
			// closestGlobal = closest.getDuplicateFragments().get(0);
			// }
			// else if(closest.getEquivalentFragments()!=null &&
			// !closest.getEquivalentFragments().isEmpty()) {
			// closestGlobal = closest.getEquivalentFragments().get(0);
			// }
			//

			DomOperator applicable = webMutator.getDomOperator(oldCandidate.getOpString());
			// CoverageDetail coverageDetails = oldCandidate.coverageMap;

			MutationCandidateEx newCandidate = new MutationCandidateEx(candidateNode, candidateNode, applicable,
					closest, coverageMap);
			newCandidate.setRecord(oldCandidate.getRecord());
			newCandidates.add(newCandidate);
		}
		addMapforCandidates(newCandidates);
		return newCandidates;
	}

	public List<Fragment> getRelatedTraceFragments(Fragment oldFragment) {
		List<Fragment> related = new ArrayList<Fragment>();
		List<Fragment> allRelated = traceSession.getFragmentManager().getRelatedFragments(oldFragment);
		for (Fragment rel : allRelated) {
			if (rel.getReferenceState().getName().startsWith("old_")) {
				continue;
			}
			related.add(rel);
		}
		return related;
	}

	/*
	 * Gets coverage details either from coverage map in trace session or from the
	 * DOM.
	 */
	public List<CoverageDetail> getCoverageDetails(Node node, StateVertex state) {
		CoverageNode coverageNode = new CoverageNode(state.getId(), XPathHelper.getSkeletonXpath(node));

		List<CoverageDetail> coverageDetails = new ArrayList<>();
		if (traceSession != null && traceSession.getCoverageMap() != null
				&& traceSession.getCoverageMap().get(coverageNode) != null) {

			coverageDetails.addAll(traceSession.getCoverageMap().get(coverageNode));
		}
		
		if(VipsUtils.getCoverage(node, AccessType.equivalent) != Coverage.none) {
			coverageDetails.add(new CoverageDetail("Equivalent", VipsUtils.getCoverage(node, AccessType.equivalent),
					AccessType.equivalent, state.getName()));
		}
		
		
		if(VipsUtils.getCoverage(node, AccessType.js) != Coverage.none) {
			coverageDetails.add(new CoverageDetail("fromDOM", VipsUtils.getCoverage(node, AccessType.js),
					AccessType.js, state.getName()));
		}
		

		return coverageDetails;
	}

	public List<MutationCandidateEx> getCandidatePool() {
		List<DomOperator> operators = webMutator.getDomOperators();
		List<MutationCandidateEx> candidatePool = new ArrayList<>();

		for (StateVertex state : traceSession.getStateFlowGraph().getAllStates()) {
			try {
				Document toMutate = DomUtils.asDocument(state.getStrippedDom());
				NodeList allNodes = DomUtils
						.getAllSubtreeNodes(state.getDocument().getElementsByTagName("body").item(0));
				if(allNodes == null) {
					LOG.error("No nodes found for state {}", state);
					continue;
				}
				for (int i = 0; i < allNodes.getLength(); i++) {
					try {
						Fragment closest = state.getClosestFragment(allNodes.item(i));
						if (closest.isGlobal()) {
							// add a candidate
							Node node = allNodes.item(i);
							Node nodeToMutate = DomUtils.getElementByXpath(toMutate, XPathHelper.getXPathExpression(node));
							List<DomOperator> applicableOp = getApplicableOperators(node, operators);
							List<CoverageDetail> coverageDetails = getCoverageDetails(node, state);
							for (Operator applicable : applicableOp) {
								candidatePool.add(
										new MutationCandidateEx(node, nodeToMutate, applicable, closest, coverageDetails));
							}
						}
					}catch(Exception ex) {
						LOG.error("Cannot generate candidates for node {}", allNodes.item(i));
					}
				}
			} catch (XPathExpressionException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(Exception ex) {
				LOG.error("Cannot process candidates from state {}", state);
				ex.printStackTrace();
			}
		}
		return candidatePool;
	}

	public List<MutationCandidateEx> mutateTrace() {
	
		int maxMutants = Settings.MAX_MUTANTS;

		List<MutationCandidateEx> pool = getCandidatePool();
		if(pool == null) {
			LOG.error("No candidate pool could be retrieved. Pool is {}", pool);
			return null;
		}
		LOG.info("TOtal available Candidates - {}", pool.size());
		totalScore(pool);
		try {
			MutationUtils.writeMutationCandidates(subject, pool);
		} catch (Exception ex) {
			LOG.error("Could not save mutation candidates {} to file", pool);
			LOG.error("Exception {}", ex.getMessage());
		}
		
		if (pool.size() < maxMutants) {
			maxMutants = pool.size();
		}
		
		try {
			List<MutationCandidateEx> randomMutants = selectMutants_random(maxMutants, pool);
			MutationUtils.writeMutationRecords_random(subject, randomMutants);
		}catch(Exception ex) {
			ex.printStackTrace();
			LOG.error("Exception writing randome mutants");
		}
		
		return selectMutants_bsr(maxMutants, pool);
	}

	private List<MutationCandidateEx> selectMutants_random(int maxMutants, List<MutationCandidateEx> pool) {
		int numMutants = 0;
		List<MutationCandidateEx> unUsed = new ArrayList<>();
		unUsed.addAll(pool);
		List<MutationCandidateEx> selectedCandidates = new ArrayList<MutationCandidateEx>();
		List<MutationCandidateEx> toBeMapped = new ArrayList<MutationCandidateEx>();

		while (numMutants < maxMutants) {
			 int nextRandom = webMutator.getRandom(unUsed.size());
			 MutationCandidateEx nextCandidate = unUsed.remove(nextRandom);

			
			if (nextCandidate == null) {
				LOG.info("Could not get next candidate from {}", unUsed);
				break;
			}

			try {
				MutationRecord record = nextCandidate.getRecord();
				if(record == null) {
					record = webMutator.applyMutationToCandidate(nextCandidate);
					toBeMapped.add(nextCandidate);
				}
				if (record.isSuccess()) {
					numMutants += 1;
				}
				selectedCandidates.add(nextCandidate);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			unUsed.remove(nextCandidate);

			if (unUsed.isEmpty()) {
				// No more candidates left
				LOG.info("no more candidates left. {} mutants created", numMutants);
				break;
			}
		}

		addMapforCandidates(toBeMapped);
		return selectedCandidates;
	}

	public List<MutationCandidateEx> selectMutants_bsr(int maxMutants, List<MutationCandidateEx> pool) {
		try {
			pool = filterLowScores(pool);
			LOG.info("Filtered Pool size - {}", pool.size());
		}catch(Exception ex) {
			ex.printStackTrace();
			LOG.error("Could not filter mutants based on scores");
		}
		if (pool.size() < maxMutants) {
			maxMutants = pool.size();
		}

		int numMutants = 0;
		List<MutationCandidateEx> unUsed = new ArrayList<>();
		unUsed.addAll(pool);
		List<MutationCandidateEx> selectedCandidates = new ArrayList<MutationCandidateEx>();
		List<MutationCandidateEx> toBeMapped = new ArrayList<MutationCandidateEx>();
		while (numMutants < maxMutants) {

			MutationCandidateEx nextCandidate = getNextCandidate(unUsed);

			if (nextCandidate == null) {
				LOG.info("Could not get next candidate from {}", unUsed);
				break;
			}

			try {
				MutationRecord record = nextCandidate.getRecord();
				if(record == null) {
					record = webMutator.applyMutationToCandidate(nextCandidate);
					toBeMapped.add(nextCandidate);
				}
				if (record.isSuccess()) {
					numMutants += 1;
				}
				selectedCandidates.add(nextCandidate);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			unUsed.remove(nextCandidate);

			if (unUsed.isEmpty()) {
				// No more candidates left
				LOG.info("no more candidates left. {} mutants created", numMutants);
				break;
			}

			if (numMutants < maxMutants)
				recomputePriorityScore(unUsed, nextCandidate);

		}

		addMapforCandidates(toBeMapped);
		return selectedCandidates;
	}

	private List<MutationCandidateEx> filterLowScores(List<MutationCandidateEx> allCandidates) {
		List<MutationCandidateEx> returnList = new ArrayList<>();

		for (MutationCandidateEx candidate : allCandidates) {
			if (candidate.getPriorityScore() < 0) {
				candidateScore.computeCandidateScore(candidate, traceSession);
			}
			if (candidate.getElementScore() >= Settings.SCORE_THRESHOLD
					&& candidate.getOperatorScore() >= Settings.SCORE_THRESHOLD) {
				returnList.add(candidate);
			} else {
				LOG.info("Filtering candidate {} \n elementScore - {} \n operatorScore - {}", candidate,
						candidate.getElementScore(), candidate.getOperatorScore());
			}
		}
		return returnList;
	}

	public int totalScore(List<MutationCandidateEx> allCandidates) {
		int totalScore = 0;

		for (MutationCandidateEx candidate : allCandidates) {
			if (candidate.getPriorityScore() < 0) {
				candidateScore.computeCandidateScore(candidate, traceSession);
			}
			totalScore += candidate.getCurrentScore();
		}
		return totalScore;
	}

	/**
	 * biased random (with score as probability)
	 * 
	 * @param available
	 * @return
	 */
	public MutationCandidateEx getNextCandidate(List<MutationCandidateEx> available) {
		if (available == null || available.isEmpty()) {
			LOG.error("No candidates provided to select");
			return null;
		}

		/*
		 * 
		 * int sum_of_weight = 0; for(int i=0; i<num_choices; i++) { sum_of_weight +=
		 * choice_weight[i]; } int rnd = random(sum_of_weight);
		 * 
		 * for(int i=0; i<num_choices; i++) { if(rnd < choice_weight[i]) return i; rnd
		 * -= choice_weight[i]; } assert(!"should never get here");
		 */
		int totalScore = totalScore(available);

		if (totalScore <= 0) {
			LOG.error("Not Possible to have Negative total score {}", totalScore);
		}

		Random random = new Random();
		int nextInt = 0;
		try {
			nextInt = Math.abs(random.nextInt(totalScore));

		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("Error getting random number for total {}", totalScore);
		}

		int arrayIndex = -1;

		for (MutationCandidateEx candidate : available) {
			arrayIndex += candidate.getCurrentScore();
			if (arrayIndex >= nextInt) {
				return candidate;
			}
			// totalScore -= candidate.getPriorityScore();
		}
		// Should not reach
		LOG.error("Could not find a candidate for the random number {} . Checked till {}", nextInt, arrayIndex);
		return null;
	}

	public List<MutationCandidateEx> recomputePriorityScore(List<MutationCandidateEx> available,
			MutationCandidateEx selected) {
		for (MutationCandidateEx candidate : available) {
			candidateScore.recomputeCandidateScore(candidate, selected, traceSession);
		}
		return available;
	}

	/*
	 * Create a map of nodes to which each mutation should be applied
	 */
	public void addMapforCandidates(List<MutationCandidateEx> mutationMap) {

		for (MutationCandidateEx candidateEx : mutationMap) {
			if(candidateEx == null 
					|| candidateEx.getRecord() ==null 
					|| !candidateEx.getRecord().isSuccess()
					|| candidateEx.getFragment() == null
					|| traceSession==null 
					|| traceSession.getFragmentManager()==null) {
				continue;
			}
			
			
			List<Fragment> relatedFrags = null;
			try {		
				relatedFrags =	traceSession.getFragmentManager()
					.getRelatedFragments(candidateEx.getFragment());
			
			}catch(Exception ex) {
				ex.printStackTrace();
				LOG.error("Could not get related fragments for candidate {}", candidateEx);
				continue;
			}
			
			for (Fragment related : relatedFrags) {
				if (related.equals(candidateEx.getFragment())) {
					continue;
				}
				// Get the node from stripped dom
				Node toMutate = related.getEquivalentNode(candidateEx.getOrigNode(), candidateEx.getFragment());
				if (toMutate == null) {
					LOG.error("Error adding mutation record \n candidate {} \n equivalent node {} ", toMutate,
							candidateEx);
					continue;
				}
				
				try {
					NodeProperties eqNodeProps = MutationCandidate.extractNodeProperties(toMutate);
					Document origDoc = DomUtils.asDocument(related.getReferenceState().getStrippedDom());
					String xpath = XPathHelper.getXPathExpression(toMutate);
					toMutate = DomUtils.getElementByXpath(origDoc, xpath);
					MutationRecord record = webMutator.applyDomOperator(toMutate, candidateEx.getOperator(),
							eqNodeProps);
					candidateEx.addToMutantMap(related.getReferenceState().getName(), record);
				} catch (IOException | XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception ex) {
					ex.printStackTrace();
					LOG.error("Error adding mutation record \n equivalent node {} \n candidate {}", toMutate,
							candidateEx);
				}

			}
		}

		/*
		 * Replicate the record for all concrete states
		 */
		if(traceSession.allConcreteStates == null) {
			LOG.error("Session has no concrete states");
			return;
		}
		
		for (StateVertex state : traceSession.allConcreteStates.values()) {
			try {
				String stateName = state.getName();
				String duplicate = traceSession.statementToStateMap.get(stateName);
				if (!duplicate.equalsIgnoreCase(stateName)) {
					// State that is not added to the flowgraph.. duplicate of some other state
					/*
					 * } int duplicate = state.getCluster(); if (duplicate != state.getId()) {
					 */
					// State that has not been added to the graph
					StateVertex duplicateState = traceSession.allConcreteStates.get(duplicate);
					for (MutationCandidate candidate : mutationMap) {
						if (!candidate.getRecord().isSuccess() || !(candidate instanceof MutationCandidateEx)) {
							continue;
						}
						MutationCandidateEx candidateEx = (MutationCandidateEx) candidate;
						candidateEx.replicateRecord(duplicateState, state);
					}
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				LOG.error("Error adding duplicate records for state {}", state);
			}
		}

	}
}
