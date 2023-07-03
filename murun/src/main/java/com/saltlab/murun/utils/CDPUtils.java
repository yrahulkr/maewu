package com.saltlab.murun.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.saltlab.webmutator.utils.DomUtils;
import com.saltlab.webmutator.utils.XPathHelper;

public class CDPUtils {
	private static final Logger LOG = LoggerFactory.getLogger(CDPUtils.class);


	private static final String RECTANGLE = "rectangle";
	private static final String ISDISPLAYED = "isdisplayed";

	private static final String BGCOLOR = "bgcolor";
	private static final String FONTWEIGHT = "fontweight";
	private static final String FONTSIZE = "fontsize";
	private static final String BACKGROUNDCOLOR = "background-color";
	
	private static final String POPULATED = "populated";
	
	public static ArrayList<Node> getChildren(Node vipsBlock) {
		if(vipsBlock == null || vipsBlock.getNodeName().equalsIgnoreCase("#text") || vipsBlock.getChildNodes() == null) {
			return new ArrayList<>();
		}
		NodeList childNodes = vipsBlock.getChildNodes();
		ArrayList<Node> children = new ArrayList<Node>();
		for (int i = 0; i<childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if(child == null) {
				continue;
			}
			if(child.getNodeName().equalsIgnoreCase("#text")) {
				if(child.getTextContent().trim().isEmpty())
					continue;
			}
			children.add(child);
		}
		return children;
	}
	
	/**
	 * return xpaths of all children
	 * 
	 * @param node
	 * @return
	 */
	public static List<String> getXpathList(Node node){
		List<String> returnList = new ArrayList<String>();
		if(node.getNodeName().equalsIgnoreCase("#text")) {
			return returnList;
		}
		returnList.add(XPathHelper.getXPathExpression(node));
		List<Node> children = getChildren(node);
		for(Node child: children) {
			if(child.getNodeName().equalsIgnoreCase("#text")) {
				continue;
			}
			returnList.addAll(getXpathList(child));
		}
		return returnList;
	}
	static String COMPUTEDSTYLESHEET_ALL = "Array.from(%s).map(element => {return {xpath: element, attributes: getVipsAttributes(element)}});";


