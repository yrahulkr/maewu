package com.saltlab.webmutator.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TextUtils {
	
	/**
	 * Empty elements / void tags
	 * <area>
		<base>
		<br>
		<col>
		<embed>
		<hr>
		<img>
		<input>
		<keygen>(HTML 5.2 Draft removed)
		<link>
		<meta>
		<param>
		<source>
		<track>
		<wbr> 
	 * @return
	 */
	public static List<String> excludeTags(){
		List<String> returnList = new ArrayList<>();
		returnList.add("br");
		returnList.add("img");
		returnList.add("wbr");
		returnList.add("hr");
		returnList.add("input");
		returnList.add("track");
		returnList.add("source");
		returnList.add("param");
		returnList.add("meta");
		returnList.add("link");
		returnList.add("keygen");
		returnList.add("embed");
		returnList.add("col");
		returnList.add("base");
		returnList.add("area");
		return returnList;
	}
	
	private static String formatTextToken(String textToken) {
		if (textToken == null)
			return null;
		String returnToken = textToken.trim().replace('\n',' ').replace('\t',' ').replace("\"", "'").replace('{', '<').replace('}','>' ).replace('[', '<').replace(']', '>');
		if(returnToken.toLowerCase().contains("body")) {
			System.out.println("found it");
			System.out.println(returnToken);
		}
		return returnToken;
	}
	
	public static String getRandomTextToken(Set<String> textTokens) {		
		if(textTokens.size()<1) {
			return null;
		}
		Random rand = new Random();
		rand.ints();
		int nextInt = Math.abs(rand.nextInt()%textTokens.size());
		List<String> list = new ArrayList<>();
		list.addAll(textTokens);
		String returnToken =  list.get(nextInt);
		formatTextToken(returnToken);
		return returnToken;
	}
}
