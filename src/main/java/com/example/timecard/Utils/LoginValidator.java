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

	public boolean validateLogin(String empID, String empPass) throws SQLException
	{
		return databaseHandler.validateLogin(empID,empPass);
	}
}
