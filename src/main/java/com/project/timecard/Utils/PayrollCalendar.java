package com.project.timecard.Utils;

import com.project.timecard.Models.Database.DatabaseHandler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class PayrollCalendar
{

	private static final String path = "src/main/java/com/project/timecard/Utils/TextFiles/payrollCalendar.txt";

	public static void updatePayrollCalendar(DatabaseHandler databaseHandler)
	{
		System.out.println("Checking if dates needs update");
		LocalDate date = databaseHandler.getLastDateOfPayroll();
		if(date.getYear() < LocalDate.now().getYear()){
			System.out.println("Calculating new dates");
			ArrayList<LocalDate> dates = getFridayPaydays();
			System.out.print("Updating dates Payroll Database:\n"+dates);
			databaseHandler.updatePayrollCalendarDates(dates);
		}
	}

	private static ArrayList<LocalDate> getFridayPaydays()
	{
		LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
		LocalDate end = LocalDate.of(LocalDate.now().getYear(), 12,31);

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
		return payDate.toString();
	}

	//	public static void initPayrollCalendar()
//	{
//		File file = new File(path);
//		if(file.exists()){
//			if(file.length() > 10){
//				System.out.println("file already filled");
//				return;
//			}
//		}
//		LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
//		LocalDate end = LocalDate.of(LocalDate.now().getYear(), 12,31);
//
//		DayOfWeek dowOfStart = start.getDayOfWeek();
//		int difference = DayOfWeek.FRIDAY.getValue() - dowOfStart.getValue();
//		if (difference < 0) difference += 7;
//
//		ArrayList<LocalDate> fridaysInRange = new ArrayList<>();
//
//		LocalDate currentFriday = start.plusDays(difference);
//
//		try {
//			int i = 1;
//			FileWriter fileWriter = new FileWriter(path);
//			do {
//				fridaysInRange.add(currentFriday);
//				fileWriter.write(currentFriday+"\n");
//				currentFriday = currentFriday.plusDays(14);
//				i++;
//			} while (currentFriday.isBefore(end));
//			fileWriter.close();
//			System.out.println("Successfully wrote to the file.");
//		} catch (IOException e) {
//			System.out.println("An error occurred.");
//			e.printStackTrace();
//		}
//	}
}
