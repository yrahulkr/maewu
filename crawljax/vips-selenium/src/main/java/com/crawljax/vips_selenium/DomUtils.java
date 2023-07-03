package com.crawljax.vips_selenium;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.crawljax.util.XPathHelper;
import com.google.gson.Gson;

public class DomUtils {
	public static Document dom = null;
	public static VipsSelenium vips = null;
	
	public static void cleanDom(Document dom) {
		Node root = dom.getDocumentElement();
		cleanNode(root);
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

	public static ArrayList<Node> getChildren(Node vipsBlock) {
		NodeList childNodes = vipsBlock.getChildNodes();
		ArrayList<Node> children = new ArrayList<Node>();
		for (int i = 0; i<childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if(child.getNodeName().equalsIgnoreCase("#text")) {
				if(child.getTextContent().trim().isEmpty())
					continue;
			}
			children.add(child);
		}
		return children;
	}
	
	public static void setDoC(Node vipsBlock, int i) {
		Node newAttribute = dom.createAttribute("doc");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem("doc").setNodeValue(Integer.toString(i));
	}
	public static int getDoC(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return getDoC(vipsBlock.getParentNode());
		}
		String value = vipsBlock.getAttributes().getNamedItem("doc").getNodeValue();
		return Integer.parseInt(value);
	}
	public static void setIsVisualBlock(Node vipsBlock, boolean b) {
		Node newAttribute = dom.createAttribute("isvisualblock");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem("isvisualblock").setNodeValue(b?"true":"false");
		checkProperties(vipsBlock);
	}

	public static void checkProperties(Node vipsBlock)
	{
		checkIsImg(vipsBlock);
		checkContainImg(vipsBlock);
		checkContainTable(vipsBlock);
		checkContainP(vipsBlock);
		countLinkTextLength(vipsBlock);
		setSourceIndex(vipsBlock);
	}
	
	public static void setSourceIndex(Node vipsBlock) {
		// TODO Auto-generated method stub
		
	}
	public static void countLinkTextLength(Node vipsBlock) {
		int ltl = 0;
		if (vipsBlock.getNodeName().equalsIgnoreCase("a"))
		{
			ltl+= vipsBlock.getTextContent().trim().length();
		}
		NodeList childNodes = vipsBlock.getChildNodes();
		for(int i=0; i<childNodes.getLength(); i++) {
			Node childVipsBlock = childNodes.item(i);
			if (childVipsBlock.getNodeName().equalsIgnoreCase("a"))
			{
				ltl+=childVipsBlock.getTextContent().trim().length();
			}
		}
		setLinkTextLength(vipsBlock, ltl);
	}
	
	public static void checkContainP(Node vipsBlock) {
		int containP = 0;
		if (vipsBlock.getNodeName().equalsIgnoreCase("p"))
		{
			containP++;
		}
		NodeList childNodes = vipsBlock.getChildNodes();
		for(int i=0; i<childNodes.getLength(); i++) {
			Node childVipsBlock = childNodes.item(i);
			if (childVipsBlock.getNodeName().equalsIgnoreCase("p"))
			{
				containP++;
			}
		}
		setPCount(vipsBlock, containP);
	}
	public static void checkContainTable(Node vipsBlock) {
		boolean containTable = false;
		if (vipsBlock.getNodeName().equalsIgnoreCase("table"))
			containTable = true;

		NodeList childNodes = vipsBlock.getChildNodes();
		for(int i=0; i<childNodes.getLength(); i++) {
			Node childVipsBlock = childNodes.item(i);
			if (childVipsBlock.getNodeName().equalsIgnoreCase("table"))
			containTable = true;
		}
		if(containTable)
			setContainsTable(vipsBlock);
	}
	
	public static void checkContainImg(Node vipsBlock) {
		int containImg = 0;
		if (vipsBlock.getNodeName().equalsIgnoreCase("img"))
		{
			containImg++;
		}
		NodeList childNodes = vipsBlock.getChildNodes();
		for(int i=0; i<childNodes.getLength(); i++) {
			Node childVipsBlock = childNodes.item(i);
			if(checkIsImg(childVipsBlock))
				containImg++;
		}
		setImageCount(vipsBlock, containImg);
	}
	
