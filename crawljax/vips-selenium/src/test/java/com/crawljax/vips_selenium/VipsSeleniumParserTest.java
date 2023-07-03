package com.crawljax.vips_selenium;

import static org.junit.Assert.*;

import org.junit.Test;

public class VipsSeleniumParserTest {

	@Test
	public void test() {
		VipsSelenium vips = new VipsSelenium("http://localhost:9966/petclinic");
		VipsSeleniumParser parser = new VipsSeleniumParser(vips);
		fail("Not yet implemented");
	}


}
