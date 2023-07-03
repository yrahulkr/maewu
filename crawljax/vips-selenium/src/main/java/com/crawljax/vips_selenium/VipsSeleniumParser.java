package com.crawljax.vips_selenium;

import java.awt.image.BufferedImage;

/*
 * Tomas Popela, 2012
 * VIPS - Visual Internet Page Segmentation
 * Module - VipsParser.java
 */


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.crawljax.util.XPathHelper;
import com.google.gson.Gson;
/**
 * Class that parses blocks on page and finds visual blocks.
 * @author Tomas Popela
 *
 */
public class VipsSeleniumParser {
	private VipsSelenium vips;
	private Node _vipsBlocks = null;
	private Node _currentVipsBlock = null;
	private Node _tempVipsBlock = null;
	private Document dom = null;

	private int _sizeTresholdWidth = 0;
	private int _sizeTresholdHeight = 0;
	private BufferedImage _viewport = null;
	private int _visualBlocksCount = 0;
	private int _pageWidth = 0;
	private int _pageHeight = 0;

	/**
	 * Default constructor
	 * 
	 * @param viewport Rendered's page viewport
	 */
	public VipsSeleniumParser(VipsSelenium vips) {
		this.vips = vips;
		DomUtils.vips= vips;
		this._viewport = vips.getViewport();
		this.dom = vips.dom;
		DomUtils.dom = vips.dom;
		this._vipsBlocks = null;
		this._sizeTresholdHeight = 80;
		this._sizeTresholdWidth = 80;
		this._pageWidth = _viewport.getWidth();
		this._pageHeight = _viewport.getHeight();
	}

	/**
	 * Constructor, where we can define element's size treshold
	 * @param viewport	Rendered's page viewport
	 * @param sizeTresholdWidth Element's width treshold
	 * @param sizeTresholdHeight Element's height treshold
	 */
	public VipsSeleniumParser(VipsSelenium vips, int sizeTresholdWidth, int sizeTresholdHeight) {
		this.vips = vips;
		DomUtils.vips= vips;
		this._viewport = vips.getViewport();
		this.dom = vips.dom;
		DomUtils.dom = vips.dom;
		this._vipsBlocks = null;
		this._sizeTresholdHeight = sizeTresholdHeight;
		this._sizeTresholdWidth = sizeTresholdWidth;
	}

	/**
	 * Starts visual page segmentation on given page
	 */
	public void parse()
	{
		if (_viewport != null && this.dom!=null)
		{
//			this._vipsBlocks = new Node();
			_visualBlocksCount = 0;
			this._vipsBlocks = dom.getDocumentElement().getElementsByTagName("body").item(0);
//			constructVipsBlockTree(_viewport.getElementBoxByName("body", false), _vipsBlocks);
			divideVipsBlockTree(_vipsBlocks);
			getVisualBlocksCount(_vipsBlocks);
			//System.err.println(String.valueOf("We have " + _visualBlocksCount + " visual blocks."));
		}
		else
			System.err.print("Page's viewPort is not defined");
	}

	/**
	 * Counts number of visual blocks in visual structure
	 * @param vipsBlock Visual structure
	 */
	private void getVisualBlocksCount(Node vipsBlock)
	{
		if (DomUtils.isVisualBlock(vipsBlock))
			_visualBlocksCount++;

		ArrayList<Node>  vipsStructureChildren = DomUtils.getChildren(vipsBlock);
		for (int i = 0; i< vipsStructureChildren.size(); i++) {
			Node vipsBlockChild = vipsStructureChildren.get(i);
			if (!(DomUtils.isTextBox(vipsBlockChild)))
				getVisualBlocksCount(vipsBlockChild);
		}
	}

	

	private void findVisualBlocks(Node vipsBlock, List<Node> list)
	{
		if (DomUtils.isVisualBlock(vipsBlock))
			list.add(vipsBlock);
		
		ArrayList<Node>  vipsStructureChildren = DomUtils.getChildren(vipsBlock);
		for (int i = 0; i< vipsStructureChildren.size(); i++) {
			Node vipsStructureChild = vipsStructureChildren.get(i);
			findVisualBlocks(vipsStructureChild, list);
		}
	}
	
	public List<Node> getVisualBlocks()
	{
		List<Node> list = new ArrayList<Node>();
		findVisualBlocks(_vipsBlocks, list);

		return list;
	}

//	/**
//	 * Construct VIPS block tree from viewport.
//	 * <p>
//	 * Starts from &lt;body&gt; element.
//	 * @param element Box that represents element
//	 * @param node Visual structure tree node
//	 */
//	private void constructVipsBlockTree(Box element, Node node)
//	{
//		node.setBox(element);
//
//		if (! (element instanceof TextBox))
//		{
//			for (Box box: ((Node) element).getSubBoxList())
//			{
//				node.addChild(new Node());
//				constructVipsBlockTree(box, node.getChildren().get(node.getChildren().size()-1));
//			}
//		}
//	}

