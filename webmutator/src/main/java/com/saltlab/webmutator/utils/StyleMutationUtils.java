package com.saltlab.webmutator.utils;

import java.awt.Point;
import java.util.Random;

public class StyleMutationUtils {
	
    public static String getRandomHexString(int numchars){
        Random r = new Random();
        
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Long.toHexString(r.nextLong()));
        }

        return sb.toString().substring(0, numchars);
    }

    public static void main(String args[]) {
    	String randString = getRandomHexString(6);
    	System.out.println(randString);
    }

	public static Point getRandomCoordinates() {
		 Random r = new Random();
		 Point point = new Point(Math.abs(r.nextInt())%800, Math.abs(r.nextInt())%600);
		 return point;
	}
	
	
}
