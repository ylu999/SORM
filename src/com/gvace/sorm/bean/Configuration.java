package com.gvace.sorm.bean;
/**
 * Manage configuration info
 * @author Yushan Lu gvace.blogspot.com Feb 4, 2016
 *
 */
public class Configuration {
	/**
	 * Driver class
	 */
	private String driver;
	/**
	 * database url
	 */
	private String url;
	/**
	 * database username
	 */
	private String user;
	/**
	 * database password
	 */
	private String pwd;
	/**
	 * current database name
	 */
	private String currentDB;
	/**
	 * project source directory
	 */
	private String srcPath;
	/**
	 * the package of all generated models, po=Persistence Object
	 */
	private String poPackage;
	/**
	 * using specify the implemented query class
	 */
	private String queryClass;
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getCurrentDB() {
		return currentDB;
	}
	public void setCurrentDB(String currentDB) {
		this.currentDB = currentDB;
	}
	public String getSrcPath() {
		return srcPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	public String getPoPackage() {
		return poPackage;
	}
	public void setPoPackage(String poPackage) {
		this.poPackage = poPackage;
	}
	
	public String getQueryClass() {
		return queryClass;
	}
	public void setQueryClass(String queryClass) {
		this.queryClass = queryClass;
	}
	public Configuration(String driver, String url, String user, String pwd,
			String currentDB, String srcPath, String poPackage, String queryClass) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.currentDB = currentDB;
		this.srcPath = srcPath;
		this.poPackage = poPackage;
		this.queryClass = queryClass;
	}
	public Configuration() {
	}
}
