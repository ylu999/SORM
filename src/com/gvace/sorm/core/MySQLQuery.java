package com.gvace.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gvace.sorm.bean.ColumnInfo;
import com.gvace.sorm.bean.TableInfo;
import com.gvace.sorm.utils.JDBCUtils;
import com.gvace.sorm.utils.ReflectUtils;

public class MySQLQuery implements Query {
	/*
	@Test
	public void test(){
		Employee emp = new Employee();
		emp.setName("aaa");
		emp.setBirthday(new java.sql.Date(System.currentTimeMillis()));
		//emp.setDepartmentId(0);
		insert(emp);
	}
	*/
	@Override
	public int executeDML(String sql, Object[] params) {
		int count = 0;
		try(
			Connection conn = DBManager.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
		){
			JDBCUtils.handleParams(ps, params);
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
		return 0;
	}

	@Override
	public int update(Object object) {
		return 0;
	}

	@Override
	public List queryRows(String sql, Class clazz, Object[] params) {
		return null;
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
