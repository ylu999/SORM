package com.gvace.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import com.gvace.sorm.bean.ColumnInfo;
import com.gvace.sorm.bean.TableInfo;
import com.gvace.sorm.utils.JDBCUtils;
import com.gvace.sorm.utils.ReflectUtils;
import com.sun.rowset.CachedRowSetImpl;

/*
 * For selection, core public function
 * @author Yushan Lu gvace.blogspot.com
 */
@SuppressWarnings("all")
public abstract class Query {
	public Object executeQueryTemplate(final String sql,final Class clazz,final Object[] params,final CallBack callBack){
		try(
			Connection conn = DBManager.getConn();
			PreparedStatement ps = JDBCUtils.createPreparedStatement(conn,sql,params);
			ResultSet rs = ps.executeQuery();
		){
			return callBack.execute(conn, ps, rs);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * execute a DML
	 * @param sql
	 * @param params
	 * @return number of rows affected
	 */
	public int executeDML(String sql,Object[] params) {
		int count = 0;
		try(
			Connection conn = DBManager.getConn();
			PreparedStatement ps = JDBCUtils.createPreparedStatement(conn, sql, params);
		){
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	/**
	 * Store an object to database if the object is not null
	 * If any number type field is null, put 0 in database 
	 * @param object
	 */
	public void insert(Object object) {
		if(object==null)return;
		Class clazz = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(object.getClass());
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO "+tableInfo.getTname()+" (");
		List<Object> paramsList = new ArrayList<Object>();
		int paramCount=0;
		for(Field field:fields){
			String fieldName = field.getName();
			Object fieldValue = ReflectUtils.invokeGet(object, fieldName);
			if(fieldValue==null){
				if(fieldValue instanceof Number && !tableInfo.getPriKeys().contains(tableInfo.getColumns().get(fieldName))) {
					fieldValue = 0;
				}
				else continue;
			}
			if(paramCount>0)sqlBuilder.append(",");
			sqlBuilder.append(fieldName);
			paramsList.add(fieldValue);
			paramCount++;
		}
		sqlBuilder.append(") VALUES (");
		for(int i=0;i<paramCount;i++){
			if(i>0)sqlBuilder.append(",");
			sqlBuilder.append("?");
		}
		sqlBuilder.append(");");
		executeDML(sqlBuilder.toString(), paramsList.toArray(new Object[paramCount]));
	}
	/**
	 * Delete a row from database by primary key value
	 * @param clazz table related class
	 * @param priKeyValue the primary key value of record
	 */
	public void delete(Class clazz,Object priKeyValue) {
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "DELETE FROM "+tableInfo.getTname()+" WHERE "+onlyPriKey.getName()+"=?;";
		Object[] params = new Object[]{priKeyValue};
		executeDML(sql, params);
	}
	/**
	 * Delete a row from database by object
	 * @param object
	 */
	public void delete(Object object) {
		Class clazz = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(object.getClass());
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "DELETE FROM "+tableInfo.getTname()+" WHERE "+onlyPriKey.getName()+"=?;";
		//Using reflection to call setter or getter
		Object priKeyValue = ReflectUtils.invokeGet(object, onlyPriKey.getName());
		delete(clazz,priKeyValue);
	}
	/**
	 * Update the record, only update the specific field by fieldName
	 * @param object the object to be updated in table
	 * @param fieldNames the array of field name to be updated in table
	 * @return number of rows affected
	 */
	public int update(Object object,String[] fieldNames) {
		if(object==null)return 0;
		Class clazz = object.getClass();
		System.out.println("TableInfo");
		TableInfo tableInfo = TableContext.poClassTableMap.get(object.getClass());
		System.out.println("TableInfo");
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sqlBuilder = new StringBuilder("UPDATE "+tableInfo.getTname()+" SET ");
		List<Object> paramsList = new ArrayList<Object>();
		int paramCount=0;
		for(String fieldName:fieldNames){
			if(tableInfo.getOnlyPriKey().getName().equals(fieldName)){
				continue;
			}
			Object fieldValue = ReflectUtils.invokeGet(object, fieldName);
			if(paramCount>0)sqlBuilder.append(",");
			sqlBuilder.append(fieldName+"=?");
			paramsList.add(fieldValue);
			paramCount++;
		}
		sqlBuilder.append(" WHERE "+tableInfo.getOnlyPriKey().getName()+"=?;");
		paramsList.add(ReflectUtils.invokeGet(object, tableInfo.getOnlyPriKey().getName()));
		return executeDML(sqlBuilder.toString(), paramsList.toArray());
	}
	/**
	 * Update the record, update all the specific field except primary key
	 * @param object the object to be updated in table
	 * @return number of rows affected
	 */
	public int update(Object object) {
		if(object==null)return 0;
		Class clazz = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(object.getClass());
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sqlBuilder = new StringBuilder("UPDATE "+tableInfo.getTname()+" SET ");
		List<Object> paramsList = new ArrayList<Object>();
		int paramCount=0;
		for(Field field:fields){
			String fieldName = field.getName();
			if(tableInfo.getOnlyPriKey().getName().equals(fieldName)){
				continue;
			}
			Object fieldValue = ReflectUtils.invokeGet(object, fieldName);
			if(paramCount>0)sqlBuilder.append(",");
			sqlBuilder.append(fieldName+"=?");
			paramsList.add(fieldValue);
			paramCount++;
		}
		sqlBuilder.append(" WHERE "+tableInfo.getOnlyPriKey().getName()+"=?;");
		paramsList.add(ReflectUtils.invokeGet(object, tableInfo.getOnlyPriKey().getName()));
		return executeDML(sqlBuilder.toString(), paramsList.toArray());
	}
	/**
	 * Select and return CachedRowSet
	 * @param sql selection sql statement
	 * @param params sql parameters
	 * @return results CachedRowSet
	 */
	public CachedRowSet queryRows(String sql,Object[] params) {
		try(
				Connection conn = DBManager.getConn();
				PreparedStatement ps = JDBCUtils.createPreparedStatement(conn,sql,params);
				ResultSet rs = ps.executeQuery();
			){
			CachedRowSet crs = new CachedRowSetImpl();
			crs.populate(rs);
			return crs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Select and return one CachedRowSet
	 * @param sql selection sql statement
	 * @param params sql parameters
	 * @return return one CachedRowSet
	 */
	public CachedRowSet queryUniqueRow(String sql,Object[] params) {
		CachedRowSet crs = queryRows(sql,params);
		try {
			return (crs!=null&&crs.next())?crs:null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Select and return multiple results as a list of objects
	 * @param sql selection sql statement
	 * @param clazz target javabean Class object
	 * @param params sql parameters
	 * @return results as a list of objects
	 */
	public List queryRows(final String sql,final Class clazz,final Object[] params) {
		return (List)executeQueryTemplate(sql, clazz, params, new CallBack(){
			@Override
			public Object execute(Connection conn, PreparedStatement ps, ResultSet rs) {
				ResultSetMetaData metaData;
				List list = null;
				try {
					metaData = rs.getMetaData();
					while(rs.next()){
						if(list==null){
							list = new ArrayList();
						}
						Object rowObj = clazz.newInstance(); //call javabean constructor
						//for each column
						for(int i=0;i<metaData.getColumnCount();i++){
							String columnName = metaData.getColumnLabel(i+1);
							Object columnValue = rs.getObject(i+1);
							ReflectUtils.invokeSet(rowObj, columnName, columnValue);
						}
						list.add(rowObj);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				return list;
			}			
		});
	}

	/**
	 * Select and return one object as result
	 * @param sql selection sql statement
	 * @param clazz target javabean Class object
	 * @param params sql parameters
	 * @return return one object as result
	 */
	public Object queryUniqueRow(String sql, Class clazz,Object[] params) {
		List list = queryRows(sql,clazz,params);
		return (list!=null&&list.size()>0)?list.get(0):null;
	}
	/**
	 * Select and return one value(one field value in one row) as result
	 * @param sql selection sql statement
	 * @param params sql parameters
	 * @return return one object as result
	 */
	public Object queryValue(String sql, Object[] params) {
		return executeQueryTemplate(sql, null, params, new CallBack(){
			@Override
			public Object execute(Connection conn, PreparedStatement ps, ResultSet rs) {
				try {
					if(rs.next()) return rs.getObject(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}
			
		});
	}
	/**
	 * Select and return one number(one field value in one row) as result
	 * @param sql selection sql statement
	 * @param params sql parameters
	 * @return return one number as result
	 */
	public Number queryNumber(String sql,Object[] params) {
		return (Number)queryValue(sql,params);
	}
	
	/**
	 * Select and return multiple results as a list of objects, by pageNum and page size
	 * @param sql selection sql statement
	 * @param clazz target javabean Class object
	 * @param params sql parameters
	 * @return results as a list of objects
	 * @param pageNum the number of page
	 * @param size size of each page
	 * @return
	 */
	public abstract List queryPagenate(String sql, Class clazz,Object[] params,int pageNum,int size);

	/**
	 * Select and return multiple results as a CachedRowSet, by pageNum and page size
	 * @param sql selection sql statement
	 * @param clazz target javabean Class object
	 * @param params sql parameters
	 * @return results as a list of objects
	 * @param pageNum the number of page
	 * @param size size of each page
	 * @return
	 */
	public abstract CachedRowSet queryPagenate(String sql,Object[] params,int pageNum,int size);
}
