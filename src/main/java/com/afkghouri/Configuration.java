package com.afkghouri;

public class Configuration{

	 private static Configuration obj;
	 public  String DB_USER_NAME;
	 public  String DB_PASSWORD;
	 public  String DB_URL;
	 public  String DB_DRIVER;
	 public  Integer DB_MAX_CONNECTIONS;
	 public  String DB_NAME;

	 private Configuration(){
		               	    init();
	 }

	  public static Configuration getInstance(){
	  	if(obj == null)
	               synchronized (Configuration.class) {  
		               if(obj == null)                       
		               		obj = new Configuration(); 
	    		   }  
	        return obj;
	  }

	  private  void init(){
	  	DB_DRIVER = "com.mysql.jdbc.Driver";
	  	DB_URL = "jdbc:mysql://127.0.0.1:3306/";
	    DB_NAME = "trigsoft";
	  	DB_USER_NAME = "root";
	  	DB_PASSWORD = "";  	
	  	DB_MAX_CONNECTIONS = 2; 
	  }
	}