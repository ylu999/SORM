package com.gvace.sorm.bean;

import java.util.List;
import java.util.Map;


/**
 * Store table structure info
 * @author Yushan Lu gvace.blogspot.com Feb 4, 2016
 *
 */
public class TableInfo {
	/**
	 * Table name
	 */
	private String tname;
	/**
	 * All fields info
	 */
	private Map<String,ColumnInfo> columns; 
	/**
	 * Combinations of primary keys, save to use later
	 */
	private List<ColumnInfo> priKeys; 
	/**
	 * Primary key name
	 * (We can only handle one primary key )
	 */
	private ColumnInfo onlyPriKey;
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public Map<String, ColumnInfo> getColumns() {
		return columns;
	}
	public void setColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}
	public ColumnInfo getOnlyPriKey() {
		return onlyPriKey;
	}
	public void setOnlyPriKey(ColumnInfo onlyPriKey) {
		this.onlyPriKey = onlyPriKey;
	}
	public TableInfo(String tname,List<ColumnInfo> priKeys, Map<String, ColumnInfo> columns) {
		super();
		this.tname = tname;
		this.priKeys = priKeys;
		this.columns = columns;
	}
	public TableInfo(String tname,List<ColumnInfo> priKeys, Map<String, ColumnInfo> columns,
			ColumnInfo onlyPriKey) {
		super();
		this.tname = tname;
		this.priKeys = priKeys;
		this.columns = columns;
		this.onlyPriKey = onlyPriKey;
	}
	public TableInfo() {
	
	}
	public List<ColumnInfo> getPriKeys() {
		return priKeys;
	}
	public void setPriKeys(List<ColumnInfo> priKeys) {
		this.priKeys = priKeys;
	}
}
