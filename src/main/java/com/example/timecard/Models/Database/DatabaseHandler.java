package com.example.timecard.Models.Database;

import com.example.timecard.Models.Constants.DatabaseFiles;
import com.example.timecard.Models.Constants.EmpTableColumns;
import com.example.timecard.Models.Constants.MainTableColumns;
import com.example.timecard.Models.Objects.Employee;
import com.example.timecard.Models.Objects.Shift;
import com.example.timecard.Models.Objects.TimeCard;
import com.example.timecard.Utils.EncryptPassword;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DatabaseHandler
{

	private static boolean isDBConnected = false;
	private static final String FILE_PATH = DatabaseFiles.MAIN_DB_FILE_PATH.toString();
	private static final String SQL_FILE = DatabaseFiles.EMPLOYEE_DATABASE.toString();
	private static final String TABLE_NAME = DatabaseFiles.EMPS_TABLE.name();
	private static Connection connection;

	/**
	 * Default Constructor. Establishes a connection with the database.
	 */
	public DatabaseHandler()
	{
		connection =  DatabaseConnection.connector(FILE_PATH+SQL_FILE);
		if(connection == null){
			System.out.println("Database not connected");
			System.exit(1);
		}
		try {
			checkIfTableExists();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	/**
	 * Method verifies that the database for the application exists.
	 * If it does not exist then it implies that this is a first time use
	 * and creates a new one in the same directory.
	 *
	 * @throws SQLException - SQL Exception
	 */
	private void checkIfTableExists() throws SQLException
	{
		assert connection != null;
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet resultSet = databaseMetaData.getTables(null, null,
				TABLE_NAME, null);
		if(!resultSet.next()){
			System.out.println("Empty Database Detected...\nCreating a new one...");
			createMainTable();
			System.out.println("Database was created...");
		}else{
			isDBConnected = true;
		}
		resultSet.close();
	}

	public boolean isDBConnected()
	{
		System.out.println("connnected");
		return isDBConnected;
	}

	/**
	 * Creates the main table with all the different users.
	 * General Table with all the users that are currently registered.
	 * Only contains the summary not the account transactions of any such detail.
	 * @throws SQLException - Exception
	 */
	private void createMainTable() throws SQLException
	{
		String query = "CREATE TABLE IF NOT EXISTS "+ DatabaseFiles.EMPS_TABLE +" (\n"
				+                    "id INTEGER PRIMARY KEY NOT NULL UNIQUE,\n"
				+ MainTableColumns.firstName + " text NOT NULL,\n"
				+ MainTableColumns.lastName  + " text NOT NULL,\n"
				+ MainTableColumns.empId     +" text NOT NULL,\n"
				+ MainTableColumns.empPass   +" text,\n"
				+ MainTableColumns.empStatus +" text\n"
				+ ")";
		createPrepStmtExecute(query);
		System.out.println("Main Table created");

	}

	/**
	 * Specific database table for each user.
	 * THis table details the transaction history of the user.
	 * @param user - Specific user
	 * @throws SQLException - Exception
	 */
	public void createEmployeeShiftTable(String user) throws SQLException
	{
		String query = "CREATE TABLE IF NOT EXISTS employee_"+user+ " (\n"
				+                    "id INTEGER PRIMARY KEY NOT NULL UNIQUE,\n"
				+ EmpTableColumns.shiftBegin.name() +" text NOT NULL,\n"
				+ EmpTableColumns.shiftEnd.name() +" text NOT NULL,\n"
				+ EmpTableColumns.mealBegin.name() +" text,\n"
				+ EmpTableColumns.mealEnd.name() +" text ,\n"
				+ EmpTableColumns.hours.name() +" text NOT NULL,\n"
				+ EmpTableColumns.overtimeHours.name() +" text ,\n"
				+ EmpTableColumns.grossPay.name() +" text NOT NULL,\n"
				+ EmpTableColumns.overtimePay.name() +" text \n"
				+ EmpTableColumns.ytdHours +" text \n"
				+ EmpTableColumns.ytdOvertimeHours.name() +" text \n"
				+ EmpTableColumns.ytdGross.name() +" text \n"
				+ EmpTableColumns.ytdOvertimePay.name() +" text \n"
				+")";
		createPrepStmtExecute(query);
		System.out.println("Statement Table created: " + user);
	}

	/**
	 * General method that generates a SQL statement to be used for queries.
	 * @param query - String query
	 * @throws SQLException - Exception
	 */
	private void createPrepStmtExecute(String query) throws SQLException
	{
		PreparedStatement preparedStatement = null;

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			preparedStatement.close();
		}
	}

	public boolean validateLogin(String user, String pass) throws SQLException
	{
		String encrypted = EncryptPassword.encryptPassword(pass);
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * from "
				+  DatabaseFiles.EMPS_TABLE.name()
				+ " where empID = ? and empPass = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,user);
			preparedStatement.setString(2,encrypted);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
			assert preparedStatement != null;
			preparedStatement.close();
			assert resultSet != null;
			resultSet.close();
		}
		return false;
	}

	private void addEmployee(Employee employee) throws SQLException
	{
		PreparedStatement preparedStatement = null;

		String query = "INSERT into EMPS_TABLE(firstName, lastName,empId,empPass,empStatus)" +
				"VALUES(?,?,?,?,?)";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, employee.getFirstName());
			preparedStatement.setString(2, employee.getLastName());
			preparedStatement.setString(3, Long.toString(employee.getEmpId()));
			preparedStatement.setString(4, employee.getEmpPass());
			preparedStatement.setString(5, "Employed");
			preparedStatement.executeUpdate();
			createEmployeeShiftTable(Long.toString(employee.getEmpId()));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			preparedStatement.close();
		}
	}

	public boolean addShift(Shift shift) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		String query = "INSERT INTO employee_" + "780643" + "(shiftBegin, shiftEnd, hours,grossPay) " +
				"VALUES(?,?,?,?)";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,shift.getTimeCard().getShiftBegin().toString());
			preparedStatement.setString(2,shift.getTimeCard().getShiftEnd().toString());
			preparedStatement.setString(3,shift.getShiftDuration().toString());
			preparedStatement.setString(4,Double.toString(shift.getGrossPay()));
			preparedStatement.execute();
		} finally {
			assert preparedStatement != null;
			preparedStatement.close();
		}

		System.out.println(query);
		return true;
	}

	public static void main(String[] args) throws SQLException
	{
		DatabaseHandler databaseHandler = new DatabaseHandler();
		databaseHandler.addShift(null);

	}



}
