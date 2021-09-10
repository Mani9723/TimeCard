package com.example.timecard.Utils;

import com.example.timecard.Models.Database.DatabaseHandler;

import java.sql.SQLException;


public class LoginValidator
{
	private static DatabaseHandler databaseHandler;

	public LoginValidator(DatabaseHandler handler)
	{
		databaseHandler = handler;
	}

	public boolean validateLogin(int empID, String empPass)
	{
		try {
			return databaseHandler.validateLogin(Integer.toString(empID),empPass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