	public static void populateStyle(Document dom, ChromiumDriver driver) {
		Path cdpScript = Paths.get("src", "main", "resources", "cdpScript.js");
		File scriptFile = new File(cdpScript.toString());
		if(!scriptFile.exists()) {
			LOG.error("Could not find Vips Script at {}", scriptFile.getAbsolutePath());
			return;
		}
		String script="";
		try {
			script =  FileUtils.readFileToString(new File(cdpScript.toString()));
			
		} catch (FileNotFoundException e) {
			LOG.error("Could not find CDP Script at {}", cdpScript);
			LOG.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> xpaths = getXpathList(dom.getElementsByTagName("body").item(0));
		LOG.info("Sending {} xpaths", xpaths.size());
		Gson gson = new Gson();
		String xpathString = gson.toJson(xpaths);
		String executeScript = script +  String.format(COMPUTEDSTYLESHEET_ALL, xpathString);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("expression", executeScript);
		parameters.put("includeCommandLineAPI", Boolean.TRUE);
		parameters.put("returnByValue", Boolean.TRUE);
		Map<String, Object> attributeString = driver.executeCdpCommand("Runtime.evaluate", parameters);
		LOG.info("{}",attributeString);

//		Gson gson = new Gson();
//		List<Map<String, VipsBrowserAttributes>> attributes = gson.fromJson( attributeString, new TypeToken<List<Map<String, VipsBrowserAttributes>>>(){}.getType());
//		LOG.debug(attributes);
	
		Map<String, DynamicBrowserAttributes> attributeMap = new HashMap<String, DynamicBrowserAttributes>();
		
		if(attributeString instanceof Collection) {
			LOG.info("Found {} attribute objects", ((Collection) attributeString).size()); 
			for(Object elementSheet: (Collection)attributeString) {
				if(elementSheet instanceof Map) {
					String xpath = (String)((Map)elementSheet).get("xpath");
					LOG.debug(xpath);
//					if(webElement instanceof WebElement) {
//						String bgColor = ((WebElement)webElement).getCssValue(BACKGROUNDCOLOR);
//						LOG.debug(bgColor);
//					}
					Object attributes = ((Map)elementSheet).get("attributes");
					LOG.debug("attributes{}", attributes);
					if(attributes instanceof Map) {
						if(((Map) attributes).isEmpty()) {
							LOG.debug("Empty attributes found for {}", xpath);
							continue;
						}
						Object rectangle = ((Map)attributes).get(RECTANGLE);
						Rectangle rect  = null;
						if(rectangle instanceof Map) {
							rect = new Rectangle(
									(int)(long)((Map)rectangle).get("x"), 
									(int)(long)((Map)rectangle).get("y"),
									(int)(long)((Map)rectangle).get("width"),
									(int)(long)((Map)rectangle).get("height")
								);
							LOG.debug(rect.toString());
						}
						else {
							rect = new Rectangle(-1,-1,-1,-1);
						}
							
						
						int fontSize = (int) (long)((Map)attributes).get(FONTSIZE);
						int fontWeight = (int) (long)((Map)attributes).get(FONTWEIGHT);
						String bgColor = (String) ((Map)attributes).get(BGCOLOR);
						boolean isDisplayed = (boolean)((Map)attributes).get(ISDISPLAYED);
						LOG.debug("rectangle {}", rectangle);
						LOG.debug("font size {}",fontSize);
						LOG.debug("font weight {}",fontWeight);
						LOG.debug("bg color {}",bgColor);
						attributeMap.put(xpath, new DynamicBrowserAttributes(rect, fontSize, fontWeight, bgColor, isDisplayed));
					}
				}
				LOG.debug(elementSheet.toString());
			}
		}
		
		for(String xpath: xpaths) {
			try {
				NodeList nodes = XPathHelper.evaluateXpathExpression(dom, xpath);
				if(nodes.getLength()==1) {
					Node vipsBlock = nodes.item(0);
					// Apply attributes
					if(attributeMap.containsKey(xpath)) {
						setBrowserAttributes(vipsBlock, attributeMap.get(xpath));
					}
				}
			} catch (XPathExpressionException e) {
				LOG.error("Error while setting browser attribtues to document");
				LOG.error(e.getMessage());
			}
		}
		
		setPopulated(dom);
	}

	private static void setBrowserAttributes(Node vipsBlock, DynamicBrowserAttributes browserAttributes) {
		setFontSize(vipsBlock, browserAttributes.getFontsize());
		setFontWeight(vipsBlock, browserAttributes.getFontweight());
		setCssProperty(vipsBlock, BGCOLOR, browserAttributes.getBgcolor());
		setCssProperty(vipsBlock, BACKGROUNDCOLOR, browserAttributes.bgcolor);
		boolean isDisplayed = browserAttributes.isDisplayed() && browserAttributes.rectangle!=null &&
				browserAttributes.rectangle.height>0 && browserAttributes.rectangle.width>0;
		setIsDisplayed(vipsBlock, isDisplayed?"true":"false");
		setRectangle(vipsBlock, browserAttributes.rectangle);
	}

	public static String[] getVipsAttributes(){
		String[] returnArray  = {RECTANGLE, ISDISPLAYED, 
				 BGCOLOR, FONTSIZE, FONTWEIGHT, BACKGROUNDCOLOR, POPULATED};
		return returnArray;
	}

//	public static Document dom = null;
//	public static VipsSelenium vips = null;
//	
	public static void cleanDom(Document dom) {
		Node root = dom.getDocumentElement();
		cleanNode(root);
		root.normalize();
	}
	
	
	private static void cleanNode(Node root) {
		
		NodeList childNodes = root.getChildNodes();
		ArrayList<Node> toRemove = new ArrayList<Node>();
		for(int i = 0; i<childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			if(childNode.getAttributes() == null) {
				if(childNode.getNodeName().equalsIgnoreCase("#text")) {
					if(!childNode.getTextContent().trim().isEmpty()) {
						continue;
					}
				}
				toRemove.add(childNode);
			}
			else {
				cleanNode(childNode);
			}
		}
		for(Node toRemoveNode : toRemove) {
			toRemoveNode.getParentNode().removeChild(toRemoveNode);
		}

	}

	
	public static boolean checkIsImg(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("img"))
			return true;
		else
			return false;
	}
	
	

	public static void setRectangle(Node vipsBlock, Rectangle rect) {
		Gson gson = new Gson();
		String rectString = gson.toJson(rect);
		Node newAttribute = vipsBlock.getOwnerDocument().createAttribute(RECTANGLE);
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem(RECTANGLE).setNodeValue(rectString);
	}

