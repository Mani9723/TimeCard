package com.project.timecard.Models.Objects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Shift
{
	private LocalDate date;
	private TimeCard timeCard;
	private Employee employee;
	private LocalTime shiftDuration;
	private LocalTime mealDuration;
	private double grossPay;
	private double totalHours;
	private double ytd_gross = 0.0;

	public  Shift()
	{
		this.date = null;
		this.timeCard = null;
		this.employee = null;
		this.shiftDuration = null;
		this.mealDuration = null;
		this.grossPay = 0.0;
		this.totalHours = 0.0;
	}

	public Shift(LocalDate date, TimeCard timeCard, Employee employee)
	{
		this.date = date;
		this.timeCard = timeCard;
		this.employee = employee;
		totalHours = 0.0;
		this.grossPay = 0.0;
	}


	public void calculateShiftData()
	{
		ytd_gross = employee.getYtd_gross();
		LocalTime tempShiftDuration = calculateTimeDifference(timeCard.getShiftBegin()
				,timeCard.getShiftEnd());

		if(timeCard.isMealBreakFinished()){
			mealDuration = calculateTimeDifference(timeCard.getMealBreakBegin()
					,timeCard.getMealBreakEnd());
			setShiftDuration(calculateTimeDifference(tempShiftDuration,mealDuration));
		}
		setShiftDuration(tempShiftDuration);
		setGrossPay(calculateGrossPay());
		ytd_gross += getGrossPay();
		System.out.println("Gross Pay: $" + getGrossPay());
		System.out.println("Hours worked: " + getShiftDuration());
	}

	private double calculateGrossPay()
	{
		this.totalHours = calculateTotalHoursWorked(this.shiftDuration
				.toSecondOfDay());
		return this.totalHours * this.employee.getPay();
	}

	private double calculateTotalHoursWorked(int secs)
	{
		double totalMinutesWorked = (double)(secs)/60.0;
		return totalMinutesWorked/60.0;
	}

	private LocalTime calculateTimeDifference(LocalTime start, LocalTime end)
	{
		double tempHours = Math.abs(calculateTotalHoursWorked(end.toSecondOfDay()
				- start.toSecondOfDay()));
		BigDecimal bigDecimalTotalHours = new BigDecimal(tempHours);
		int hours = bigDecimalTotalHours.intValue();
		double decimalPart = bigDecimalTotalHours.subtract(new BigDecimal(hours)).
				doubleValue();
		int min = (int)(60*decimalPart);

		return LocalTime.of(hours,min);
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}

	public void setTimeCard(TimeCard timeCard)
	{
		this.timeCard = timeCard;
	}

	public void setEmployee(Employee employee)
	{
		this.employee = employee;
	}


	public Employee getEmployee()
	{
		return this.employee;
	}

	public LocalDate getDate()
	{
		return date;
	}

	public double getYtd_gross()
	{
		return ytd_gross;
	}

	public void setYtd_gross(double ytd_gross){
		this.ytd_gross = ytd_gross;
	}

	public TimeCard getTimeCard()
	{
		return timeCard;
	}

	public LocalTime getShiftDuration()
	{
		return shiftDuration;
	}

	public double getGrossPay()
	{
		return grossPay;
	}
	public LocalTime getMealDuration()
	{
		return mealDuration;
	}

	public void setShiftDuration(LocalTime shiftDuration)
	{
		this.shiftDuration = shiftDuration;
	}

	public void setGrossPay(double grossPay)
	{
		this.grossPay = grossPay;
	}

	@Override
	public String toString()
	{
		return "Shift{" +
				"date=" + date +
				", timeCard=" + timeCard +
				", employee=" + employee +
				", shiftDuration=" + shiftDuration +
				", mealDuration=" + mealDuration +
				", grossPay=" + grossPay +
				", totalHours=" + totalHours +
				", ytd_gross=" + ytd_gross +
				'}';
	}
}