	/**
	 * Tries to divide DOM elements and finds visual blocks.
	 * @param vipsBlock Visual structure
	 */
	private void divideVipsBlockTree(Node vipsBlock)
	{
		_currentVipsBlock = vipsBlock;
		Node elementBox = vipsBlock;
		DomUtils.setIsVisualBlock(vipsBlock, false);
		DomUtils.setIsDividable(vipsBlock, true);
		//System.err.println(elementBox.getNode().getNodeName());
		//System.out.println(elementBox.getText());
		System.out.println(vipsBlock);
		
//		System.out.println(applyVipsRules(elementBox));
		// With VIPS rules it tries to determine if element is dividable
		if (applyVipsRules(elementBox) && DomUtils.isDividable(vipsBlock) && !DomUtils.isVisualBlock(vipsBlock))
		{
			// if element is dividable, let's divide it
			DomUtils.setAlreadyDivided(_currentVipsBlock);
			ArrayList<Node>  vipsStructureChildren = DomUtils.getChildren(vipsBlock);
			for (int i = 0; i< vipsStructureChildren.size(); i++) {
				Node vipsBlockChild = vipsStructureChildren.get(i);
				if (!(DomUtils.isTextBox(vipsBlockChild))) {
					divideVipsBlockTree(vipsBlockChild);
				}
			}
		}
		else
		{
			if (DomUtils.isDividable(vipsBlock))
			{
				//System.err.println("Element " + elementBox.getNode().getNodeName() + " is visual block");
				System.out.println("Visual Block");
				DomUtils.setIsVisualBlock(vipsBlock, true);
				DomUtils.setDoC(vipsBlock, 11);
			}

			if (!verifyValidity(elementBox))
			{
				DomUtils.setIsVisualBlock(vipsBlock, false);
				System.out.println("Not a visual block");
//				_currentVipsBlock.DomUtils.setIsVisualBlock(false);
			}
			/*
			if (vipsBlock.DomUtils.isVisualBlock())
				//System.err.println("Element " + elementBox.getNode().getNodeName() + " is visual block");
			else
				System.err.println("Element " + elementBox.getNode().getNodeName() + " is not visual block");*/
		}
	}


	
	
	private int getAllTextLength(Node node)
	{
		List<Node> childrenTextNodes = new ArrayList<Node>();

		findTextChildrenNodes(node, childrenTextNodes);

		int textLength = 0;

		for (Node child : childrenTextNodes)
		{
			String childText = child.getTextContent();

			if (!childText.equalsIgnoreCase("") && !childText.equalsIgnoreCase(" ") && !childText.equalsIgnoreCase("\n"))
				textLength += childText.length();
		}

		return textLength;
	}

	private void getAllChildren(Node node, List<Node> children)
	{
		children.add(node);

		if (DomUtils.isTextBox(node))
			return;

		ArrayList<Node>  childNodes = DomUtils.getChildren(node);
		for(int i=0; i<childNodes.size(); i++) {
		
			Node child  = childNodes.get(i);
		
			getAllChildren(child, children);
		}
	}

	private boolean verifyValidity(Node node)
	{
		if(isTextNode(node)) {
			if(node.getNodeValue().trim().isEmpty())
				return false;
			else
				return true;
		}
		Rectangle rect = DomUtils.getRectangle(node);
		
		if (rect.getX() < 0 || rect.getY() < 0)
			return false;
		
		
		if (rect.getX() + rect.getWidth() > _pageWidth)
		{
			return false;
			//System.out.println("X " + node.getAbsoluteContentX() + "\t" + (node.getAbsoluteContentX() + node.getContentWidth()) + "\t" + _pageWidth);
		}

		if (rect.getY() + rect.getHeight() > _pageHeight)
		{
			return false;
			//System.out.println("Y " + node.getAbsoluteContentY() + "\t" + (node.getAbsoluteContentY() + node.getContentHeight()) + "\t" + _pageHeight);
		}

		if (rect.getWidth() <= 0 || rect.getHeight() <= 0)
			return false;

		if (!DomUtils.isDisplayed(node))
			return false;

		
		if (getAllTextLength(node) == 0)
		{
			if (node.getNodeName().equalsIgnoreCase("img"))
				return true;
			if (node.getNodeName().equalsIgnoreCase("input"))
				return true;
			
			List<Node> children = new ArrayList<Node>();

			getAllChildren(node, children);

			for (Node child : children)
			{
				String childNodeName = child.getNodeName();

				if (!isVisible(child))
					continue;

				if (childNodeName.equalsIgnoreCase("img"))
					return true;
				if (childNodeName.equalsIgnoreCase("input"))
					return true;
			}

			return false;
		}

		return true;
	}

	

	