	public static boolean isDisplayed(Node vipsBlock, WebDriver driver) {
		if(vipsBlock.getNodeName().equalsIgnoreCase("#text"))
			return isDisplayed(vipsBlock.getParentNode(), driver);
		
		if (vipsBlock.getAttributes().getNamedItem(ISDISPLAYED) != null) {
			String str = vipsBlock.getAttributes().getNamedItem(ISDISPLAYED).getNodeValue();
			return str.equalsIgnoreCase("true");
		}		
		
		if(driver ==null) {
			setDisplayedUsingRectangle(vipsBlock);
			LOG.debug("Cannot find isDisplayed for {}. Using rectangle", vipsBlock.getNodeName());
			return isDisplayed(vipsBlock, driver);
		}
		
		boolean isDisplayed = false;
		try {
			String xpath = XPathHelper.getXPathExpression(vipsBlock);
			WebElement element = driver.findElement(By.xpath(xpath));
			isDisplayed = element.isDisplayed();
		}catch(Exception ex) {
			
		}
		
		setIsDisplayed(vipsBlock, isDisplayed?"true":"false");
		return isDisplayed;
	}
	
	private static void setDisplayedUsingRectangle(Node vipsBlock) {
		Rectangle rectangle = getRectangle(vipsBlock, null);
		boolean isDisplayed = rectangle!=null &&
				rectangle.height>0 && rectangle.width>0;
		setIsDisplayed(vipsBlock, isDisplayed?"true":"false");

	}

	public static void setIsDisplayed(Node vipsBlock, String value) {
		Node newAttribute = vipsBlock.getOwnerDocument().createAttribute(ISDISPLAYED);
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem(ISDISPLAYED).setNodeValue(value);
	}
	

	public static boolean isTextBox(Node vipsBlockChild) {
		return vipsBlockChild.getNodeName().equalsIgnoreCase("#text");
	}

	
	private static int getNumerals(String string) {
		return Integer.parseInt(string.replaceAll("[^0-9]", ""));
	}

	
	
