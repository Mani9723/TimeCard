package com.project.timecard.Models.Objects;

import com.project.timecard.Utils.EmployeeIDGenerator;
import com.project.timecard.Utils.EncryptPassword;

public class Employee
{
	private final String firstName;
	private final String lastName;
	private long empId;
	private String empPass;

	public void setYtd_gross(double ytd_gross)
	{
		this.ytd_gross = ytd_gross;
	}

	private double ytd_gross;
	private final double pay;

	public Employee(String firstName, String lastName, String empPass, double pay)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.empId = EmployeeIDGenerator.generateID();
		this.empPass = EncryptPassword.encryptPassword(empPass);
		this.pay = pay;
	}

	public Employee(String firstName, String lastName, Long empId, double pay,double ytd_gross)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.empId = empId;
		this.pay = pay;
		this.ytd_gross = ytd_gross;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public double getYtd_gross()
	{
		return ytd_gross;
	}

	public String getLastName()
	{
		return lastName;
	}

	public long getEmpId()
	{
		return empId;
	}

	public void setEmpId(Long id)
	{
		this.empId = id;
	}

	public String getEmpPass()
	{
		return empPass;
	}

	public double getPay()
	{
		return pay;
	}

	@Override
	public String toString()
	{
		return "Employee{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", empId=" + empId +
				", empPass='" + empPass + '\'' +
				", pay=$" + pay +
				'}';
	}
}
