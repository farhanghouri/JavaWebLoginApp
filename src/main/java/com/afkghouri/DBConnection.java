package com.afkghouri;

import java.sql.*; 

class DBConnection{
	private static DBConnection obj; 
	private ConnectionPool connectionPool;

	private DBConnection() throws ClassNotFoundException, SQLException{
		    initializeConnectionPool();
	}

	public static DBConnection getInstance() throws ClassNotFoundException, SQLException{
		if(obj == null)
               synchronized (DBConnection.class) {  
	               if(obj == null)                   
	               		obj = new DBConnection();
    		   }  
        return obj;
	}

	private void initializeConnectionPool() throws ClassNotFoundException, SQLException{
		connectionPool = new ConnectionPool();
		connectionPool.initializeConnectionPool();
	}

	public  MyConnection getConnectionFromPool(){  
		return new MyConnection(connectionPool.getConnectionFromPool(), connectionPool);  
   	}
	 
}