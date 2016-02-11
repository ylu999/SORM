package com.gvace.sorm.core;

/**
 * The factory for Query objects
 * @author Yushan Lu gvace.blogspot.com Feb 10, 2016
 *
 */
public class QueryFactory {//Prototype clone
	private static Query prototype; //prototype object
	static {
		try {
			Class clazz = Class.forName(DBManager.getConfiguration().getQueryClass()); //load query class
			prototype = (Query)clazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	private QueryFactory(){//private constructor
		
	}
	public static Query createQuery(){
		try {
			return (Query)prototype.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
