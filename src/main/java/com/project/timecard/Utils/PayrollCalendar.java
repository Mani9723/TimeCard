package com.project.timecard.Utils;

import com.project.timecard.Models.Database.DatabaseHandler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class PayrollCalendar
{
	public static void updatePayrollCalendar(DatabaseHandler databaseHandler)
	{
		System.out.println("Checking if dates needs update");
		Pair data = databaseHandler.getLastDateOfPayroll();
		LocalDate date = (LocalDate)(data.getData1());
		if(date.getYear() < LocalDate.now().getYear() || (int)data.getData2() > 0){
			System.out.println("Updating dates Payroll Database");
			databaseHandler.updatePayrollCalendarDates(getFridayPaydays());
		}
	}

	private static ArrayList<LocalDate> getFridayPaydays()
	{
		LocalDate start = LocalDate.of(LocalDate.now().getYear()+1, 1, 1);
		LocalDate end = LocalDate.of(LocalDate.now().getYear()+1, 12,31);

		DayOfWeek dowOfStart = start.getDayOfWeek();
		int difference = DayOfWeek.FRIDAY.getValue() - dowOfStart.getValue();
		if (difference < 0) difference += 7;

		ArrayList<LocalDate> fridaysInRange = new ArrayList<>();

		LocalDate currentFriday = start.plusDays(difference);

		do {
			fridaysInRange.add(currentFriday);
			currentFriday = currentFriday.plusDays(14);
		} while (currentFriday.isBefore(end));

		return fridaysInRange;
	}

	public static String isPayDay(DatabaseHandler databaseHandler)
	{
		LocalDate payDate = databaseHandler.getPayDate();
		return payDate == null ? "Empty" : payDate.toString();
	}

}
