package com.crawljax.vips_selenium;


public class Test {
	public static void main(String args[]) {
		VipsSelenium vips = new VipsSelenium("http://amesbah-macpro.ece.ubc.ca:8888/claroline/claroline-1.11.10/");
//		VipsSeleniumParser parser = new VipsSeleniumParser(vips);
		vips.startSegmentation();
		vips.cleanup();
	}
}
