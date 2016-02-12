package com.gvace.sorm.pool;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.gvace.sorm.core.DBManager;

public class DBConnPool {
	/**
	 * Connection pool object
	 */
	private List<Connection> pool;
	/**
	 * Min Connection Number
	 */
	private static final int POOL_MIN_SIZE = DBManager.getConfiguration().getPoolMinSize();
	/**
	 * Max Connection Number
	 */
	private static final int POOL_MAX_SIZE = DBManager.getConfiguration().getPoolMaxSize();
	/**
	 * Constructor
	 */
	public DBConnPool() {
		initPool();
	}
	/**
	 * Initialize connection pool, make connections count to at least POOL_MIN_SIZE
	 */
	public void initPool(){
		if(pool==null){
			pool = new ArrayList<Connection>();
		}
		while(pool.size()<POOL_MIN_SIZE){
			Connection conn = DBManager.createConn();
			if(conn==null) throw new RuntimeException("DBManager.createConn returns null");
			pool.add(conn);
		}
	}
	/**
	 * Get a connection from connection pool
	 * @return
	 */
	public synchronized Connection getConnection(){
		if(pool.size()<=0){
			initPool();
		}
		int last_index = pool.size()-1;
		Connection conn = pool.get(last_index);
		pool.remove(last_index);
		return conn;
	}
	/**
	 * Put connection back to connection pool
	 * @param conn
	 */
	public synchronized void close(Connection conn){
		if(pool.size()>=POOL_MAX_SIZE){
			DBManager.eliminate(conn);
		}
		else if(pool.contains(conn)){
			throw new RuntimeException("Pool leak!");
		}
		else pool.add(conn);
	}
}
