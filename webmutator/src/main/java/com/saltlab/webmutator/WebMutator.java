package com.saltlab.webmutator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.saltlab.webmutator.operators.Operator;
import com.saltlab.webmutator.operators.dom.DomOperator;
import com.saltlab.webmutator.operators.dom.DomOperators;
import com.saltlab.webmutator.operators.visual.VisualOperators;
import com.saltlab.webmutator.plugins.NodePickerPlugin;
import com.saltlab.webmutator.utils.DomUtils;
import com.saltlab.webmutator.utils.XPathHelper;

public class WebMutator {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebMutator.class.getName());

	private static final int MAX_RETRIES = 3;
	public static final String MUTATION_ATTRIBUTE = "mutation";

	private static final String MUTATION_OPERATOR_ATTRIBUTE = "mutationoperator";
	private int tries = 0;
	String domLocation;
	String screenshotLocation;
	MutationMode mutationMode;
	List<String> excludedOperators;
	Random random;
	private List<DomOperator> domOperators;

	public List<DomOperator> getDomOperators() {
		if(domOperators == null) {
			initOperators();
		}
		return domOperators;
	}

	public void setDomOperators(List<DomOperator> domOperators) {
		this.domOperators = domOperators;
	}

	private List<Operator> visualOperators;
	NodePickerPlugin nodePicker;

	/*
	 * Random tokens used for mutation need to be set by caller
	 */
	Set<String> eventListeners;

	Set<String> textTokens;

	private Map<String, Set<String>> attributeTokens;

	public Set<String> getTextTokens() {
		return textTokens;
	}

	/**
	 * 
	 * @param crawlPath
	 * @param mutationMode
	 */
	public WebMutator(String crawlPath, MutationMode mutationMode, NodePickerPlugin nodePicker) {
		random = new Random();
		random.longs();
		this.domLocation = new File(crawlPath, "doms").getAbsolutePath();
		this.screenshotLocation = new File(crawlPath, "screenshots").getAbsolutePath();
		this.mutationMode = mutationMode;
		this.visualOperators = new VisualOperators().getVisualOperators(excludedOperators);
		this.nodePicker = nodePicker;
		this.eventListeners = null;
		this.textTokens = null;
		this.attributeTokens = null;
	}

	public WebMutator(MutationMode mutationMode, NodePickerPlugin nodePicker) {
		random = new Random();
		random.longs();
		this.mutationMode = mutationMode;
		this.visualOperators = new VisualOperators().getVisualOperators(excludedOperators);
		this.nodePicker = nodePicker;
		this.eventListeners = null;
		this.textTokens = null;
		this.attributeTokens = null;
	}

	public void mutateState(String stateName) {
		switch (mutationMode) {
		case DOM:
			mutateDom(stateName);
			break;
		case VISUAL:
			mutateScreenshot(stateName);
			break;
		case ALL:
		default:
			mutateDom(stateName);
			mutateScreenshot(stateName);
		}
	}

	private void mutateScreenshot(String stateName) {

	}

	/**
	 * 
	 * @param document
	 *            to be mutated
	 * @return mutated document if successful or null if unsuccessful
	 */
	public MutationRecord mutateDocument(Document dom) {
		try {
			Operator domOperator = pickDomOperator();
			String domStr = DomUtils.getStrippedDom(DomUtils.getDocumentToString(dom));
			MutationRecord record = applyDomOperator(dom, domOperator);
			record.originalDom = domStr;
			if (record.success) {
				String mutant = DomUtils.getDocumentToString(dom);
				String stripped = DomUtils.getStrippedDom(mutant);
				record.mutatedDom = stripped;
			}
			return record;
		} catch (Exception ex) {
			return new MutationRecord(false, "Exception in [mutateDomString()]" + ex.getMessage());
		}
	}

	/**
	 * 
	 * @param domStr
	 * @return mutated domStr if successful or null if unsuccessful
	 */
	public MutationRecord mutateDomString(String domStr) {
		Document dom;
		try {
			dom = DomUtils.asDocument(domStr);
			return mutateDocument(dom);
		} catch (IOException e) {
			e.printStackTrace();
			return new MutationRecord(false, "Exception in [mutateDomString()]" + e.getMessage());
		}
	}

	private void mutateDom(String stateName) {
		File domFile = new File(this.domLocation, stateName + ".html");
		Document dom;
		try {
			dom = DomUtils.asDocument(FileUtils.readFileToString(domFile));
			MutationRecord mutated = mutateDocument(dom);
			if (mutated == null) {
				LOGGER.error("Error mutating state :" + stateName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MutationRecord applyMutationToCandidate(MutationCandidate candidate) {
		if (candidate == null || candidate.getOperator() == null || candidate.getNodeToMutate() == null) {
			return new MutationRecord(false, "Invalid Mutation Candidate");
		}
		Operator operator = candidate.getOperator();
		MutationRecord record = applyDomOperator(candidate.getNodeToMutate(), operator,
				candidate.getOrigNodeProperties());
		candidate.setRecord(record);
		return record;
	}

	public MutationRecord applyDomOperator(Node toMutate, Operator domOperator, NodeProperties nodeProperties) {
		MutationRecord record = new MutationRecord(toMutate.getOwnerDocument(), domOperator.getClass().getSimpleName(),
				toMutate, null, nodeProperties);
		// record.setOriginalXpath(XPathHelper.getXPathExpression(toMutate));
		record.setOriginalXpath(XPathHelper.getSkeletonXpath(toMutate));

		Node mutated = ((DomOperator) domOperator).applyOperator(toMutate, record);
		if (mutated != null) {
			record.setSuccess(true);
			// record.setMutatedXpath(XPathHelper.getXPathExpression(mutated));
			record.setMutatedXpath(XPathHelper.getSkeletonXpath(mutated));

			LOGGER.info("Mutation succesful, {}", record);
			tries = 0;
			addMutationAttributes(domOperator, mutated);
			return record;
		} else {
			LOGGER.info("Mutation unsuccesful. Trying again!!");
			return new MutationRecord(false, "Error applying operator to given node");
		}

	}

	MutationRecord applyDomOperator(Document dom, Operator domOperator) {
		Node toMutate = nodePicker.pickNode(dom, domOperator);
		if (toMutate == null) {
			LOGGER.info("No nodes available for " + domOperator.getClass().getSimpleName());
			tries += 1;
			if (tries < MAX_RETRIES) {
				return mutateDocument(dom);
			} else {
				LOGGER.error("Mutation Failed. returning null!!");
				return new MutationRecord(false, "Exceeded max retries trying to mutate");
				// return null;
			}
		}
		MutationRecord record = applyDomOperator(toMutate, domOperator, null);
		if (!record.success) {
			tries += 1;
			if (tries < MAX_RETRIES) {
				return mutateDocument(dom);
			} else {
				LOGGER.error("Mutation Failed. returning null!!");
				tries = 0;
				return new MutationRecord(false, "Exceeded max retries trying to mutate");
			}
		}
		return record;
	}

	private void addMutationAttributes(Operator domOperator, Node toMutate) {
		/**
		 * add an attribute to identify mutated node and the operator applied TODO: add
		 * mutation ID in case there is a need to identify caller
		 */
		Document dom = toMutate.getOwnerDocument();
		Node mutationAttribute = dom.createAttribute(MUTATION_ATTRIBUTE);
		Node mutationOperatorAttribute = dom.createAttribute(MUTATION_OPERATOR_ATTRIBUTE);
		if (toMutate.getNodeName().equalsIgnoreCase("#text")) {
			toMutate = toMutate.getParentNode();
		}
		toMutate.getAttributes().setNamedItem(mutationAttribute);
		toMutate.getAttributes().setNamedItem(mutationOperatorAttribute);

		toMutate.getAttributes().getNamedItem(MUTATION_ATTRIBUTE).setNodeValue("yes");

		toMutate.getAttributes().getNamedItem(MUTATION_OPERATOR_ATTRIBUTE)
				.setNodeValue(domOperator.getClass().getSimpleName());
	}

	private Operator pickDomOperator() {
		int operatorNumber = Math.abs((int) (random.nextLong() % getDomOperators().size()));
		System.out.println("DOM operators available " + getDomOperators().size());

		System.out.println("DOM operator  " + operatorNumber);
		System.out.println("Picked : " + getDomOperators().get(operatorNumber));
		return getDomOperators().get(operatorNumber);
	}

	/*
	 * node properties not applied
	 */
	/**
	 * Transfer mutation of one dom to another ( both are similar )
	 * 
	 * @param mutatedDOM
	 * @param doc
	 * @return true if successful or false otherwise
	 */
	public List<MutationRecord> transferMutation(MutationRecord oldRecord, Document doc) {
		List<MutationRecord> transfers = new ArrayList<MutationRecord>();
		try {
			String xpath = oldRecord.originalXpath;
			String operator = oldRecord.operator;
			Object data = oldRecord.getData();
			MutationRecord record = applyMutation(doc, xpath, operator, data);
			record.setOrigNodeProperties(oldRecord.origNodeProperties);
			transfers.add(record);
			return transfers;

		} catch (XPathExpressionException e) {
			LOGGER.error("Could not transfer mutation {}", e.getMessage());
		}
		return transfers;
	}

	/*
	 * node properties not applied
	 */
	public MutationRecord applyMutation(Document doc, String xpath, String operator, Object data)
			throws XPathExpressionException {

		DomOperator domOperator = getDomOperator(operator);

		if (domOperator == null) {
			LOGGER.error("Could not find the operator applied on the node " + operator);
			LOGGER.error("Could not transfer mutation of node {}  ", xpath);
			return null;
		}

		NodeList toMutate = XPathHelper.evaluateXpathExpression(doc, xpath);

		if (toMutate.getLength() >= 1) {
			MutationRecord record = new MutationRecord(doc, operator, toMutate.item(0), data);
			record.setOriginalXpath(xpath);
			Node applied = domOperator.applyOperator(toMutate.item(0), record);
			if (applied != null) {
				record.setSuccess(true);
				record.setMutatedXpath(XPathHelper.getSkeletonXpath(applied));
				addMutationAttributes(domOperator, applied);
				LOGGER.info("Mutation Transfer success for {} ", XPathHelper.getXPathExpression(applied));
			} else {
				record.setSuccess(false);
				record.setError("Mutation could not be applied to the node with given operator");
				LOGGER.error("Could not transfer mutation for {}", xpath);
			}
			return record;
		}
		MutationRecord error = new MutationRecord(false, "No nodes found for the given xpath in document ");
		error.setOriginalXpath(xpath);
		error.setDoc(doc);
		error.setOperator(operator);
		return error;
	}

	public DomOperator getDomOperator(String operator) {
		// try {
		// Class operatorClass = Class.forName("com.saltlab.webmutator.operators.dom." +
		// operator);
		// DomOperator domOperator = (DomOperator) operatorClass.newInstance();
		// return domOperator;
		// } catch (ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InstantiationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		for (DomOperator op : getDomOperators()) {
			if (op.getClass().getSimpleName().equalsIgnoreCase(operator)) {
				return op;
			}
		}
		return null;

		/*
		 * DomOperator domOperator = null;
		 * 
		 * switch(operator) { case "ActionableNodeMutator": domOperator = new
		 * ActionableNodeMutator(); break; case "AttributeMutator": domOperator = new
		 * AttributeMutator(); break; case "ContainerNodeMutator": domOperator = new
		 * ContainerNodeMutator(); break; case "TextNodeMutator": domOperator = new
		 * TextNodeMutator(); break; case "TagMutator": domOperator = new TagMutator();
		 * break; case "SubtreeMutator": domOperator= new SubtreeDelete(); break;
		 * default: domOperator = null; break; } return domOperator;
		 */
	}

	public MutationRecordEx transferMutation(MutationRecord record, WebDriver d) {
		String xpath = record.originalXpath;
		String operator = record.operator;
		MutationRecordEx newRecord = applyMutation(d, xpath, operator, record.getData());
		if (newRecord.success) {
			newRecord.mutatedXpath = record.mutatedXpath;
		}
		newRecord.setOrigNodeProperties(record.origNodeProperties);
		return newRecord;
	}

	private MutationRecordEx applyMutation(WebDriver d, String xpath, String operator, Object data) {
		DomOperator domOperator = getDomOperator(operator);

		if (domOperator == null) {
			LOGGER.error("Could not find the operator applied on the node " + operator);
			LOGGER.error("Could not transfer mutation of node {}  ", xpath);
			return null;
		}

		WebElement toMutate = d.findElement(By.xpath(xpath));

		String dom = DomUtils.getStrippedDom(d.getPageSource());
		if (toMutate != null) {

			MutationRecordEx record = new MutationRecordEx(new MutationRecord(dom, operator, xpath, data));

			record.setOriginalXpath(xpath);
			record.setVisible(toMutate.isDisplayed());
			WebElement applied = domOperator.applyOperator(d, toMutate, record);
			if (applied != null) {
				record.setSuccess(true);
				// mutation is visible if element is displayed before or after mutation.
				record.setVisible(record.visible || applied.isDisplayed());
				LOGGER.info("Mutation Transfer success for {} ", applied.getText());
			} else {
				record.setSuccess(false);
				record.setError("Mutation could not be applied to the node with given operator");
				LOGGER.error("Could not transfer mutation for {}", xpath);
			}
			return record;
		}
		MutationRecordEx error = new MutationRecordEx(false, "Web Element not found for the xpath ");
		error.setOriginalXpath(xpath);
		error.setOperator(operator);
		return error;
	}

	public NodePickerPlugin getNodePicker() {
		// TODO Auto-generated method stub
		return nodePicker;
	}

	public int getRandom(int size) {
		return Math.abs((int) (random.nextLong() % size));

	}

	public void setEventListeners(Set<String> eventListeners) {
		this.eventListeners = eventListeners;
	}

	public Set<String> getEventListeners() {
		return eventListeners;
	}

	public void setTextTokens(Set<String> textTokens) {
		this.textTokens = textTokens;
	}

	public void setAttributeTokens(Map<String, Set<String>> attributeTokens) {
		this.attributeTokens = attributeTokens;
	}

	/*
	 * For Generating Mutants, call this after providing random tokens for EventListeners, Attributes, Text
	 */
	public void initOperators() {
		/*Set<String> attrTokens = null;
		if(attributeTokens!=null) {
			attrTokens = new HashSet<>();
			for(Set<String> mapVal: attributeTokens.values()) {
				attrTokens.addAll(mapVal);
			}
		}*/
	
		this.domOperators = new DomOperators(eventListeners, attributeTokens, textTokens)
									.getDomOperators(excludedOperators);
	}
}
