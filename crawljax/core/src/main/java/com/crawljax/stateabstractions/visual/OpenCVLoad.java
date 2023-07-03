package com.crawljax.stateabstractions.visual;

public class OpenCVLoad {
	public static String location = 
//			"/home/rahul/git/opencv/build/lib/libopencv_java342.so";
	"/Users/rahulkrishna/VisCrawler/libs/opencv-3.4.2/staticlibs/lib/libopencv_java342.dylib";

	public static boolean loaded = false;
	static {
		System.load(location);
		System.out.println("OPENCV LOADED!!");
	}
	public static boolean load() {
		if(!loaded) {
			loaded = true;
		}
		
		return true;
	}
	
}
