package com.gvace.sorm.utils;

/**
 * Common String operations
 * @author yushan
 *
 */
public class StringUtils {
	/**
	 * First char to uppercase
	 * @return First char to uppercase String
	 */
	public static String firstCharUpperCase(String str){
		char first = Character.toUpperCase(str.charAt(0));
		return first+str.substring(1);
	}
}
