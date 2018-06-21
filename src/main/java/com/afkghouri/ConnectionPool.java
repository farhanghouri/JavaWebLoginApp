package com.afkghouri;
import java.sql.*;
import java.util.ArrayList;
import java.util.List; 
public class ConnectionPool{

	private  List<Connection> list;
    private Connection connection;
	
	public  ConnectionPool(){ 
	}
	
	public void initializeConnectionPool() throws ClassNotFoundException, SQLException{
	    list =  new ArrayList<Connection>();
		Configuration config = Configuration.getInstance(); 
		Class.forName(config.DB_DRIVER);  // mysql software choose
	        
		for(int i=0; i<config.DB_MAX_CONNECTIONS; ++i){
			 
	       	 list.add(DriverManager.getConnection(config.DB_URL + config.DB_NAME, config.DB_USER_NAME, config.DB_PASSWORD));  // connect to server
	        
	    }

	}
	public  Connection getConnectionFromPool(){ 
		 connection = null;
        if(list.size() > 0)
           connection = list.remove(0);
		else 
        	System.out.println("connection pool overflow.");
        System.out.println("connection obtained: "+list.size());
      return connection;
        	 
  	}
	public void returnConnectionToPool(Connection connection){
      list.add(connection);
      System.out.println("connection released: "+list.size());
	}
	
}
