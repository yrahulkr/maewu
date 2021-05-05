package com.saltlab.webmutator.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.w3c.dom.Node;

public class StringMutator {

	public static String getMutant(String attribute) {
		// TODO Auto-generated method stub
		return attribute.concat("_mut");
	}

	public static String getRandomString(Set<String> availableStrings) {
		if(availableStrings.size()<1) {
			return null;
		}
		Random rand = new Random();
		rand.ints();
		int nextInt = Math.abs(rand.nextInt()%availableStrings.size());
		List<String> list = new ArrayList<>();
		list.addAll(availableStrings);
		return list.get(nextInt);
	}
	
	public static String getInsValue(Node toMutate, String attrToInsert) {
		switch(attrToInsert.toLowerCase()) {
		case "id":
			return "mutID";
		case "name":
			return "mutName";
		case "class":
			return "mutClass";
		default:
			return "mutant";
		}
	}
	
}
