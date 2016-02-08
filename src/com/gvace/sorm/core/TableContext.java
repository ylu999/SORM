package com.gvace.sorm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.gvace.sorm.bean.ColumnInfo;
import com.gvace.sorm.bean.TableInfo;
import com.gvace.sorm.utils.JavaFileUtils;
import com.gvace.sorm.utils.StringUtils;

/**
 * Manage database table and class relationship, can generate class based on table
 * @author Yushan Lu gvace.blogspot.com 
 *
 */
public class TableContext {
	/**
	 * Table name as key, TableInfo as value
	 */
	public static Map<String,TableInfo> tables = new HashMap<String,TableInfo>();
	/**
	 * Class as key, TableInfo as value
	 */
	@SuppressWarnings("all")
	public static Map<Class,TableInfo> poClassTableMap = new HashMap<Class,TableInfo>();
	
	public TableContext(){
		
	}
	
	static{
		try(
			Connection conn = DBManager.getConn();
			){
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});
			while(tableRet.next()){
				//set table info to tables map
				String tableName = tableRet.getString("TABLE_NAME");
				TableInfo tableInfo = new TableInfo(tableName, new ArrayList<ColumnInfo>(), new HashMap<String,ColumnInfo>());
				tables.put(tableName, tableInfo);
				
				//set all columns to tableInfo
				try(ResultSet columnSet = dbmd.getColumns(null, "%", tableName, "%");){
					while(columnSet.next()){
						String columnName = columnSet.getString("COLUMN_NAME");
						String typeName = columnSet.getString("TYPE_NAME");
						ColumnInfo columnInfo = new ColumnInfo(columnName, typeName, 0);
						tableInfo.getColumns().put(columnName, columnInfo);
					}
				}
				//set all primary keys to tableInfo
				try(ResultSet primaryKeySet = dbmd.getPrimaryKeys(null, "%", tableName);){
					while(primaryKeySet.next()){
						ColumnInfo columnInfo = tableInfo.getColumns().get(primaryKeySet.getString("COLUMN_NAME"));
						columnInfo.setKeyType(1); //set as Primary Key
						tableInfo.getPriKeys().add(columnInfo);
					}
				}
				//if there is only one primary key, set the primary key as onlyPriKey
				if(tableInfo.getPriKeys().size()==1){
					tableInfo.setOnlyPriKey(tableInfo.getPriKeys().get(0));
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		//Update java PO files
		updatePOFiles();
		//load class for updated java PO files
		loadPOTables();
	}
	public static Map<String,TableInfo> getTableInfos(){
		return tables;
	}
	/**
	 * Load generated Class
	 */
	public static void loadPOTables(){
		try {
			for(TableInfo tableInfo:tables.values()){
				Class c = Class.forName(DBManager.getConfiguration().getPoPackage()+"."+StringUtils.firstCharUpperCase(tableInfo.getTname()));
				poClassTableMap.put(c, tableInfo);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void updatePOFiles(){
		for(TableInfo tableInfo: tables.values()){
			JavaFileUtils.createJavaPOFile(tableInfo,new MySQLTypeConvertor());
		}
	}
	@Test
	public void test(){
		
	}
}

























