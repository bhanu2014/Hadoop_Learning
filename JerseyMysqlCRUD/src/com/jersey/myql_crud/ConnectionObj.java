package com.jersey.myql_crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class ConnectionObj {
	public static Connection getConnection() throws ClassNotFoundException{
		
		Class.forName("com.mysql.jdbc.Driver");  
		  
		Connection con = null;
		try {
			con = DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/emp_data","root","");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;  
	}
}
