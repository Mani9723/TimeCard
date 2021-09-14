package com.project.timecard.Utils;

import com.project.timecard.Models.Database.DatabaseHandler;

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
