package com.project.timecard.Models.Database;

import com.project.timecard.Models.Constants.DatabaseFiles;
import com.project.timecard.Models.Constants.EmpTableColumns;
import com.project.timecard.Models.Constants.MainTableColumns;
import com.project.timecard.Models.Objects.Employee;
import com.project.timecard.Models.Objects.Paystub;
import com.project.timecard.Models.Objects.Shift;
import com.project.timecard.Models.Objects.TimeCard;
import com.project.timecard.Utils.EncryptPassword;
import com.project.timecard.Utils.Pair;
import com.project.timecard.Utils.PayrollCalendar;
import javafx.concurrent.Task;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

@SuppressWarnings("SqlResolve")
public class DatabaseHandler
{

	private static boolean isDBConnected = false;
	private static final String FILE_PATH = DatabaseFiles.MAIN_DB_FILE_PATH.toString();
	private static final String SQL_FILE = DatabaseFiles.EMPLOYEE_DATABASE_FILE_NAME.toString();
	private static final String TABLE_NAME = DatabaseFiles.EMPS_TABLE.name();
	private static Connection connection;

	/**
	 * Establishes a connection with the database.
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
			PayrollCalendar.updatePayrollCalendar(this);
			checkIfPayDay();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	private void checkIfPayDay()
	{
		String date = PayrollCalendar.isPayDay(this);
		if(!date.equalsIgnoreCase("Empty")) {
			Task<Boolean> task = new Task<>()
			{
				@Override
				protected Boolean call()
				{
					return updateDatabasePayDay(date);
				}
			};
			new Thread(task).start();
		}else{
			System.out.println("No Payroll Dates left this year");
		}
	}

	public void updatePayrollCalendarDates(ArrayList<LocalDate> dates)
	{
		PreparedStatement preparedStatement;
		for (LocalDate date : dates) {
			String query = "INSERT INTO PAYROLL (payweek, paid) VALUES (?,0)";
//			String query = "UPDATE PAYROLL set payweek = ?, paid = 0 where ROWID = ?";
			try {
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1,date.toString());
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private boolean updateDatabasePayDay(String date)
	{
		LocalDate payDate = LocalDate.parse(date);
		LocalDate start = payDate.minusWeeks(2).minusDays(5);
		LocalDate end = payDate.minusWeeks(1).plusDays(1);

		ArrayList<String> employees = getAllEmployeeIds();
		LinkedHashMap<Employee,Paystub> payrollMap = new LinkedHashMap<>();
		if(employees == null) {
			System.out.println("No Employees in this company");
		}else {
			System.out.println("Calculating employee Paystubs");
			for (String employee : employees) {
				Employee currEmployee = getEmployee(employee);
				ArrayList<Shift> shifts = getPayPeriodShifts(employee, start, end);
				if(shifts.size() > 0) {
					Paystub paystub = new Paystub(currEmployee, shifts);
					payrollMap.put(currEmployee, paystub);
				}else{
					updatePayrollDatabaseEmptyValues(date,employees);
				}
			}
			if(payrollMap.size() > 0) {
				System.out.println("Updating Payroll Database");
				updatePayrollDatabase(date, payrollMap);
			}else{
				System.out.println("No employees worked during this pay period.");
			}
		}
		return true;
	}

	public Pair<LocalDate,Integer> getLastDateOfPayroll()
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		String query = "SELECT payweek,paid FROM PAYROLL " +
				"where ROWID = (SELECT MAX(ROWID) FROM PAYROLL)";

		try{
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			return new Pair<>(LocalDate.parse(resultSet.getString("payweek"))
					,resultSet.getInt("paid"));
		}catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	private void updatePayrollDatabaseEmptyValues(String date, ArrayList<String> employees)
	{
		System.out.println("Updating payroll with zeros");
		for (String employee : employees) {
			String query = "UPDATE PAYROLL set paid = 2, '" + employee + "' = ? where payweek = ?";
			PreparedStatement preparedStatement;
			try{
				preparedStatement = connection.prepareStatement(query);
				String info =  "0.0,0.0";
				preparedStatement.setString(1,info);
				preparedStatement.setString(2,date);
				preparedStatement.executeUpdate();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}

	}

	public void deleteDatabase()
	{

	}

	private void updatePayrollDatabase(String payDate, LinkedHashMap<Employee,Paystub> payrollMap)
	{
		Set<Employee> employeeSet = payrollMap.keySet();
		for(Employee employee : employeeSet){
			String query = "UPDATE PAYROLL set paid = 1, '" + employee.getEmpId() + "' = ? where payweek = ?";
			PreparedStatement preparedStatement;
			try{
				preparedStatement = connection.prepareStatement(query);
				double[] data = payrollMap.get(employee).getPayStubInfo();
				String info =  data[0] + "," + data[1];
				preparedStatement.setString(1,info);
				preparedStatement.setString(2,payDate);
				preparedStatement.executeUpdate();
				System.out.println("Payroll database updated");
			}catch (SQLException e){
				e.printStackTrace();
			}
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
		System.out.println(connection.toString());
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
				+ MainTableColumns.empId     +"  text NOT NULL,\n"
				+ MainTableColumns.empPass   +"  text,\n"
				+ MainTableColumns.empPay       +"  text\n"
				+ MainTableColumns.empStatus +"  text\n"
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
	public void createNewEmployeeShiftTable(String user) throws SQLException
	{
		String query = "CREATE TABLE IF NOT EXISTS employee_"+user+ " (\n"
				+ EmpTableColumns.work_date.name() +" text PRIMARY KEY NOT NULL UNIQUE,\n"
				+ EmpTableColumns.shiftBegin.name() +" text,\n"
				+ EmpTableColumns.shiftEnd.name() +" text,\n"
				+ EmpTableColumns.mealBegin.name() +" text,\n"
				+ EmpTableColumns.mealEnd.name() +" text ,\n"
				+ EmpTableColumns.hours.name() +" text,\n"
				+ EmpTableColumns.overtimeHours.name() +" text ,\n"
				+ EmpTableColumns.grossPay.name() +" text,\n"
				+ EmpTableColumns.overtimePay.name() +" text, \n"
				+ EmpTableColumns.ytdHours +" text, \n"
				+ EmpTableColumns.ytdOvertimeHours.name() +" text, \n"
				+ EmpTableColumns.ytdGross.name() +" text, \n"
				+ EmpTableColumns.ytdOvertimePay.name() +" text )";
		System.out.println(query);
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

	public LocalDate getPayDate()
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT payweek,paid from PAYROLL " +
				"where paid < 1 and payweek < CURRENT_DATE;";

		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				if(resultSet.getInt("paid") == 0){
					return LocalDate.parse(resultSet.getString("payweek"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addNewEmployee(Employee employee) throws SQLException
	{
		PreparedStatement preparedStatement = null;

		String query = "INSERT into EMPS_TABLE(firstName, lastName,empId,empPass,empStatus,empPay,ytd_gross)" +
				"VALUES(?,?,?,?,?,?,?)";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, employee.getFirstName());
			preparedStatement.setString(2, employee.getLastName());
			preparedStatement.setString(3, Long.toString(employee.getEmpId()));
			preparedStatement.setString(4, employee.getEmpPass());
			preparedStatement.setString(5, "Employed");
			preparedStatement.setString(6, "12.0");
			preparedStatement.setString(7, "0.00");
			System.out.println("Adding new employee");
			preparedStatement.executeUpdate();
			System.out.println("Employee Added\nCreating Employee Shift table");
			createNewEmployeeShiftTable(Long.toString(employee.getEmpId()));
			addEmployeeToPayroll(employee.getEmpId());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			preparedStatement.close();
		}
	}

	private void addEmployeeToPayroll(long empId)
	{
		String query = "ALTER TABLE PAYROLL ADD '"+ empId + "' TEXT;";

		try {
			createPrepStmtExecute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean updateYtdValues(String empId, double ytd_gross, double ytd_hours, LocalDate date)
	{
		PreparedStatement preparedStatement;
		String query = "UPDATE employee_"+empId
				+ " set ytdGross = ?, ytdhours = ? where work_date = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,Double.toString(ytd_gross));
			preparedStatement.setString(2,Double.toString(ytd_hours).replace(".",":"));
			preparedStatement.setString(3,date.toString());
			preparedStatement.executeUpdate();
			return true;
		}catch (SQLException e){
			return false;
		}
	}

	public boolean updateShift(String empId, Shift shift)
	{
		PreparedStatement preparedStatement = null;
		String query = "UPDATE employee_" + empId
				+ " set shiftEnd = ?, mealBegin = ?, " +
				"mealEnd = ?, grossPay = ?, hours = ? where work_date = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			if(shift.getTimeCard().getShiftEnd() != null) {
				preparedStatement.setString(1, shift.getTimeCard()
						.getShiftEnd().toString());
			}else{
				preparedStatement.setString(1, null);
			}
			if(shift.getTimeCard().getMealBreakBegin() != null) {
				preparedStatement.setString(2, shift.getTimeCard()
						.getMealBreakBegin().toString());
			}else{
				preparedStatement.setString(2, null);
			}
			if(shift.getTimeCard().getMealBreakEnd() != null){
				preparedStatement.setString(3, shift.getTimeCard()
						.getMealBreakEnd().toString());
			}else {
				preparedStatement.setString(3, null);
			}
			if(shift.getGrossPay() != 0.0){
				preparedStatement.setString(4, Double.toString(shift.getGrossPay()));
				System.out.println("Pay added");
			}else {
				preparedStatement.setString(4, null);
			}
			if(shift.getShiftDuration() != null){
				preparedStatement.setString(5, shift.getShiftDuration().toString());
				System.out.println("hours added");
			}else {
				preparedStatement.setString(5, null);
			}
			preparedStatement.setString(6,shift.getDate().toString());

			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean addNewShift(Shift shift)
	{
		PreparedStatement preparedStatement = null;
		String query = "INSERT INTO employee_" + shift.getEmployee().getEmpId() + "(work_date, shiftBegin,ytdGross,ytdHours) " +
				"VALUES(?,?,?,?)";
		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,shift.getDate().toString());
			if(shift.getTimeCard().getShiftBegin() != null) {
				preparedStatement.setString(2, shift.getTimeCard()
						.getShiftBegin().toString());
			}else{
				preparedStatement.setString(1, null);
			}
			preparedStatement.setString(3,Double.toString(shift.getYtd_gross()));
			preparedStatement.setString(4,Double.toString(shift.getYtd_hours()).replace(".",":"));
			preparedStatement.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}
		finally {
			assert preparedStatement != null ;
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		System.out.println(query);
		return true;
	}

	public String getLastShift(String empId)
	{
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		String query = "SELECT "+EmpTableColumns.ytdGross+", "
				+ EmpTableColumns.ytdHours+" FROM employee_"+empId
				+" where ROWID == (SELECT MAX(ROWID) FROM employee_"+empId+");";
		System.out.println(query);

		try{
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return resultSet.getString("ytdGross") + ","
						+ resultSet.getString("ytdHours");
			}else{
				return null;
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return null;

	}

	public Shift getShift(String empId)
	{
		Shift shift = null;

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * from employee_"+ empId +" WHERE work_date = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,LocalDate.now().toString());
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				shift = new Shift();
				TimeCard timeCard = new TimeCard();
				if(resultSet.getString("shiftBegin") != null){
					timeCard.clockIn(LocalTime.parse(resultSet.getString("ShiftBegin")));
				}if(resultSet.getString("shiftEnd") != null){
					timeCard.clockOut(LocalTime.parse(resultSet.getString("ShiftEnd")));
				}if(resultSet.getString("mealBegin") != null){
					timeCard.clockMealBreakBegin(LocalTime.parse(resultSet.getString("mealBegin")));
				}if(resultSet.getString("mealEnd") != null){
					timeCard.clockMealBreakEnd(LocalTime.parse(resultSet.getString("mealEnd")));
				}
				shift.setDate(LocalDate.parse(resultSet.getString("work_date")));
				shift.setTimeCard(timeCard);
				return shift;
			}
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			if (preparedStatement != null && resultSet != null) {
				try {
					preparedStatement.close();
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return shift;
	}

	@SuppressWarnings("unused")
	public boolean shiftExists(String empId)
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * from employee_"+ empId +" WHERE work_date = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,LocalDate.now().toString());
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				if(resultSet.getString(EmpTableColumns.shiftBegin.name()) != null){
					return true;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			if (preparedStatement != null && resultSet != null) {
				try {
					preparedStatement.close();
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	public Employee getEmployee(String user)
	{
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		Employee employee = null;
		String query = "SELECT * FROM " + DatabaseFiles.EMPS_TABLE.name()
				+ " where empID = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,user);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				long empId = Long.parseLong(resultSet
						.getString(MainTableColumns.empId.name()));
				double pay = Double.parseDouble(resultSet
						.getString(MainTableColumns.empPay.name()));
				double ytd_gross = Double.parseDouble(resultSet.getString(MainTableColumns.ytd_gross.name()));
				employee = new Employee(
						resultSet.getString(MainTableColumns.firstName.name()),
						resultSet.getString(MainTableColumns.lastName.name()),
						empId,
						pay,
						ytd_gross);
			}

		}catch (SQLException e){
			e.printStackTrace();
		}

		return employee;

	}

	public boolean usernameExists(String user)
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * from "+DatabaseFiles.EMPS_TABLE.name()+" where empId = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,user);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				if(resultSet.getString("empId").equalsIgnoreCase(user))
					return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			try {
				preparedStatement.close();
				assert resultSet != null;
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public ArrayList<Shift> getPayPeriodShifts(String empId, LocalDate start, LocalDate end)
	{
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		String query = "SELECT * FROM employee_"+empId
				+ " where work_date < ? and work_date > ?";
		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,end.toString());
			preparedStatement.setString(2,start.toString());
			resultSet = preparedStatement.executeQuery();
			ArrayList<Shift> shifts = new ArrayList<>();
			while (resultSet.next()) {
				Shift shift = new Shift();
				String hours = resultSet.getString(EmpTableColumns.hours.name());
				shift.setDate(LocalDate.parse(resultSet.getString(EmpTableColumns.work_date.name())));
				shift.setGrossPay(Double.parseDouble(resultSet.getString(EmpTableColumns.grossPay.name())));
				shift.setShiftDuration(LocalTime.of(Integer.parseInt(hours.split(":")[0])
						, Integer.parseInt(hours.split(":")[1])));
				shift.setYtd_gross(Double.parseDouble(resultSet.getString(EmpTableColumns.ytdGross.name())));
				shifts.add(shift);
			}

			return shifts;
		}catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}


	private ArrayList<String> getAllEmployeeIds()
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT empId FROM EMPS_TABLE;";
		ArrayList<String> empIds = new ArrayList<>();
		try{
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				empIds.add(resultSet.getString(MainTableColumns.empId.name()));
			}
			return empIds;
		}catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}

}
