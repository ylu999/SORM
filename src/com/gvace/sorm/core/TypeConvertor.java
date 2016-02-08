package com.gvace.sorm.core;
/**
 * Convert between java datatype and database datatype
 * @author Yushan Lu gvace.blogspot.com
 *
 */
public interface TypeConvertor {
	/**
	 * Convert database type to java data type 
	 * @param columnType
	 * @return java data type
	 */
	public String db2Java(String columnType);
	/**
	 * Convert java data type to database type 
	 * @param columnType
	 * @return database data type
	 */
	public String java2db(String columnType);
}