	private boolean isVisible(Node node) {
		if(isTextNode(node)) {
			if(node.getNodeValue().trim().isEmpty())
				return false;
			else
				return true;
		}
		
		Rectangle rect = DomUtils.getRectangle(node);
		
		if (rect.getX() < 0 || rect.getY() < 0)
			return false;
		
		
		if (rect.getX() + rect.getWidth() > _pageWidth)
		{
			return false;
			//System.out.println("X " + node.getAbsoluteContentX() + "\t" + (node.getAbsoluteContentX() + node.getContentWidth()) + "\t" + _pageWidth);
		}

		if (rect.getY() + rect.getHeight() > _pageHeight)
		{
			return false;
			//System.out.println("Y " + node.getAbsoluteContentY() + "\t" + (node.getAbsoluteContentY() + node.getContentHeight()) + "\t" + _pageHeight);
		}

		if (rect.getWidth() <= 0 || rect.getHeight() <= 0)
			return false;

		if (!DomUtils.isDisplayed(node))
			return false;
		
		return true;
	}

	/**
	 * Checks, if node is a valid node.
	 * <p>
	 * Node is valid, if it's visible in browser. This means, that the node's
	 * width and height are not zero.
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if node is valid, otherwise false.
	 */
	private boolean isValidNode(Node node)
	{
		Rectangle rect = DomUtils.getRectangle(node);
		if (rect.getHeight() > 0 && rect.getWidth() > 0)
			return true;

		return false;
	}

	/**
	 * Checks, if node is a text node.
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if node is a text node, otherwise false.
	 */
	private boolean isTextNode(Node node)
	{
		return (node.getNodeName().equalsIgnoreCase("#text")) ? true : false;
	}
	
	
	/**
	 * <a><abbr><acronym>
	 * <b><bdo><big><br><button>
	 * <cite><code><dfn><em><i><img><input>
	 * <kbd><label><map><object><output>
	 * <q><samp><script><select><small><span>
	 * <strong><sub><sup><textarea><time><tt><var>
	 * @param node
	 * @return
	 */
	public boolean isInlineNode(Node node) {
		String nodeName = node.getNodeName().toLowerCase();
		switch(nodeName) {
			case "#text":
			case "a":
			case "abbr":
			case "acronym":
			case "bdo":
			case "big":
			case "br":
			case "button":
			case "cite":
			case "code":
			case "dfn":
			case "em":
			case "i":
			case "img":
			case "input":
			case "kbd":
			case "label":
			case "map":
			case "object":
			case "output":
			case "q":
			case "samp":
			case "script":
			case "select":
			case "small":
			case "span":
			case "strong":
			case "sub":
			case "sup":
			case "textarea":
			case "time":
			case "tt":
			case "var":
			case "b":
				return true;
			default:
				return false;
		}
	}

	/**
	 * Checks, if node is a virtual text node.
	 * <p>
	 * Inline node with only text node children is a virtual text node.
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if node is virtual text node, otherwise false.
	 */
	private boolean isVirtualTextNode1(Node node)
	{
		if ( ! isInlineNode(node))
			return false;

		ArrayList<Node>  children = DomUtils.getChildren(node);
		for(int i=0; i<children.size(); i++) {
			Node childNode = children.get(i);
				if (!isTextNode((Node) childNode))
					return false;
		}

		return true;
	}

	/**
	 * Checks, if node is virtual text node.
	 * <p>
	 * Inline node with only text node and virtual text node children is a
	 * virtual text node.
	 *
	 * @param node
	 *            Input node
	 * 
	 * @return True, if node is virtual text node, otherwise false.
	 */
	private boolean isVirtualTextNode2(Node node)
	{
		if ( ! isInlineNode(node))
			return false;

		ArrayList<Node>  children = DomUtils.getChildren(node);
		for(int i=0; i<children.size(); i++) {
			Node childNode = children.get(i);
			if (!isTextNode( childNode) ||
					!isVirtualTextNode1( childNode))
				return false;
		}
		return true;
	}

	/**
	 * Checks, if node is virtual text node.
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if node is virtual text node, otherwise false.
	 */
	private boolean isVirtualTextNode(Node node)
	{
		if (isVirtualTextNode1(node))
			return true;
		if (isVirtualTextNode2(node))
			return true;

		return false;
	}

	int _cnt = 0;

	private void checkValidChildrenNodes(Node node)
	{
		if (DomUtils.isTextBox(node))
		{
			if (!node.getTextContent().trim().isEmpty())
			{
				_cnt++;
			}
			return;
		}
		else
		{
			if (isValidNode((Node) node))
				_cnt++;
		}

		ArrayList<Node>  children = DomUtils.getChildren(node);
		for(int i=0; i<children.size(); i++) {
			Node childNode = children.get(i);
			checkValidChildrenNodes(childNode);
		}
	}