	public static boolean checkIsImg(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("img"))
			return true;
		else
			return false;
	}
	
	
	
	public static void setAlreadyDivided(Node vipsBlock) {
		Node newAttribute = dom.createAttribute("alreadydivided");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
	}
	

	public static boolean isAlreadyDivided(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return isAlreadyDivided(vipsBlock.getParentNode());
		}
		return vipsBlock.getAttributes().getNamedItem("alreadydivided") !=null;
	}
	
	public static void setContainsTable(Node vipsBlock) {
		Node newAttribute = dom.createAttribute("containstable");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
	
	}
	
	public static boolean containsTable(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return containsTable(vipsBlock.getParentNode());
		}
		return vipsBlock.getAttributes().getNamedItem("containstable") !=null;
	}
	
	public static void setRectangle(Node vipsBlock, Rectangle rect) {
		Gson gson = new Gson();
		String rectString = gson.toJson(rect);
		Node newAttribute = dom.createAttribute("rectangle");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem("rectangle").setNodeValue(rectString);
	}

	public static boolean isDisplayed(Node vipsBlock) {
		if (vipsBlock.getAttributes().getNamedItem("isdisplayed") != null) {
			String str = vipsBlock.getAttributes().getNamedItem("isdisplayed").getNodeValue();
			return str.equalsIgnoreCase("true");
		}		
		
		String xpath = XPathHelper.getXPathExpression(vipsBlock);
		WebElement element = vips.driver.findElement(By.xpath(xpath));
		boolean isDisplayed = element.isDisplayed();
		setIsDisplayed(vipsBlock, isDisplayed?"true":"false");
		return isDisplayed;
	}
	
	public static void setIsDisplayed(Node vipsBlock, String value) {
		Node newAttribute = dom.createAttribute("isdisplayed");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem("isdisplayed").setNodeValue(value);
	}
	

	public static void setIsDividable(Node vipsBlock, boolean b) {
		Node newAttribute = dom.createAttribute("isdividable");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem("isdividable").setNodeValue(b?"true":"false");
	}

	static boolean isDividable(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return isDividable(vipsBlock.getParentNode());
		}
		if(vipsBlock.getAttributes().getNamedItem("isvisualblock")!=null) {
			String value = vipsBlock.getAttributes().getNamedItem("isdividable").getNodeValue();
			return value.trim().equalsIgnoreCase("true");
		}
		return vipsBlock.getAttributes().getNamedItem("isdividable") !=null;
	}
	
	public static boolean isTextBox(Node vipsBlockChild) {
		return vipsBlockChild.getNodeName().equalsIgnoreCase("#text");
	}

	public static boolean isVisualBlock(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return isVisualBlock(vipsBlock.getParentNode());
		}
		if(vipsBlock.getAttributes().getNamedItem("isvisualblock")!=null) {
			String value = vipsBlock.getAttributes().getNamedItem("isvisualblock").getNodeValue();
			return (value.trim().equalsIgnoreCase("true"));
			
		}
		
		return vipsBlock.getAttributes().getNamedItem("isvisualblock") != null;
	}

	public static Rectangle getRectangle(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return getRectangle(vipsBlock.getParentNode());
		}
		if (vipsBlock.getAttributes().getNamedItem("rectangle") != null) {
			Gson gson = new Gson();
			String rectString = vipsBlock.getAttributes().getNamedItem("rectangle").getNodeValue();
			Rectangle rect =  gson.fromJson(rectString, Rectangle.class);
//			System.out.println(rect);
			return rect;
		}		
		
		String xpath = XPathHelper.getXPathExpression(vipsBlock);
		WebElement element = vips.driver.findElement(By.xpath(xpath));
		Rectangle rect = element.getRect();
		setRectangle(vipsBlock, rect);
		return rect;
	}
	
	public static void setCssProperty(Node vipsBlock, String property, String propertyValue) {
		Node newAttribute = dom.createAttribute(property);
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem(property).setNodeValue(propertyValue);
	}
	
	public static void setFontSize(Node vipsBlock, int fontSize) {
		Node newAttribute = dom.createAttribute("fontsize");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem("fontsize").setNodeValue(Integer.toString(fontSize));
	}
	
	public static int getFontSize(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return getFontSize(vipsBlock.getParentNode());
		}
		if (vipsBlock.getAttributes().getNamedItem("fontsize") != null) {
			
			String propertyValue = vipsBlock.getAttributes().getNamedItem("fontsize").getNodeValue();
			
			return Integer.parseInt(propertyValue);
		}	
		
		String xpath = XPathHelper.getXPathExpression(vipsBlock);
		WebElement element = vips.driver.findElement(By.xpath(xpath));
		String propertyValue = element.getCssValue("fontSize");
		propertyValue = propertyValue.replaceAll("\\D+","");
		int fontSize = Integer.parseInt(propertyValue);
		setFontSize(vipsBlock, fontSize);
		return fontSize;
	}
	
	public static String getCssProperty(Node vipsBlock, String property) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return getCssProperty(vipsBlock.getParentNode(), property);
		}
		if (vipsBlock.getAttributes().getNamedItem(property.toLowerCase()) != null) {
			
			String propertyValue = vipsBlock.getAttributes().getNamedItem(property.toLowerCase()).getNodeValue();
			
			return propertyValue;
		}	
		
		String xpath = XPathHelper.getXPathExpression(vipsBlock);
		WebElement element = vips.driver.findElement(By.xpath(xpath));
		String propertyValue = element.getCssValue(property);
		setCssProperty(vipsBlock, property.toLowerCase(), propertyValue);
		return propertyValue;
	}
	
	public static void setImageCount(Node vipsBlock, int containImg) {
		Node newAttribute = dom.createAttribute("imagecount");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem("imagecount").setNodeValue(Integer.toString(containImg));
	}
	


	public static int getImageCount(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return getImageCount(vipsBlock.getParentNode());
		}
		String value = vipsBlock.getAttributes().getNamedItem("imagecount").getNodeValue();
		return Integer.parseInt(value);
	}

	public static void setPCount(Node vipsBlock, int containP) {
		Node newAttribute = dom.createAttribute("pcount");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem("pcount").setNodeValue(Integer.toString(containP));
	}

	public static int getPCount(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return getPCount(vipsBlock.getParentNode());
		}
		String value = vipsBlock.getAttributes().getNamedItem("pcount").getNodeValue();
		return Integer.parseInt(value);
	}

	public static int getTextLength(Node vipsBlock) {
		String text = vipsBlock.getTextContent().trim();
		return text.length();
	}
	
	public static void setLinkTextLength(Node vipsBlock, int linkTextLength) {
		Node newAttribute = dom.createAttribute("linktextlength");
		vipsBlock.getAttributes().setNamedItem(newAttribute);
		vipsBlock.getAttributes().getNamedItem("linktextlength").setNodeValue(Integer.toString(linkTextLength));
	}

	public static int getLinkTextLength(Node vipsBlock) {
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return getLinkTextLength(vipsBlock.getParentNode());
		}
		String value = vipsBlock.getAttributes().getNamedItem("linktextlength").getNodeValue();
		return Integer.parseInt(value);
	}
	
}
