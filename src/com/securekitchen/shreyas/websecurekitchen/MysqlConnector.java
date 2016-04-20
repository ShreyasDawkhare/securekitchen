package com.securekitchen.shreyas.websecurekitchen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnector {

	final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final static String DB_URL = "jdbc:mysql://localhost:3306/"+Constants.DATABASE_NAME+"";
	final static String USER = "root";
	final static String PASS = "root";
	
	public Connection getConnection()
	{
		Connection con = null;
		if(con==null)
		{
			try 
			{
				Class.forName(JDBC_DRIVER);
				con = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return con;
	}
	
	public void closeConnection(Connection con)
	{
		try 
		{
			if(con!=null)
			{
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}