	/*
	 * Checks if node has valid children nodes
	 */
	private boolean hasValidChildrenNodes(Node node)
	{
		if (node.getNodeName().equalsIgnoreCase("img") || node.getNodeName().equalsIgnoreCase("input") )
		{
			Rectangle rect = DomUtils.getRectangle(node);
			if (rect.getWidth() > 0 && rect.getHeight() > 0)
			{
				DomUtils.setIsVisualBlock(_currentVipsBlock, true);
				
				DomUtils.setDoC(_currentVipsBlock, 8);
				return true;
			}
			else
				return false;
		}

		if (DomUtils.getChildren(node).size() == 0)
			return false;

		_cnt = 0;

		ArrayList<Node>  children = DomUtils.getChildren(node);
		for(int i=0; i<children.size(); i++) {
			Node childNode = children.get(i);
			checkValidChildrenNodes(childNode);
		}

		return (_cnt > 0) ? true : false;
	}

	/*
	 * Returns the number of node's valid children
	 */
	private int numberOfValidChildNodes(Node node)
	{
		_cnt = 0;

		if (DomUtils.getChildren(node).size() == 0)
			return _cnt;

		ArrayList<Node>  children = DomUtils.getChildren(node);
		for(int i=0; i<children.size(); i++) {
			Node childNode = children.get(i);
			checkValidChildrenNodes(childNode);
		}

		return _cnt;
	}

	/**
	 * On different DOM nodes it applies different sets of VIPS rules.
	 * @param node DOM node
	 * @return Returns true if element is dividable, otherwise false.
	 */
	private boolean applyVipsRules(Node node)
	{
		boolean retVal = false;

		//System.err.println("Applying VIPS rules on " + node.getNode().getNodeName() + " node");

		if (isInlineNode(node))
		{
			retVal = applyInlineTextNodeVipsRules(node);
		}
		else if (node.getNodeName().equalsIgnoreCase("table"))
		{
			retVal = applyTableNodeVipsRules(node);
		}
		else if (node.getNodeName().equalsIgnoreCase("tr"))
		{
			retVal = applyTrNodeVipsRules(node);
		}
		else if (node.getNodeName().equalsIgnoreCase("td"))
		{
			retVal = applyTdNodeVipsRules(node);
		}
		else if (node.getNodeName().equalsIgnoreCase("p"))
		{
			retVal = applyPNodeVipsRules(node);
		}
		else
		{
			retVal = applyOtherNodeVipsRules(node);
		}

		return retVal;
	}

	/**
	 * Applies VIPS rules on block nodes other than &lt;P&gt; &lt;TD&gt;
	 * &lt;TR&gt; &lt;TABLE&gt;.
	 * @param node Node
	 * @return Returns true if one of rules success and node is dividable.
	 */
	private boolean applyOtherNodeVipsRules(Node node)
	{
		// 1 2 3 4 6 8 9 11

				if (ruleOne(node)) {
					System.out.println("Rule1 Applied");
					return true;
					}

				if (ruleTwo(node)) {
					
					System.out.println("Rule 2 applied");
					return true;}

				if (ruleThree(node)) {			
					System.out.println("Rule 3 applied");
					return true;
					}

				if (ruleFour(node)) {
					System.out.println("Rule 4 applied");

					return true;
					}

				if (ruleSix(node)) {
					System.out.println("Rule 6 applied");

					return true;}

				if (ruleEight(node)) {
					System.out.println("Rule 8 applied");

					return true;}

				if (ruleNine(node)) {
					System.out.println("Rule 9 applied");

					return true;}

				if (ruleEleven(node)) {
					System.out.println("Rule 11 applied");

					return true;}

				return false;
	}

	/**
	 * Applies VIPS rules on &lt;P&gt; node.
	 * @param node Node
	 * @return Returns true if one of rules success and node is dividable.
	 */
	private boolean applyPNodeVipsRules(Node node)
	{
		// 1 2 3 4 5 6 8 9 11

		if (ruleOne(node))
			return true;

		if (ruleTwo(node))
			return true;

		if (ruleThree(node))
			return true;

		if (ruleFour(node))
			return true;

		if (ruleFive(node))
			return true;

		if (ruleSix(node))
			return true;

		if (ruleSeven(node))
			return true;

		if (ruleEight(node))
			return true;

		if (ruleNine(node))
			return true;

		if (ruleTen(node))
			return true;

		if (ruleEleven(node))
			return true;

		if (ruleTwelve(node))
			return true;

		return false;
	}

	/**
	 * Applies VIPS rules on &lt;TD&gt; node.
	 * @param node Node
	 * @return Returns true if one of rules success and node is dividable.
	 */
	private boolean applyTdNodeVipsRules(Node node)
	{
		// 1 2 3 4 8 9 10 12

		if (ruleOne(node))
			return true;

		if (ruleTwo(node))
			return true;

		if (ruleThree(node))
			return true;

		if (ruleFour(node))
			return true;

		if (ruleEight(node))
			return true;

		if (ruleNine(node))
			return true;

		if (ruleTen(node))
			return true;

		if (ruleTwelve(node))
			return true;

		return false;
	}

