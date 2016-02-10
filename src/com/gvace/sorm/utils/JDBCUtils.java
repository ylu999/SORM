package com.gvace.sorm.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Common JDBC operations
 * @author yushan
 *
 */
public class JDBCUtils {
	public static void handleParams(PreparedStatement ps,Object[] params){
		if(params!=null){
			try {
				for(int i=0;i<params.length;i++){
					ps.setObject(i+1, params[i]);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static PreparedStatement createPreparedStatement(Connection conn,String sql,Object[] params) throws SQLException{
		PreparedStatement ps = conn.prepareStatement(sql);
		JDBCUtils.handleParams(ps, params);
		return ps;
	}
}
