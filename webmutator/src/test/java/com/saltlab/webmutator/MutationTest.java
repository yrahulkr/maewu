package com.saltlab.webmutator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.saltlab.webmutator.operators.dom.AttributeAdd;
import com.saltlab.webmutator.operators.dom.AttributeDelete;
import com.saltlab.webmutator.operators.dom.AttributeReplace;
import com.saltlab.webmutator.operators.dom.MutationData;
import com.saltlab.webmutator.operators.dom.StyleColor;
import com.saltlab.webmutator.operators.dom.StylePosition;
import com.saltlab.webmutator.operators.dom.StyleVisibility;
import com.saltlab.webmutator.operators.dom.SubtreeDelete;
import com.saltlab.webmutator.operators.dom.SubtreeInsert;
import com.saltlab.webmutator.operators.dom.SubtreeMove;
import com.saltlab.webmutator.operators.dom.TagMutator;
import com.saltlab.webmutator.operators.dom.TextNodeMutator;
import com.saltlab.webmutator.plugins.DefaultNodePicker;
import com.saltlab.webmutator.utils.DomUtils;
import com.saltlab.webmutator.utils.XPathHelper;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MutationTest {

	/*
	 * Tests the string representation of a document.
	 * @throws IOException
	 */
	@Test
	public void testTagMutator() throws Exception {
		String html = "<body><div id='testdiv'</div><div style=\"colour:#FF0000\">"
				+ "<h>Header</h></div></body>";

		String expectedDocString = "<HTML><HEAD><META http-equiv=\"Content-Type\"" +
				" content=\"text/html; charset=UTF-8\"></HEAD><BODY><SPAN id=\"testdiv\"> <a></a>" +
				"</SPAN><DIV style=\"colour:#FF0000\"><H>Header</H></DIV></BODY></HTML>";

		
		Document dom = DomUtils.asDocument(expectedDocString);
		
		TagMutator mutator = new TagMutator();
		Node applied  = mutator.applyOperator(dom.getElementById("testdiv"), null);
		if(applied != null) {
			System.out.println(DomUtils.getDocumentToString(dom).replace("\n", ""));
		}
		assertNotNull(dom);
		assertEquals(expectedDocString, DomUtils.getDocumentToString(dom).replace("\n", "")
				.replace("\r", ""));
	}
	
	@Test
	public void testTextMutator() throws Exception {
		String html = "<body><div id='testdiv'</div><div style=\"colour:#FF0000\">"
				+ "<h1>Header</h1></div></body>";

		String expectedDocString = "<HTML><HEAD><META http-equiv=\"Content-Type\"" +
				" content=\"text/html; charset=UTF-8\"></HEAD><BODY><SPAN id=\"testdiv\"> <a></a>" +
				"</SPAN><DIV style=\"colour:#FF0000\"><H>Header</H></DIV></BODY></HTML>";

		
		Document dom = DomUtils.asDocument(expectedDocString);
		
		TextNodeMutator mutator = new TextNodeMutator();
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
		Node node = webMutator.getNodePicker().pickNode(dom, mutator);
		
		Node applied  = mutator.applyOperator(node, null);
		if(applied != null) {
			System.out.println(DomUtils.getDocumentToString(dom).replace("\n", ""));
		}
		assertNotNull(dom);
		assertEquals(expectedDocString, DomUtils.getDocumentToString(dom).replace("\n", "")
				.replace("\r", ""));
	}
	
	@Test
	public void testOnlineTextMutator() throws Exception {

		String expectedDocString = "<HTML><head>should not mutate</head><BODY>"
				+ "<SPAN id='testdiv'> outside <a>Hello</a></SPAN>"
				+ "<DIV>Text <H1>Header</H1></DIV>"
				+ "</BODY></HTML>";

		Document dom = DomUtils.asDocument(expectedDocString);
		
		TextNodeMutator mutator = new TextNodeMutator();
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
		MutationRecord record = webMutator.applyDomOperator(dom, mutator);
		
	
		if(record.isSuccess()) {
			Node toCompareNode = XPathHelper.evaluateXpathExpression(dom, record.getMutatedXpath()).item(0);
			String toCompare = toCompareNode.getTextContent().trim();
			String newDom = DomUtils.getDocumentToString(dom).replace("\n", "");
			System.out.println(newDom);
			WebDriver d = null;
			try {
				WebDriverManager.chromedriver().setup();
				d = new ChromeDriver();
				((JavascriptExecutor)d).executeScript("document.getElementsByTagName('html')[0].innerHTML=arguments[0]", expectedDocString);
				Thread.sleep(2000);
				MutationRecordEx recordEx = webMutator.transferMutation(record, d);
				newDom = d.getPageSource();
				System.out.println(newDom);

				Thread.sleep(2000);
				System.out.println(recordEx.success);
				assertNotNull(recordEx);
				assertTrue(recordEx.success);
				String mutatedText = d.findElement(By.xpath(recordEx.originalXpath)).getText().trim();
				assertEquals(mutatedText, toCompare);
			}finally {
				if(d!=null) {
					d.close();
				}
			}
		}
		assertNotNull(dom);
	}
	
	@Test
	public void testOnlineTagMutator() throws Exception {

		String expectedDocString = "<HTML><HEAD></HEAD><BODY>"
				+ "<SPAN id='testdiv'> outside <a>Hello</a></SPAN>"
				+ "<DIV>Text <H1>Header</H1></DIV>"
				+ "</BODY></HTML>";

		Document dom = DomUtils.asDocument(expectedDocString);
		
		TagMutator mutator = new TagMutator();
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
		MutationRecord record = webMutator.applyDomOperator(dom, mutator);
		String toCompare = record.getMutatedXpath().split("/")[record.getMutatedXpath().split("/").length-1];
		toCompare = toCompare.substring(0, toCompare.indexOf("["));
		if(record.isSuccess()) {
			String newDom = DomUtils.getDocumentToString(dom).replace("\n", "");
			System.out.println(newDom);
			WebDriver d = null;
			try {
				WebDriverManager.chromedriver().setup();
				d = new ChromeDriver();
				((JavascriptExecutor)d).executeScript("document.getElementsByTagName('html')[0].innerHTML=arguments[0]", expectedDocString);
				Thread.sleep(2000);
				MutationRecordEx recordEx = webMutator.transferMutation(record, d);
				System.out.println(recordEx.success);
				assertNotNull(recordEx);
				assertTrue(recordEx.success);
				String mutatedTag = d.findElement(By.xpath(recordEx.mutatedXpath)).getTagName();
				newDom = d.getPageSource();
				System.out.println(newDom);
				System.out.println("Mutated Tag: " + mutatedTag);
				Thread.sleep(2000);
				assertEquals(toCompare, mutatedTag.toUpperCase());
			}finally {
				if(d!=null) {
					d.close();
				}
			}
		}
		assertNotNull(dom);
	}

	@Test
	public void testOnlineSubtreeMutator() throws Exception {

		String expectedDocString = "<HTML><HEAD></HEAD><BODY>"
				+ "<SPAN id='testdiv'> outside <a>Hello</a></SPAN>"
				+ "<DIV>Text <H1>Header</H1></DIV>"
				+ "</BODY></HTML>";

		Document dom = DomUtils.asDocument(expectedDocString);
		
		SubtreeDelete mutator = new SubtreeDelete();
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
		MutationRecord record = webMutator.applyDomOperator(dom, mutator);
		
		System.out.println(DomUtils.getDocumentToString(dom));
		
		String toCompare = record.getMutatedXpath().split("/")[record.getMutatedXpath().split("/").length-1];
		toCompare = toCompare.substring(0, toCompare.indexOf("["));
		if(record.isSuccess()) {
			
			WebDriver d = null;
			try {
				WebDriverManager.chromedriver().setup();
				d = new ChromeDriver();
				((JavascriptExecutor)d).executeScript("document.getElementsByTagName('html')[0].innerHTML=arguments[0]", expectedDocString);
				Thread.sleep(2000);

				MutationRecordEx recordEx = webMutator.transferMutation(record, d);
				System.out.println(recordEx.success);
				assertNotNull(recordEx);
				assertTrue(recordEx.success);
				String mutatedTag = d.findElement(By.xpath(recordEx.mutatedXpath)).getTagName();
				String newDom = d.getPageSource();
				System.out.println(newDom);
				System.out.println("Mutated Tag: " + mutatedTag);
				Thread.sleep(2000);
				assertEquals(toCompare, mutatedTag.toUpperCase());
			}finally {
				if(d!=null) {
					d.close();
				}
			}
		}
		assertNotNull(dom);
	}
	
	@Test
	public void testStylePositionMutator() throws Exception{

		String html = "<body><div id='testdiv' style=\"colour:#FF0000\">"
				+ "<h>Header</h></div></body>";

		/*String expectedDocString = "<HTML><HEAD><META http-equiv=\"Content-Type\"" +
				" content=\"text/html; charset=UTF-8\"></HEAD><BODY><DIV id=\"testdiv\" style=\"colour:#FF0000\"><H>Header</H></DIV></BODY></HTML>";

		*/
		Document dom = DomUtils.asDocument(html);
		
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());

		StylePosition mutator = new StylePosition();
		MutationRecord record =  webMutator.applyDomOperator(dom.getElementById("testdiv"), mutator, null);
//		Node applied  = mutator.applyOperator(dom.getElementById("testdiv"), new MutationRecord(false, null));
		
		assertTrue("Failed to apply mutation to DOM Node", record.isSuccess());
		

		
		
		WebDriver d = null;
		try {
			WebDriverManager.chromedriver().setup();
			d = new ChromeDriver();
			((JavascriptExecutor)d).executeScript("document.getElementsByTagName('html')[0].innerHTML=arguments[0]", html);
			Thread.sleep(2000);
			Point oldPoint = d.findElement(By.xpath(record.originalXpath)).getLocation();
			MutationRecordEx recordEx = webMutator.transferMutation(record, d);
			System.out.println(recordEx.success);
			assertNotNull(recordEx);
			assertTrue(recordEx.success);
			Point mutatedPoint = d.findElement(By.xpath(recordEx.mutatedXpath)).getLocation();
			String newDom = d.getPageSource();
			System.out.println(newDom);
			System.out.println( oldPoint + "Mutated : " + mutatedPoint);
			Thread.sleep(2000);
//			assertEquals(toCompare, mutatedTag.toUpperCase());
		}finally {
			if(d!=null) {
				d.close();
			}
		}
		
	}
	
	@Test
	public void testStyleVisibilityMutator() throws Exception{

		String html = "<body><div isDisplayed=true id='testdiv' style=\"color:#FF0000\">"
				+ "<h>Header</h></div></body>";

		/*String expectedDocString = "<HTML><HEAD><META http-equiv=\"Content-Type\"" +
				" content=\"text/html; charset=UTF-8\"></HEAD><BODY><DIV id=\"testdiv\" style=\"colour:#FF0000\"><H>Header</H></DIV></BODY></HTML>";

		*/
		Document dom = DomUtils.asDocument(html);
		
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());

		StyleVisibility mutator = new StyleVisibility();
		MutationRecord record =  webMutator.applyDomOperator(dom.getElementById("testdiv"), mutator, null);