	/**
	 * Applies VIPS rules on &TR;&gt; node.
	 * @param node Node
	 * @return Returns true if one of rules success and node is dividable.
	 */
	private boolean applyTrNodeVipsRules(Node node)
	{
		// 1 2 3 7 9 12

		if (ruleOne(node))
			return true;

		if (ruleTwo(node))
			return true;

		if (ruleThree(node))
			return true;

		if (ruleSeven(node))
			return true;

		if (ruleNine(node))
			return true;

		if (ruleTwelve(node))
			return true;

		return false;
	}

	/**
	 * Applies VIPS rules on &lt;TABLE&gt; node.
	 * @param node Node
	 * @return Returns true if one of rules success and node is dividable.
	 */
	private boolean applyTableNodeVipsRules(Node node)
	{
		// 1 2 3 7 9 12

		if (ruleOne(node))
			return true;

		if (ruleTwo(node))
			return true;

		if (ruleThree(node))
			return true;

		if (ruleSeven(node))
			return true;

		if (ruleNine(node))
			return true;

		if (ruleTwelve(node))
			return true;

		return false;
	}

	/**
	 * Applies VIPS rules on inline nodes.
	 * @param node Node
	 * @return Returns true if one of rules success and node is dividable.
	 */
	private boolean applyInlineTextNodeVipsRules(Node node)
	{
		// 1 2 3 4 5 6 8 9 11


		if (ruleOne(node)) {
			System.out.println("Rule1 Applied");
			return true;
			}

		if (ruleTwo(node)) {
			
			System.out.println("Rule 2 applied");
			return true;}

		if (ruleThree(node)) {			
			System.out.println("Rule 3 applied");
			return true;
			}

		if (ruleFour(node)) {
			System.out.println("Rule 4 applied");

			return true;
			}
		
		if (ruleFive(node)) {
			System.out.println("Rule 5 applied");

			return true;
			}
		if (ruleSix(node)) {
			System.out.println("Rule 6 applied");

			return true;}

		if (ruleEight(node)) {
			System.out.println("Rule 8 applied");

			return true;}

		if (ruleNine(node)) {
			System.out.println("Rule 9 applied");

			return true;}

		if (ruleEleven(node)) {
			System.out.println("Rule 11 applied");

			return true;
			}

		return false;
	}

