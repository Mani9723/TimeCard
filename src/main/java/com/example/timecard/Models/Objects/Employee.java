package com.example.timecard.Models.Objects;

class Employee
{
	private final String firstName;
	private final String lastName;
	private final long empId;
	private final String empPass;
	private final double pay;

	public Employee(String firstName, String lastName, long empId, String empPass, double pay)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.empId = empId;
		this.empPass = empPass;
		this.pay = pay;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public long getEmpId()
	{
		return empId;
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