	public static Rectangle getRectangle(Node vipsBlock, WebDriver driver) {
		if(vipsBlock==null)
			return null;
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return getRectangle(vipsBlock.getParentNode(), driver);
		}
		if (vipsBlock.getAttributes().getNamedItem(RECTANGLE) != null) {
			Gson gson = new Gson();
			String rectString = vipsBlock.getAttributes().getNamedItem(RECTANGLE).getNodeValue();
			Rectangle rect =  gson.fromJson(rectString, Rectangle.class);
//			LOG.debug(rect);
			return rect;
		}		
		return null;
	}
	
	

	

	private static void setCssProperty(Node vipsBlock, String property, String propertyValue) {
		Node newAttribute = vipsBlock.getOwnerDocument().createAttribute(property);
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem(property).setNodeValue(propertyValue);
	}
	
	public static void setFontSize(Node vipsBlock, int fontSize) {
		Node newAttribute = vipsBlock.getOwnerDocument().createAttribute(FONTSIZE);
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem(FONTSIZE).setNodeValue(Integer.toString(fontSize));
	}
	
	public static int getFontSize(Node vipsBlock, WebDriver driver) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text") || vipsBlock.getNodeName().equalsIgnoreCase("text")) {
			return getFontSize(vipsBlock.getParentNode(), driver);
		}
		if (vipsBlock.getAttributes().getNamedItem(FONTSIZE) != null) {
			
			String propertyValue = vipsBlock.getAttributes().getNamedItem(FONTSIZE).getNodeValue();
			
			return Integer.parseInt(propertyValue);
		}	
		
		String xpath = XPathHelper.getXPathExpression(vipsBlock);
		WebElement element = driver.findElement(By.xpath(xpath));
		String propertyValue = element.getCssValue("fontSize");
		propertyValue = propertyValue.replaceAll("\\D+","");
		int fontSize = Integer.parseInt(propertyValue);
		setFontSize(vipsBlock, fontSize);
		return fontSize;
	}
	
	private static String getCssProperty(Node vipsBlock, String property, WebDriver driver) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text") || vipsBlock.getNodeName().equalsIgnoreCase("text")) {
			return getCssProperty(vipsBlock.getParentNode(), property, driver);
		}
		if (vipsBlock.getAttributes().getNamedItem(property.toLowerCase()) != null) {
			
			String propertyValue = vipsBlock.getAttributes().getNamedItem(property.toLowerCase()).getNodeValue();
			
			return propertyValue;
		}	
		
		if(driver ==null) {
			return "";
		}
		String xpath = XPathHelper.getXPathExpression(vipsBlock);
		WebElement element = driver.findElement(By.xpath(xpath));
		String propertyValue = element.getCssValue(property);
		setCssProperty(vipsBlock, property.toLowerCase(), propertyValue);
		return propertyValue;
	}
	

	/**
	 * returns true if parent and child are the same node
	 * @param parent
	 * @param child
	 * @return
	 */
	public static boolean contains(Node parent, Node child) {
		if(parent == null || child==null) {
			return false;
		}
		
		if((parent.compareDocumentPosition(child) & Document.DOCUMENT_POSITION_CONTAINED_BY)  == 0) {
			if(!parent.isSameNode(child)){
				return false;
			}
		}
		return true;
	}
	
	public static Node getParentBox(List<Node> nestedBlocks) {
//		for(Node nestedBlock: nestedBlocks) {
//			LOG.debug(XPathHelper.getSkeletonXpath(nestedBlock));
//		}
//		
		if(nestedBlocks == null) {
			return null;
		}
		if(nestedBlocks.isEmpty()) {
			return null;
		}
		
		Node tempBlock = nestedBlocks.get(0);
		while(tempBlock!= tempBlock.getOwnerDocument().getDocumentElement()) {
			LOG.debug("Trying : " + XPathHelper.getSkeletonXpath(tempBlock));
			boolean allContains = true;
			for(Node block: nestedBlocks) {
				if((tempBlock.compareDocumentPosition(block) & Document.DOCUMENT_POSITION_CONTAINED_BY)  == 0) {
					if(!block.isSameNode(tempBlock)){
						LOG.debug(XPathHelper.getSkeletonXpath(block));
						allContains = false;
						break;
					}
				}
			}
			
			if(allContains)
				return tempBlock;
			tempBlock = tempBlock.getParentNode();
		}
		
		return null;
	}
	
	
	public static boolean isValidRectangle(Rectangle rect) {
		if(rect.x <0 || rect.y <0 || rect.width <=0 || rect.height<=0)
			return false;
		
		return true;
	}
	
	public static void exportFragment(BufferedImage pageViewPort, File target, Rectangle rect) {
		try {
			BufferedImage subImage = pageViewPort.getSubimage(rect.x, rect.y, rect.width, rect.height);
			
			saveToImage(subImage, target);
		}catch(Exception ex) {
			LOG.error("Error exporting rectangle to image " + rect);
			LOG.debug(ex.getStackTrace().toString());
		}
	}
	
	

	public static void saveToImage(BufferedImage image , File target)
	{
		try
		{
			ImageIO.write(image, "PNG", target);
		} catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	
	
	public static void setFontWeight(Node vipsBlock, int i) {
		Node newAttribute = vipsBlock.getOwnerDocument().createAttribute(FONTWEIGHT);
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem(FONTWEIGHT).setNodeValue(Integer.toString(i));
	}
	
	public static String getFontWeight(Node vipsBlock, WebDriver driver) {
		return getCssProperty(vipsBlock, FONTWEIGHT, driver);
	}
	
	public static String getBgColor(Node vipsBlock, WebDriver driver) {
		return getCssProperty(vipsBlock, BGCOLOR, driver);
	}

	public static String getBackgroundColor(Node vipsBlock, WebDriver driver) {
		return getCssProperty(vipsBlock, BACKGROUNDCOLOR, driver);
	}
	public static boolean isPopulated(Document dom) {
		if(dom == null || dom.getDocumentElement()==null) {
			return false;
		}
		return dom.getDocumentElement().hasAttribute(POPULATED);
	}
	
	public static void setPopulated(Document dom) {
		if(dom == null) {
			return;
		}
		Node vipsBlock = dom.getDocumentElement();
		Node newAttribute = vipsBlock.getOwnerDocument().createAttribute(POPULATED);
		vipsBlock.getAttributes().setNamedItem(newAttribute);
	}
	
	public static void removeVipsAttributes(Node node) {
		if(node==null || !node.hasAttributes() || node.getAttributes() == null) {
			// Skip attribute removal
		}
		else {
			for(String attribute: getVipsAttributes()) {
				try {
					node.getAttributes().removeNamedItem(attribute);
				}catch(Exception ex) {
					// Attribute doesn't exist
					LOG.debug("No such attribute {}", attribute);
				}
			}
		}
		
		if(node!=null && node.getChildNodes().getLength()>0) {
			for(Node child: getChildren(node)) {
				removeVipsAttributes(child);
			}
		}
		
	}

	public static String removeVipsAttributes(String dom) {
		Document doc = null;
		try {
			doc = DomUtils.asDocument(dom);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(doc == null) {
			return null;
		}
		removeVipsAttributes(doc.getDocumentElement());
		try {
			return DomUtils.getDocumentToString(doc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}