package com.saltlab.webmutator.operators.dom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.w3c.dom.Node;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.DomUtils;
import com.saltlab.webmutator.utils.EventListenerUtils;
import com.saltlab.webmutator.utils.VipsUtils;

public class EventHandlerAdd extends DomOperator {
	
	Set<String> availableEventListeners = null;
	
	public EventHandlerAdd(Set<String> eventListeners) {
		this.availableEventListeners = eventListeners;
	}

	public void setAvailableEventListeners(Set<String> availableEventListeners) {
		this.availableEventListeners = availableEventListeners;
	}
	
	// Dont apply this operator to nodes already with event listeners
	@Override
	public boolean isApplicable(Node node) {
		return !VipsUtils.hasEventListener(node) && DomUtils.isLeafNode(node);
	}
	
	// The document should already contain all event listeners in the page
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		
		if(record.getData() == null) {
			String randomListener = EventListenerUtils.getRandomEventListener(availableEventListeners);
			if(randomListener!=null) {
				record.setData(new MutationData("click", randomListener));
				return toMutate;
			}
			else {
				return null;
			}	
		}
		return toMutate;
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		MutationData keyValue = (MutationData) record.getData();
		
		if(driver instanceof ChromeDriver) {
			ChromeDriver cDriver = (ChromeDriver)driver;
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("expression", "a=$x('" +record.getOriginalXpath()+"')[0]; "
					+ "if(a.attributes.getNamedItem('on"+ keyValue.getKey() +"')!==null){"
							+ "a.attributes.removeNamedItem('on"+ keyValue.getKey() + "');}"
									+ "  a.removeListenerType('"+ keyValue.getKey()+"');");
			parameters.put("includeCommandLineAPI", Boolean.TRUE);
			parameters.put("returnByValue", Boolean.TRUE);
			Map<String, Object> attributeString = cDriver.executeCdpCommand("Runtime.evaluate", parameters);
			System.out.println(attributeString);
			
		}
		
		((JavascriptExecutor)driver).executeScript("arguments[0].addEventListener('" + keyValue.getKey() + "', " + keyValue.getValue()+ ")", toMutate);		
		return toMutate;
		
	}

}
