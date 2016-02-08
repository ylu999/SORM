package com.gvace.sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.gvace.sorm.bean.Configuration;

/**
 * Maintain connections, with connection pool
 * @author Yushan Lu gvace.blogspot.com 
 *
 */
public class DBManager {
	private static Configuration conf;
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
		  conf = new Configuration(driver, url, user, pwd, currentDB, srcPath, poPackage);
	}
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
	public static Connection getConn(){
		try{
			Class.forName(conf.getDriver());
			return DriverManager.getConnection(conf.getUrl(),conf.getUser(),conf.getPwd());
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
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
	public static void close(ResultSet rs){
		try{
			if(rs!=null) rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public static void close(Statement ps){
		try{
			if(ps!=null) ps.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public static void close(Connection conn){
		try{
			if(conn!=null) conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public static Configuration getConfiguration() {
		return conf;
	}
}
