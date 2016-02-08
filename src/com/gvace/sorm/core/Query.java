package com.gvace.sorm.core;

import java.util.List;

/*
 * For selection, core public function
 * @author Yushan Lu gvace.blogspot.com
 */
@SuppressWarnings("all")
public interface Query {
	/**
	 * execute a DML
	 * @param sql
	 * @param params
	 * @return number of rows affected
	 */
	public int executeDML(String sql,Object[] params);
	/**
	 * Store an object to database if the object is not null
	 * If any number type field is null, put 0 in database 
	 * @param object
	 */
	public void insert(Object object);
	/**
	 * Delete a row from database by primary key value
	 * @param clazz table related class
	 * @param priKeyValue the primary key value of record
	 */
	public void delete(Class clazz,Object priKeyValue);
	/**
	 * Delete a row from database by object
	 * @param object
	 */
	public void delete(Object object);
	/**
	 * Update the record, only update the specific field by fieldName
	 * @param object the object to be updated in table
	 * @param fieldNames the array of field name to be updated in table
	 * @return number of rows affected
	 */
	public int update(Object object,String[] fieldNames);
	/**
	 * Update the record, update all the specific field except primary key
	 * @param object the object to be updated in table
	 * @return number of rows affected
	 */
	public int update(Object object);
	/**
	 * Select and return multiple results as a list of objects
	 * @param sql selection sql statement
	 * @param clazz target javabean Class object
	 * @param params sql parameters
	 * @return results as a list of objects
	 */
	public List queryRows(String sql, Class clazz,Object[] params);
	/**
	 * Select and return one object as result
	 * @param sql selection sql statement
	 * @param clazz target javabean Class object
	 * @param params sql parameters
	 * @return return one object as result
	 */
	public List queryUniqueRow(String sql, Class clazz,Object[] params);
	/**
	 * Select and return one value(one field value in one row) as result
	 * @param sql selection sql statement
	 * @param clazz target javabean Class object
	 * @param params sql parameters
	 * @return return one object as result
	 */
	public Object queryValue(String sql, Class clazz,Object[] params);
	/**
	 * Select and return one number(one field value in one row) as result
	 * @param sql selection sql statement
	 * @param clazz target javabean Class object
	 * @param params sql parameters
	 * @return return one number as result
	 */
	public Number queryNumber(String sql, Class clazz,Object[] params);
}
