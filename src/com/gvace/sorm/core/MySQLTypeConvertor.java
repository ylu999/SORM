package com.gvace.sorm.core;
/**
 * MySql data type and JAVA data type convertor 
 * @author Yushan Lu gvace.blogspot.com Feb 5, 2016
 *
 */
public class MySQLTypeConvertor implements TypeConvertor {
	@Override
	public String db2Java(String columnType) {
		switch(columnType.toLowerCase()){
			case "varchar": return "String";
			case "char": return "String";
			case "int": return "Integer";
			case "tinyint": return "Integer";
			case "smallint": return "Integer";
			case "mediumint": return "Integer";
			case "bigint": return "Long";
			case "double": return "Double";
			case "float": return "Float";
			case "clob" : return "java.sql.Clob";
			case "blob" : return "java.sql.blob";
			case "date" : return "java.sql.Date";
			case "time" : return "java.sql.Time";
			case "timestamp" : return "java.sql.Timestamp";
		}
		return null;
	}

	@Override
	public String java2db(String columnType) {
		// TODO Auto-generated method stub
		return null;
	}

}
