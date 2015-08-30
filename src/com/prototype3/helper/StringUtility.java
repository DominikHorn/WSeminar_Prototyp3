package com.prototype3.helper;

public class StringUtility {
	public static int getOccurenceCount(String string, String substring) {
		int occurences = 0;
		int occurenceIndex = -1;
		
		while ((occurenceIndex = string.indexOf(substring)) != -1) {
			occurences++;
			string = string.substring(occurenceIndex + substring.length());
		}
		
		return occurences;
	}
	
}
