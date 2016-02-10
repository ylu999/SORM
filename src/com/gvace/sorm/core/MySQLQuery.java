package com.gvace.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gvace.sorm.bean.ColumnInfo;
import com.gvace.sorm.bean.TableInfo;
import com.gvace.sorm.utils.JDBCUtils;
import com.gvace.sorm.utils.ReflectUtils;

public class MySQLQuery implements Query {
	/*@Test
	public void test(){
		List<Employee> list = new MySQLQuery().queryRows("SELECT  `id`,  `name`,  `salary`,  `birthday`, `age`, `departmentId` FROM `employee`" +
				"WHERE age>? and salary<?", Employee.class, new Integer[]{20,5000});
		for(Employee emp: list){
			System.out.println(emp.getName());
		}
		
		Employee emp = new Employee();
		emp.setId(1);
		emp.setName("aaabbbbb");
		emp.setBirthday(new java.sql.Date(System.currentTimeMillis()+1000000000));
		//emp.setDepartmentId(0);
		update(emp,new String[]{"birthday"});
	}*/
	
	@Override
	public int executeDML(String sql, Object[] params) {
		//System.out.println(sql);
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

	@Override
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

	@Override
	public void delete(Class clazz, Object priKeyValue) {
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "DELETE FROM "+tableInfo.getTname()+" WHERE "+onlyPriKey.getName()+"=?;";
		Object[] params = new Object[]{priKeyValue};
		executeDML(sql, params);
	}

	@Override
	public void delete(Object object) {
		Class clazz = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(object.getClass());
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "DELETE FROM "+tableInfo.getTname()+" WHERE "+onlyPriKey.getName()+"=?;";
		//Using reflection to call setter or getter
		Object priKeyValue = ReflectUtils.invokeGet(object, onlyPriKey.getName());
		delete(clazz,priKeyValue);
	}



	@Override
	public int update(Object object, String[] fieldNames) {
		if(object==null)return 0;
		Class clazz = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(object.getClass());
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

	@Override
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

	@Override
	public List queryRows(String sql, Class clazz, Object[] params) {
		List result = null;
		try(
			Connection conn = DBManager.getConn();
			PreparedStatement ps = JDBCUtils.createPreparedStatement(conn,sql,params);
			ResultSet rs = ps.executeQuery();
		){
			ResultSetMetaData metaData = rs.getMetaData();
			
			while(rs.next()){
				if(result==null){
					result = new ArrayList();
				}
				Object rowObj = clazz.newInstance(); //call javabean constructor
				//for each column
				for(int i=0;i<metaData.getColumnCount();i++){
					String columnName = metaData.getColumnLabel(i+1);
					Object columnValue = rs.getObject(i+1);
					ReflectUtils.invokeSet(rowObj, columnName, columnValue);
				}
				result.add(rowObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List queryUniqueRow(String sql, Class clazz, Object[] params) {
		return null;
	}

	@Override
	public Object queryValue(String sql, Class clazz, Object[] params) {
		return null;
	}

	@Override
	public Number queryNumber(String sql, Class clazz, Object[] params) {
		return null;
	}
}
