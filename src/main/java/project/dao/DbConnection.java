package project.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import project.exception.DbException;


public class DbConnection {
	 private static final String SCHEMA = "project";
	 private static final String HOST = "localhost";
	 private static final String USER = "project";
	 private static final String PASSWORD = "project";
	 private static final int PORT = 3306;
	 
	 
	 public static Connection getConnection() {
		String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false",HOST, PORT, SCHEMA, USER, PASSWORD);
		 
		//1 System.out.println("Connecting with url= " + url);
		 
		 try {
			Connection conn = DriverManager.getConnection(url);
			//System.out.println("Connection Success");
			return conn;
			
		} catch (SQLException e) {
			throw new DbException(e);
		}
	}

}