//		Node applied  = mutator.applyOperator(dom.getElementById("testdiv"), new MutationRecord(false, null));
		
		assertTrue("Failed to apply mutation to DOM Node", record.isSuccess());
		

		
		
		WebDriver d = null;
		try {
			WebDriverManager.chromedriver().setup();
			d = new ChromeDriver();
			((JavascriptExecutor)d).executeScript("document.getElementsByTagName('html')[0].innerHTML=arguments[0]", html);
			Thread.sleep(2000);
			Point oldPoint = d.findElement(By.xpath(record.originalXpath)).getLocation();
			MutationRecordEx recordEx = webMutator.transferMutation(record, d);
			System.out.println(recordEx.success);
			assertNotNull(recordEx);
			assertTrue(recordEx.success);
			Point mutatedPoint = d.findElement(By.xpath(recordEx.mutatedXpath)).getLocation();
			String newDom = d.getPageSource();
			System.out.println(newDom);
			System.out.println( oldPoint + "Mutated : " + mutatedPoint);
			Thread.sleep(2000);
//			assertEquals(toCompare, mutatedTag.toUpperCase());
		}finally {
			if(d!=null) {
				d.close();
			}
		}
		
	}
	
	@Test
	public void testStyleColorMutator() throws Exception{

		String html = "<body><div id='testdiv' style=\"colour:#FF0000\">"
				+ "<h>Header</h></div></body>";

		/*String expectedDocString = "<HTML><HEAD><META http-equiv=\"Content-Type\"" +
				" content=\"text/html; charset=UTF-8\"></HEAD><BODY><DIV id=\"testdiv\" style=\"colour:#FF0000\"><H>Header</H></DIV></BODY></HTML>";

		*/
		Document dom = DomUtils.asDocument(html);
		
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());

		StyleColor mutator = new StyleColor();
		MutationRecord record =  webMutator.applyDomOperator(dom.getElementById("testdiv"), mutator, null);
