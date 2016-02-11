package com.gvace.sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.gvace.sorm.bean.Configuration;
import com.gvace.sorm.pool.DBConnPool;

/**
 * Maintain connections, with connection pool
 * @author Yushan Lu gvace.blogspot.com 
 *
 */
public class DBManager {
	/**
	 * DB Configuration
	 */
	private static Configuration conf;
	/**
	 * DB connection pool
	 */
	private static DBConnPool pool;
	/**
	 * Initialize DB Configuration from property file
	 */
	static{
		  Properties pp = new Properties();
		  try {
		   pp.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")); // this one is better
		  } catch (IOException e) {
		   throw new RuntimeException(e);
		  }
		  String driver = pp.getProperty("driver");
		  String url = pp.getProperty("url");
		  String user = pp.getProperty("user");
		  String pwd = pp.getProperty("pwd");
		  String currentDB = pp.getProperty("currentDB");
		  String srcPath = pp.getProperty("srcPath");
		  String poPackage = pp.getProperty("poPackage");
		  String queryClass = pp.getProperty("queryClass");
		  int poolMinSize = Integer.parseInt(pp.getProperty("poolMinSize"));
		  int poolMaxSize = Integer.parseInt(pp.getProperty("poolMaxSize"));
		  
		  conf = new Configuration(driver, url, user, pwd, currentDB, srcPath, poPackage, queryClass, poolMinSize, poolMaxSize);
		  TableContext.class.getName();
	}
	/**
	 * get Configuration object
	 * @param rs
	 */
	public static Configuration getConfiguration() {
		return conf;
	}
	/**
	 * get Connection object
	 * @return
	 */
	public static Connection getConn(){
		if(pool==null) pool = new DBConnPool();
		return pool.getConnection();
		
		/*try{
			Class.forName(conf.getDriver());
			return DriverManager.getConnection(conf.getUrl(),conf.getUser(),conf.getPwd());
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}*/
	}
	/**
	 * create a Connection object
	 * @return
	 */
	public static Connection createConn(){
		try{
			Class.forName(conf.getDriver());
			return DriverManager.getConnection(conf.getUrl(),conf.getUser(),conf.getPwd());
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Close db resources
	 * @param rs
	 * @param ps
	 * @param conn
	 */
	public static void close(ResultSet rs,Statement ps,Connection conn){
		try{
			if(rs!=null) rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		try{
			if(ps!=null) ps.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		try{
			if(conn!=null) conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Close db resources
	 * @param ps
	 * @param conn
	 */
	public static void close(Statement ps,Connection conn){
		try{
			if(ps!=null) ps.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		try{
			if(conn!=null) conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Close db resources
	 * @param rs
	 */
	public static void close(ResultSet rs){
		try{
			if(rs!=null) rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**
	 * Close db resources
	 * @param ps
	 */
	public static void close(Statement ps){
		try{
			if(ps!=null) ps.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**
	 * Recycle connection to connection pool
	 * @param conn
	 */
	public static void close(Connection conn){
		pool.close(conn);
	}
	/**
	 * Close db resources
	 * @param conn
	 */
	public static void eliminate(Connection conn){
		try{
			if(conn!=null) conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