	/**
	 * VIPS Rule One
	 * <p>
	 * If the DOM node is not a text node and it has no valid children, then
	 * this node cannot be divided and will be cut.
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleOne(Node node)
	{
		//System.err.println("Applying rule One on " + node.getNode().getNodeName() + " node");

		if (!isTextNode(node))
		{
			if (!hasValidChildrenNodes(node))
			{
				DomUtils.setIsDividable(_currentVipsBlock, false);
				return true;
			}
		}

		return false;
	}


	/**
	 * VIPS Rule Two
	 * <p>
	 * If the DOM node has only one valid child and the child is not a text
	 * node, then divide this node
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleTwo(Node node)
	{
		//System.err.println("Applying rule Two on " + node.getNode().getNodeName() + " node");

		if (numberOfValidChildNodes(node) == 1)
		{
			ArrayList<Node>  children = DomUtils.getChildren(node);
			for(int i =0; i<children.size(); i++) {
				Node child = children.get(0);
				if(!isValidNode(child))
					continue;
				/*
				 * What is the difference between these two
				if (node.getSubBox(0) instanceof TextBox)
					return false;
				if (!isTextNode((ElementBox) node.getSubBox(0)))
					return true;
				*/
				if (!isTextNode(child))
					return true;
			}
		}

		return false;
	}

	/**
	 * VIPS Rule Three
	 * <p>
	 * If the DOM node is the root node of the sub-DOM tree (corresponding to
	 * the block), and there is only one sub DOM tree corresponding to this
	 * block, divide this node.
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleThree(Node node)
	{
		//System.err.println("Applying rule Three on " + node.getNode().getNodeName() + " node");

		if (!node.getNodeName().equalsIgnoreCase("body")) {
			return false;
			}

		boolean result = true;
		int cnt = 0;

		ArrayList<Node>  children = DomUtils.getChildren(node);
		
//		return false;
		for (Node vipsBlock : children)
		{
			if (vipsBlock.getNodeName().equalsIgnoreCase(node.getNodeName()))
			{
				result = true;
				isOnlyOneDomSubTree(node, vipsBlock, result);

				if (result)
					cnt++;
			}
		}

		return (cnt == 1) ? true : false;
	}

	/**
	 * Checks if node's subtree is unique in DOM tree.
	 * @param pattern Node for comparing
	 * @param node Node from DOM tree
	 * @param result True if element is unique otherwise false
	 */
	private void isOnlyOneDomSubTree(Node pattern, Node node, boolean result)
	{
		if (!pattern.getNodeName().equalsIgnoreCase(node.getNodeName()))
			result = false;

		if (DomUtils.getChildren(pattern).size() != DomUtils.getChildren(node).size())
			result = false;

		if (!result)
			return;

		for (int i = 0; i < DomUtils.getChildren(pattern).size(); i++)
		{
			isOnlyOneDomSubTree(DomUtils.getChildren(pattern).get(i), DomUtils.getChildren(node).get(i), result);
		}
	}

	/**
	 * VIPS Rule Four
	 * <p>
	 * If all of the child nodes of the DOM node are text nodes or virtual text
	 * nodes, do not divide the node. <br>
	 * If the font size and font weight of all these child nodes are same, set
	 * the DoC of the extracted block to 10.
	 * Otherwise, set the DoC of this extracted block to 9.
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleFour(Node node)
	{
		//System.err.println("Applying rule Four on " + node.getNode().getNodeName() + " node");

		ArrayList<Node>  children = DomUtils.getChildren(node);
		
		if (children.size() == 0)
			return false;

		for (int i=0; i< children.size(); i++)
//		for (Box box : node.getSubBoxList())
		{
			Node child = children.get(i);
			
//			if (box instanceof TextBox)
//				continue;
//			
			if (!isTextNode(child) ||
					!isVirtualTextNode(child))
				return false;
		}

		DomUtils.setIsVisualBlock(_currentVipsBlock, true);
		DomUtils.setIsDividable(_currentVipsBlock, false);

		if (children.size() == 1)
		{
			/*
			if (node.getSubBox(0) instanceof TextBox)
			{
				_currentVipsBlock.DomUtils.setIsVisualBlock(false);
				_currentVipsBlock.DomUtils.setIsDividable(true);
				_currentVipsBlock.getChildren().get(0).DomUtils.setIsVisualBlock(true);
				_currentVipsBlock.getChildren().get(0).DomUtils.setIsDividable(false);
				_currentVipsBlock.getChildren().get(0).DomUtils.setDoC(11);
			}
			 */
			if (node.getFirstChild().getNodeName().equalsIgnoreCase("em"))
				DomUtils.setDoC(_currentVipsBlock, 11);
			else
				DomUtils.setDoC(_currentVipsBlock, 10);
			return true;
		}

		String fontWeight = "";
		int fontSize = 0;

		for (int i=0; i< children.size(); i++)
