package com.saltlab.murun;

import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.joox.selector.CSS2XPath;
import org.junit.Test;
import org.w3c.dom.NodeList;

import com.crawljax.util.XPathHelper;

public class TestCSS2XPath {
	@Test
	public void testClass() throws XPathExpressionException, IOException {
		String html =
				"<body>" + "<DIV><BUTTON class='btn'></BUTTON></DIV>" + "</body>";
		String cssSelector = "button.btn";
//		String xpathAllP = "//BUTTON[@class='btn']";
		String xpath = CSS2XPath.css2xpath(cssSelector);
//		String xpathOnlyExcludedP = "//DIV[@id='exclude']//P";
		NodeList nodes = XPathHelper.evaluateXpathExpression(html, xpath);
		System.out.println(nodes.getLength());
	}
}
