package com.saltlab.webmutator.operators.dom;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.w3c.dom.Node;

import com.saltlab.webmutator.MutationRecord;
import com.saltlab.webmutator.utils.VipsUtils;

public class EventHandlerDelete extends DomOperator{
	@Override
	public boolean isApplicable(Node node) {
		return VipsUtils.hasEventListener(node);
	}
	@Override
	public Node applyOperator(Node toMutate, MutationRecord record) {
		if(record.getData() == null) {
			record.setData("click");
		}
		return toMutate;
	}

	@Override
	public WebElement applyOperator(WebDriver driver, WebElement toMutate, MutationRecord record) {
		if(driver instanceof ChromeDriver) {
			ChromeDriver cDriver = (ChromeDriver)driver;
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("expression", "a=$x('" +record.getOriginalXpath()+"')[0]; "
					+ "if(a.attributes.getNamedItem('on"+ record.getData() +"')!==null){"
							+ "a.attributes.removeNamedItem('on"+ record.getData() + "');}"
									+ "  a.removeListenerType('"+record.getData()+"');");
			parameters.put("includeCommandLineAPI", Boolean.TRUE);
			parameters.put("returnByValue", Boolean.TRUE);
			Map<String, Object> attributeString = cDriver.executeCdpCommand("Runtime.evaluate", parameters);
			System.out.println(attributeString);
			return toMutate;
		}
		
		else {
			try {
				String handler = "on" + record.getData();
				Object onclick = ((JavascriptExecutor)driver).executeScript("return arguments[0]." + handler + "==null", toMutate);
				System.out.println("On click before " + onclick);
				((JavascriptExecutor)driver).executeScript("arguments[0]." + handler + "=null;", toMutate);
				onclick = ((JavascriptExecutor)driver).executeScript("return arguments[0]." + handler + "onclick==null", toMutate);
				System.out.println("on click after " +onclick);
				return toMutate;
			}catch(Exception ex) {
				return null;
			}
		}
	}

}
