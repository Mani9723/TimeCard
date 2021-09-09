package com.example.timecard.Models.Objects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Shift
{
	private final LocalDate date;
	private final TimeCard timeCard;
	private final Employee employee;
	private LocalTime shiftDuration;
	private LocalTime mealDuration;
	private double grossPay;
	private double totalHours;

	public Shift(LocalDate date, TimeCard timeCard, Employee employee)
	{
		this.date = date;
		this.timeCard = timeCard;
		this.employee = employee;
		totalHours = 0.0;
		calculateShiftData();
	}

	private void calculateShiftData()
	{
		LocalTime tempShiftDuration = calculateTimeDifference(timeCard.getShiftBegin()
				,timeCard.getShiftEnd());

		if(timeCard.tookMealBreak()){
			mealDuration = calculateTimeDifference(timeCard.getMealBreakBegin()
					,timeCard.getMealBreakEnd());
			setShiftDuration(calculateTimeDifference(tempShiftDuration,mealDuration));
		}
		setShiftDuration(tempShiftDuration);
		setGrossPay(calculateGrossPay());
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
		double tempHours = calculateTotalHoursWorked(start.toSecondOfDay()
				- end.toSecondOfDay());
		BigDecimal bigDecimalTotalHours = new BigDecimal(tempHours);
		int hours = bigDecimalTotalHours.intValue();
		double decimalPart = bigDecimalTotalHours.subtract(new BigDecimal(hours)).
				doubleValue();
		int min = (int)(60*decimalPart);

		return LocalTime.of(hours,min);
	}

	public LocalDate getDate()
	{
		return date;
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
				", grossPay=$" + grossPay +
				'}';
	}
}
