package com.example.timecard.Models.Database;

import java.sql.Connection;
import java.sql.DriverManager;

final class DatabaseConnection
{
	static Connection connector(String file)
	{
		try{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:"+file);
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
}