//		Node applied  = mutator.applyOperator(dom.getElementById("testdiv"), new MutationRecord(false, null));
		
		assertTrue("Failed to apply mutation to DOM Node", record.isSuccess());
		

		
		
		WebDriver d = null;
		try {
			WebDriverManager.chromedriver().setup();
			d = new ChromeDriver();
			((JavascriptExecutor)d).executeScript("document.getElementsByTagName('html')[0].innerHTML=arguments[0]", html);
			Thread.sleep(2000);

			MutationRecordEx recordEx = webMutator.transferMutation(record, d);
			System.out.println(recordEx.success);
			assertNotNull(recordEx);
			assertTrue(recordEx.success);
			String mutatedTag = d.findElement(By.xpath(recordEx.mutatedXpath)).getCssValue("color");
			String newDom = d.getPageSource();
			System.out.println(newDom);
			System.out.println("Mutated Color: " + mutatedTag);
			Thread.sleep(2000);
//			assertEquals(toCompare, mutatedTag.toUpperCase());
		}finally {
			if(d!=null) {
				d.close();
			}
		}
		
	}

	@Test
	public void testOnlineAttrDeleteMutator() throws Exception {

		String expectedDocString = "<HTML><HEAD></HEAD><BODY>"
				+ "<SPAN id='testdiv'> outside <a>Hello</a></SPAN>"
				+ "<DIV>Text <H1>Header</H1></DIV>"
				+ "</BODY></HTML>";

		Document dom = DomUtils.asDocument(expectedDocString);
		
		AttributeDelete mutator = new AttributeDelete();
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
		MutationRecord record = webMutator.applyDomOperator(dom, mutator);
		
		System.out.println(DomUtils.getDocumentToString(dom));
		
		String toCompare = record.getMutatedXpath().split("/")[record.getMutatedXpath().split("/").length-1];
		toCompare = toCompare.substring(0, toCompare.indexOf("["));
		if(record.isSuccess()) {
			
			WebDriver d = null;
			try {
				WebDriverManager.chromedriver().setup();
				d = new ChromeDriver();
				((JavascriptExecutor)d).executeScript("document.getElementsByTagName('html')[0].innerHTML=arguments[0]", expectedDocString);
				Thread.sleep(2000);

				MutationRecordEx recordEx = webMutator.transferMutation(record, d);
				System.out.println(recordEx.success);
				assertNotNull(recordEx);
				assertTrue(recordEx.success);
				String mutatedTag = d.findElement(By.xpath(recordEx.mutatedXpath)).getTagName();
				String newDom = d.getPageSource();
				System.out.println(newDom);
				System.out.println("Mutated Tag: " + mutatedTag);
				Thread.sleep(2000);
				assertEquals(toCompare, mutatedTag.toUpperCase());
			}finally {
				if(d!=null) {
					d.close();
				}
			}
		}
		assertNotNull(dom);
	}
	
	@Test
	public void testAttrAddMutator() throws Exception {

		String expectedDocString = "<HTML><HEAD></HEAD><BODY>"
				+ "<SPAN id='testdiv'> outside <a>Hello</a></SPAN>"
				+ "<FORM action='sendData' method='POST' id='form'> <INPUT type='text' id='textInput'/><BUTTON type='submit' id='submit' value='button'> </BUTTON></FORM>"
				+ "</BODY></HTML>";

		Document dom = DomUtils.asDocument(expectedDocString);
		
		Set<String> tokens = new HashSet<String>();
		tokens.add("xyz");
		Map<String, Set<String>> tokenMap = new HashMap<>();
		tokenMap.put("id", tokens);
		AttributeAdd mutator = new AttributeAdd(tokenMap);
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
//		MutationRecord record = webMutator.applyDomOperator(dom, mutator);
		

		MutationRecord record =  webMutator.applyDomOperator(dom.getElementById("form"), mutator, null);

		
		System.out.println(DomUtils.getDocumentToString(dom));
		
		String toCompare = record.getMutatedXpath().split("/")[record.getMutatedXpath().split("/").length-1];
		toCompare = toCompare.substring(0, toCompare.indexOf("["));
		assertNotNull(dom);
	}
	
	@Test
	public void testAttrDeleteMutator() throws Exception {

		String expectedDocString = "<HTML><HEAD></HEAD><BODY>"
				+ "<SPAN id='testdiv'> outside <a>Hello</a></SPAN>"
				+ "<FORM action='sendData' method='POST' id='form'> <INPUT type='text' id='textInput'/><BUTTON type='submit' id='submit' value='button'> </BUTTON></FORM>"
				+ "</BODY></HTML>";

		Document dom = DomUtils.asDocument(expectedDocString);
		
		Set<String> tokens = new HashSet<String>();
		tokens.add("xyz");
		Map<String, Set<String>> tokenMap = new HashMap<>();
		tokenMap.put("id", tokens);
		AttributeDelete mutator = new AttributeDelete();
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
//		MutationRecord record = webMutator.applyDomOperator(dom, mutator);
		

		MutationRecord record =  webMutator.applyDomOperator(dom.getElementById("form"), mutator, null);

		System.out.println("record : " +  record);
		System.out.println("deleted attr : " + record.data);
		
		System.out.println(DomUtils.getDocumentToString(dom));
		
		String toCompare = record.getMutatedXpath().split("/")[record.getMutatedXpath().split("/").length-1];
		toCompare = toCompare.substring(0, toCompare.indexOf("["));
		assertNotNull(dom);
	}
	
	@Test
	public void testAttrReplaceMutator() throws Exception {

		String expectedDocString = "<HTML><HEAD></HEAD><BODY>"
				+ "<SPAN id='testdiv'> outside <a>Hello</a></SPAN>"
				+ "<FORM action='sendData' method='POST' id='form'> <INPUT type='text' id='textInput'/><BUTTON type='submit' id='submit' value='button'> </BUTTON></FORM>"
				+ "</BODY></HTML>";

		Document dom = DomUtils.asDocument(expectedDocString);
		
		Set<String> tokens = new HashSet<String>();
		tokens.add("xyz");
		Map<String, Set<String>> tokenMap = new HashMap<>();
		tokenMap.put("id", tokens);
		AttributeReplace mutator = new AttributeReplace(tokenMap);
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());
//		MutationRecord record = webMutator.applyDomOperator(dom, mutator);
		

		MutationRecord record =  webMutator.applyDomOperator(dom.getElementById("textInput"), mutator, null);

		System.out.println("record : " +  record);
		System.out.println("replaced attr : " + (MutationData)record.data);
		
		System.out.println(DomUtils.getDocumentToString(dom));
		
		String toCompare = record.getMutatedXpath().split("/")[record.getMutatedXpath().split("/").length-1];
		toCompare = toCompare.substring(0, toCompare.indexOf("["));
		assertNotNull(dom);
	}
	
	@Test
	public void testSubtreeInsertMutator() throws Exception{

		String html = "<body><div id='testdiv'>"
				+ "<div id='toclone'>Header</div></div></body>";

		/*String expectedDocString = "<HTML><HEAD><META http-equiv=\"Content-Type\"" +
				" content=\"text/html; charset=UTF-8\"></HEAD><BODY><DIV id=\"testdiv\" style=\"colour:#FF0000\"><H>Header</H></DIV></BODY></HTML>";

		*/
		Document dom = DomUtils.asDocument(html);
		
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());

		SubtreeInsert mutator = new SubtreeInsert();
		MutationRecord record =  webMutator.applyDomOperator(dom.getElementById("toclone"), mutator, null);
