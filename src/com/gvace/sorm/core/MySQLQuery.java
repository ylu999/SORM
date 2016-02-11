package com.gvace.sorm.core;

import java.util.List;

import javax.sql.rowset.CachedRowSet;

public class MySQLQuery extends Query {

	@Override
	public List queryPagenate(String sql, Class clazz, Object[] params,	int pageNum, int size) {
		return null;
	}

	@Override
	public CachedRowSet queryPagenate(String sql, Object[] params, int pageNum,	int size) {
		return null;
	}

	
}
