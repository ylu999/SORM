package com.gvace.sorm.bean;

/**
 * A column information of a table
 * @author Yushan Lu gvace.blogspot.com Feb 4, 2016
 * @version 0.8
 */
public class ColumnInfo {
	/**
	 * column name
	 */
	private String name;
	/**
	 * column data type
	 */
	private String dataType;
	/**
	 * column key type(0: normal key, 1: primary key, 2: outter key)
	 */
	private int keyType;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public int getKeyType() {
		return keyType;
	}
	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}
	public ColumnInfo(String name, String dataType, int keyType) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.keyType = keyType;
	}
	public ColumnInfo() {
	}
}
