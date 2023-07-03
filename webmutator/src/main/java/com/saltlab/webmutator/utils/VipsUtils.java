package com.saltlab.webmutator.utils;

import java.awt.Rectangle;

import org.openqa.selenium.WebDriver;
import org.w3c.dom.Node;

import com.google.gson.Gson;

public class VipsUtils {
	private static final String ISDISPLAYED = "isdisplayed";
	private static final String RECTANGLE = "rectangle";

	private static final String EVLIST = "evlist";

	private static final String EVLISTVAL = "evlistval";


	public static String getVipsAttributeValue(Node vipsBlock, String attr) {
		if(vipsBlock==null) {
			return null;
		}
		
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return getVipsAttributeValue(vipsBlock.getParentNode(), attr);
		}
		
		if(vipsBlock.getAttributes()!=null && vipsBlock.getAttributes().getNamedItem(attr)!=null) {
			return vipsBlock.getAttributes().getNamedItem(attr).getNodeValue();
		}else {
			return null;
		}
		
	}
	
	public static boolean hasEventListener(Node vipsBlock) {
		String evList = getVipsAttributeValue(vipsBlock, EVLIST);
		if(evList.equalsIgnoreCase("true")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static String getEventListenerVal(Node vipsBlock) {
		return getVipsAttributeValue(vipsBlock, EVLISTVAL);
	}

	
	
	public static String isDisplayed(Node vipsBlock) {
		if(vipsBlock.getNodeName().equalsIgnoreCase("#text"))
			return isDisplayed(vipsBlock.getParentNode());
		
		if (vipsBlock.getAttributes().getNamedItem(ISDISPLAYED) != null) {
			String str = vipsBlock.getAttributes().getNamedItem(ISDISPLAYED).getNodeValue();
			return str;
		}		
		else {
			Rectangle rectangle = getRectangle(vipsBlock);
			if(rectangle==null) {
				return null;
			}
			if(	rectangle.height>0 && rectangle.width>0) {
				return "true";
			} 
			else {
				return "false";
			}
		}
	}
	
	public static Rectangle getRectangle(Node vipsBlock) {
		if(vipsBlock==null)
			return null;
		if (vipsBlock.getNodeName().equalsIgnoreCase("#text")) {
			return getRectangle(vipsBlock.getParentNode());
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
}
