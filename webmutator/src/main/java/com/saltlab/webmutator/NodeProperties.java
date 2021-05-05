package com.saltlab.webmutator;

import java.awt.Rectangle;

import com.saltlab.webmutator.utils.VipsUtils;

public class NodeProperties {

	String display;
	Rectangle position;
	String eventListener;
	
	public NodeProperties(String display, Rectangle position, String eventListener) {
		this.display = display;
		this.position = position;
		this.eventListener = eventListener;
	}
	
	public boolean isDisplayed() {
		return display != null && display.equalsIgnoreCase("true");
	}

	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public Rectangle getPosition() {
		return position;
	}
	public void setPosition(Rectangle position) {
		this.position = position;
	}
	public String getEventListener() {
		return eventListener;
	}
	public void setEventListener(String eventListener) {
		this.eventListener = eventListener;
	}

}
