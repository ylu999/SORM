package com.gvace.sorm.utils;

import java.sql.PreparedStatement;
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
}
