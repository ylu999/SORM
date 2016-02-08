package com.gvace.sorm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.gvace.model.Employee;
import com.gvace.sorm.bean.ColumnInfo;
import com.gvace.sorm.bean.TableInfo;
import com.gvace.sorm.utils.JDBCUtils;
import com.gvace.sorm.utils.ReflectUtils;

public class MySQLQuery implements Query {
	@Test
	public void test(){
		Employee emp = new Employee();
		emp.setId(2);
		delete(emp);
	}
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