//		Node applied  = mutator.applyOperator(dom.getElementById("testdiv"), new MutationRecord(false, null));
		
		assertTrue("Failed to apply mutation to DOM Node", record.isSuccess());
		

		
		
		WebDriver d = null;
		try {
			WebDriverManager.chromedriver().setup();
			d = new ChromeDriver();
			((JavascriptExecutor)d).executeScript("document.getElementsByTagName('html')[0].innerHTML=arguments[0]", html);
			Thread.sleep(2000);

			MutationRecordEx recordEx = webMutator.transferMutation(record, d);
			System.out.println(recordEx.success);
			assertNotNull(recordEx);
			assertTrue(recordEx.success);
			String mutatedTag = d.findElement(By.xpath(recordEx.mutatedXpath)).getAttribute("mutated");
			String newDom = d.getPageSource();
			System.out.println(newDom);
			System.out.println("Mutated: " + mutatedTag);
			
			
			Thread.sleep(2000);
			
			MutationRecordEx recordEx2 = webMutator.transferMutation(record, d);
			mutatedTag = d.findElement(By.xpath(recordEx.mutatedXpath)).getAttribute("mutated");
			System.out.println("Mutated 2 :" + mutatedTag);
			newDom = d.getPageSource();
			System.out.println(newDom);
//			assertEquals(toCompare, mutatedTag.toUpperCase());
		}finally {
			if(d!=null) {
				d.close();
			}
		}
		
	}
	
	@Test
	public void testSubtreeMoveMutator() throws Exception{

		String html = "<body>"
				+ "<div id='target'> target </div>"
				+ "<div id='testdiv'><div>initial</div><div id='tomove'>Header</div></div>"
				+ "</body>";

		/*String expectedDocString = "<HTML><HEAD><META http-equiv=\"Content-Type\"" +
				" content=\"text/html; charset=UTF-8\"></HEAD><BODY><DIV id=\"testdiv\" style=\"colour:#FF0000\"><H>Header</H></DIV></BODY></HTML>";

		*/
		Document dom = DomUtils.asDocument(html);
		
		WebMutator webMutator = new WebMutator(MutationMode.DOM, new DefaultNodePicker());

		SubtreeMove mutator = new SubtreeMove();
		MutationRecord record =  webMutator.applyDomOperator(dom.getElementById("tomove"), mutator, null);
//		Node applied  = mutator.applyOperator(dom.getElementById("testdiv"), new MutationRecord(false, null));
		
		assertTrue("Failed to apply mutation to DOM Node", record.isSuccess());
		

		
		
		WebDriver d = null;
		try {
			WebDriverManager.chromedriver().setup();
			d = new ChromeDriver();
			((JavascriptExecutor)d).executeScript("document.getElementsByTagName('html')[0].innerHTML=arguments[0]", html);
			Thread.sleep(2000);

			MutationRecordEx recordEx = webMutator.transferMutation(record, d);
			System.out.println(recordEx.success);
			assertNotNull(recordEx);
			assertTrue(recordEx.success);
			String mutatedTag = d.findElement(By.xpath((String)recordEx.data)).getAttribute("mutated");
			String newDom = d.getPageSource();
			System.out.println(newDom);
			System.out.println("Mutated: " + mutatedTag);
			
			
			Thread.sleep(2000);
			
			MutationRecordEx recordEx2 = webMutator.transferMutation(record, d);
			mutatedTag = d.findElement(By.xpath((String)recordEx.data)).getAttribute("mutated");
			System.out.println("Mutated 2 :" + mutatedTag);
			newDom = d.getPageSource();
			System.out.println(newDom);
//			assertEquals(toCompare, mutatedTag.toUpperCase());
		}finally {
			if(d!=null) {
				d.close();
			}
		}
		
	}

	
}