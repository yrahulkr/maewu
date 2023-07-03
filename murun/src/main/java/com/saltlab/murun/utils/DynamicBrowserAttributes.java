package com.saltlab.murun.utils;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class DynamicBrowserAttributes {
	String bgcolor;
	int fontsize;
	int fontweight;
	Rectangle rectangle;
	boolean isDisplayed;
	List<String> eventHandlers;
	
	public List<String> getEventHandlers() {
		return eventHandlers;
	}
	public void setEventHandlers(List<String> eventHandlers) {
		this.eventHandlers = eventHandlers;
	}
	public void addEventHandler(String eventHandler) {
		if(this.eventHandlers == null) {
			this.eventHandlers = new ArrayList<>();
		}
		this.eventHandlers.add(eventHandler);
	}
	
	public boolean isDisplayed() {
		return isDisplayed;
	}
	public void setDisplayed(boolean isDisplayed) {
		this.isDisplayed = isDisplayed;
	}
	public DynamicBrowserAttributes(Rectangle rect, int fontSize, int fontWeight, String bgColor, boolean isDisplayed2) {
		this.bgcolor = bgColor;
		this.rectangle= rect;
		this.fontsize= fontSize;
		this.fontweight  =fontWeight;
		this.isDisplayed = isDisplayed2;
		this.eventHandlers = new ArrayList<>();
	}
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	public int getFontsize() {
		return fontsize;
	}
	public void setFontsize(int fontsize) {
		this.fontsize = fontsize;
	}
	public int getFontweight() {
		return fontweight;
	}
	public void setFontweight(int fontweight) {
		this.fontweight = fontweight;
	}
	public Rectangle getRectangle() {
		return rectangle;
	}
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	
}