//			for (Box box : node.getSubBoxList())
		{
			
			Node childNode = children.get(i);
//			int childFontSize = childNode.getVisualContext().getFont().getSize();
//
//			if (childNode instanceof TextBox)
//			{
//				if (fontSize > 0)
//				{
//					if (fontSize != childFontSize)
//					{
//						_currentVipsBlock.DomUtils.setDoC(9);
//						break;
//					}
//					else
//						_currentVipsBlock.DomUtils.setDoC(10);
//				}
//				else
//					fontSize = childFontSize;
//				continue;
//			}

//			Node child = (Node) childNode;
//
//			if (child.getStylePropertyValue("font-weight") == null)
//				return false;
//
//			if (fontSize > 0)
//			{
//				if (child.getStylePropertyValue("font-weight").toString().equalsIgnoreCase(fontWeight) &&
//						childFontSize == fontSize)
//				{
//					_currentVipsBlock.DomUtils.setDoC(10);
//				}
//				else
//				{
//					_currentVipsBlock.DomUtils.setDoC(9);
//					break;
//				}
//			}
//			else
//			{
//				fontWeight = child.getStylePropertyValue("font-weight").toString();
//				fontSize = childFontSize;
//			}
		}

		return true;
	}

	/**
	 * VIPS Rule Five
	 * <p>
	 * If one of the child nodes of the DOM node is line-break node, then
	 * divide this DOM node.
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleFive(Node node)
	{
		//System.err.println("Applying rule Five on " + node.getNode().getNodeName() + " node");

		if (DomUtils.getChildren(node).size() == 0)
			return false;
		
		ArrayList<Node>  children = DomUtils.getChildren(node);
		for (int i=0; i< children.size(); i++){
			Node childNode = children.get(i);
			if (!isInlineNode(childNode))
				return true;
		}
		
		return false;
	}

	/**
	 * VIPS Rule Six
	 * <p>
	 * If one of the child nodes of the DOM node has HTML tag &lt;hr&gt;, then
	 * divide this DOM node
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleSix(Node node)
	{
		//System.err.println("Applying rule Six on " + node.getNode().getNodeName() + " node");
		if (DomUtils.getChildren(node).size() == 0)
			return false;

		List<Node> children = new ArrayList<Node>();
		getAllChildren(node, children);

		
		for (int i=0; i< children.size(); i++){
			Node childNode = children.get(i);
			if (childNode.getNodeName().equalsIgnoreCase("hr"))
				return true;
		}

		return false;
	}

	/**
	 * VIPS Rule Seven
	 * <p>
	 * If the background color of this node is different from one of its
	 * children’s, divide this node and at the same time, the child node with
	 * different background color will not be divided in this round.
	 * Set the DoC value (6-8) for the child node based on the &lt;html&gt;
	 * tag of the child node and the size of the child node.
	 * 
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleSeven(Node node)
	{
		
		//System.err.println("Applying rule Seven on " + node.getNode().getNodeName() + " node");
		if (DomUtils.getChildren(node).size() == 0)
			return false;
		
		if (isTextNode(node))
			return false;

		//String nodeBgColor = node.getStylePropertyValue("background-color");
		String nodeBgColor = DomUtils.getCssProperty(_currentVipsBlock, "background-color");
//		_currentVipsBlock.getBgColor();

		ArrayList<Node>  children = DomUtils.getChildren(node);
		
		for (int i=0; i<children.size(); i++)
		{
			Node childNode = children.get(i);
			String bgColor = DomUtils.getCssProperty(childNode, "background-color");
			if (!(bgColor.equalsIgnoreCase(nodeBgColor)))
			{
				DomUtils.setIsDividable(childNode, false);
				DomUtils.setIsVisualBlock(childNode, true);
				// TODO DoC values
				DomUtils.setDoC(childNode, 7);
				return true;
			}
		}

		return false;
	}


	private void findTextChildrenNodes(Node node, List<Node> results)
	{
		if (DomUtils.isTextBox(node))
		{
			results.add(node);
			return;
		}
		
		ArrayList<Node>  childNodes  = DomUtils.getChildren(node);
		for (int i=0;i<childNodes.size();i++) {
			Node childNode = childNodes.get(i);
			findTextChildrenNodes(childNode, results);
		}
	}

	/**
	 * VIPS Rule Eight
	 * <p>
	 * If the node has at least one text node child or at least one virtual
	 * text node child, and the node's relative size is smaller than
	 * a threshold, then the node cannot be divided.
	 * Set the DoC value (from 5-8) based on the html tag of the node.
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleEight(Node node)
	{
		//System.err.println("Applying rule Eight on " + node.getNode().getNodeName() + " node");
		if (DomUtils.getChildren(node).size() == 0)
			return false;
		

		List<Node> children = new ArrayList<Node>();

		findTextChildrenNodes(node, children);

		int cnt = children.size();

		if (cnt == 0)
			return false;

		Rectangle rect = DomUtils.getRectangle(node);
		
		if (rect.getWidth() == 0 || rect.getHeight() == 0)
		{
			children.clear();

			getAllChildren(node, children);

			for (Node child : children)
			{
				Rectangle childRect = DomUtils.getRectangle(child);
				if (childRect.getWidth() != 0 && childRect.getHeight() != 0)
					return true;
			}
		}

		if (rect.getWidth() * rect.getHeight() > _sizeTresholdHeight * _sizeTresholdWidth)
			return false;

		if (node.getNodeName().equalsIgnoreCase("ul"))
		{
			return true;
		}

		DomUtils.setIsVisualBlock(_currentVipsBlock, true);
		DomUtils.setIsDividable(_currentVipsBlock, false);

		if (node.getNodeName().equalsIgnoreCase("Xdiv"))
			DomUtils.setDoC(_currentVipsBlock, 7);
		else if (node.getNodeName().equalsIgnoreCase("code"))
			DomUtils.setDoC(_currentVipsBlock, 7);
		else if (node.getNodeName().equalsIgnoreCase("div"))
			DomUtils.setDoC(_currentVipsBlock, 5);
		else
			DomUtils.setDoC(_currentVipsBlock, 8);
		return true;
	}

	/**
	 * VIPS Rule Nine
	 * <p>
	 * If the child of the node with maximum size are small than
	 * a threshold (relative size), do not divide this node. <br>
	 * Set the DoC based on the html tag and size of this node.
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleNine(Node node)
	{
		//System.err.println("Applying rule Nine on " + node.getNode().getNodeName() + " node");
		if (DomUtils.getChildren(node).size() == 0)
			return false;
		
		int maxSize = 0;

		ArrayList<Node>  childNodes  = DomUtils.getChildren(node);
		for (int i=0;i<childNodes.size();i++) {
			Node childNode = childNodes.get(i);
			if(childNode.getNodeName().equalsIgnoreCase("#text")) {
				// Ignore Text nodes
				continue;
			}
			Rectangle childRect= DomUtils.getRectangle(childNode);
			int childSize = childRect.getWidth() * childRect.getHeight();

			if (maxSize < childSize)
			{
				maxSize = childSize;
			}
		}

		if (maxSize > _sizeTresholdWidth * _sizeTresholdHeight)
			return true;

		System.out.println("crossed Size Threshold");
		//TODO set DOC
		DomUtils.setIsVisualBlock(_currentVipsBlock, true);
		DomUtils.setIsDividable(_currentVipsBlock, false);

		if (node.getNodeName().equalsIgnoreCase("Xdiv"))
			DomUtils.setDoC(_currentVipsBlock, 7);
		if (node.getNodeName().equalsIgnoreCase("a"))
			DomUtils.setDoC(_currentVipsBlock, 11);
		else
			DomUtils.setDoC(_currentVipsBlock, 8);

		return true;
	}

	/**
	 * VIPS Rule Ten
	 * <p>
	 * If previous sibling node has not been divided, do not divide this node
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleTen(Node node)
	{
		//System.err.println("Applying rule Ten on " + node.getNode().getNodeName() + " node");

		//Node previousSiblingVipsBlock = null;
		//findPreviousSiblingNodeVipsBlock(node.getNode().getPreviousSibling(), _vipsBlocks, previousSiblingVipsBlock);

		_tempVipsBlock = null;
		findPreviousSiblingNodeVipsBlock(node.getPreviousSibling(), _vipsBlocks);

		if (_tempVipsBlock == null)
			return false;

		if (DomUtils.isAlreadyDivided(_tempVipsBlock))
			return true;

		return false;
	}


	/**
	 * VIPS Rule Eleven
	 * <p>
	 * Divide this node.
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleEleven(Node node)
	{
		//System.err.println("Applying rule Eleven on " + node.getNode().getNodeName() + " node");

		return (isInlineNode(node)) ? false : true;
	}

	/**
	 * VIPS Rule Twelve
	 * <p>
	 * Do not divide this node <br>
	 * Set the DoC value based on the html tag and size of this node.
	 * @param node
	 *            Input node
	 * 
	 * @return True, if rule is applied, otherwise false.
	 */
	private boolean ruleTwelve(Node node)
	{
		//System.err.println("Applying rule Twelve on " + node.getNode().getNodeName() + " node");

		DomUtils.setIsDividable(_currentVipsBlock, false);
		DomUtils.setIsVisualBlock(_currentVipsBlock, true);

		if (node.getNodeName().equalsIgnoreCase("Xdiv"))
			DomUtils.setDoC(_currentVipsBlock, 7);
		else if (node.getNodeName().equalsIgnoreCase("li"))
			DomUtils.setDoC(_currentVipsBlock, 8);
		else if (node.getNodeName().equalsIgnoreCase("span"))
			DomUtils.setDoC(_currentVipsBlock, 8);
		else if (node.getNodeName().equalsIgnoreCase("sup"))
			DomUtils.setDoC(_currentVipsBlock, 8);
		else if (node.getNodeName().equalsIgnoreCase("img"))
			DomUtils.setDoC(_currentVipsBlock, 8);
		else
			DomUtils.setDoC(_currentVipsBlock, 333);
		//TODO DoC Part
		return true;
	}

	/**
	 * @return the _sizeTresholdWidth
	 */
	public int getSizeTresholdWidth()
	{
		return _sizeTresholdWidth;
	}

	/**
	 * @param sizeTresholdWidth the _sizeTresholdWidth to set
	 */
	public void setSizeTresholdWidth(int sizeTresholdWidth)
	{
		this._sizeTresholdWidth = sizeTresholdWidth;
	}

	/**
	 * @return the _sizeTresholdHeight
	 */
	public int getSizeTresholdHeight()
	{
		return _sizeTresholdHeight;
	}

	/**
	 * @param sizeTresholdHeight the _sizeTresholdHeight to set
	 */
	public void setSizeTresholdHeight(int sizeTresholdHeight)
	{
		this._sizeTresholdHeight = sizeTresholdHeight;
	}

	public Node getVipsBlocks()
	{
		return _vipsBlocks;
	}

	/**
	 * Finds previous sibling node's VIPS block.
	 * @param node Node
	 * @param vipsBlock Actual VIPS block
	 * @param foundBlock VIPS block for given node
	 */
	private void findPreviousSiblingNodeVipsBlock(Node node, Node vipsBlock)
	{
		Rectangle rect = DomUtils.getRectangle(vipsBlock);
		Rectangle nodeRect = DomUtils.getRectangle(node);
		
		if(rect.x == nodeRect.x && rect.y == nodeRect.y && rect.width == nodeRect.width && rect.height == nodeRect.height) {
			_tempVipsBlock = vipsBlock;
			return;
		}	
//		if (vipsBlock.getBox().getNode().equalsIgnoreCase(node))
//		{
//			_tempVipsBlock = vipsBlock;
//			return;
//		}
//		else
//			for (Node vipsBlockChild : vipsBlock.getChildren())
//				findPreviousSiblingNodeVipsBlock(node, vipsBlockChild);
	}
}
