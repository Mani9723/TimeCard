package com.project.timecard.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayrollCalendar
{

	private static final String path = "src/main/java/com/project/timecard/Utils/TextFiles/payrollCalendar.txt";

	public static void calculatePayrollCalendar()
	{
		File file = new File(path);
		if(file.exists()){
			if(file.length() > 10){
				System.out.println("file already filled");
				return;
			}
		}
		LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
		LocalDate end = LocalDate.of(LocalDate.now().getYear(), 12,31);

		DayOfWeek dowOfStart = start.getDayOfWeek();
		int difference = DayOfWeek.FRIDAY.getValue() - dowOfStart.getValue();
		if (difference < 0) difference += 7;

		ArrayList<LocalDate> fridaysInRange = new ArrayList<>();

		LocalDate currentFriday = start.plusDays(difference);

		try {
			int i = 1;
			FileWriter fileWriter = new FileWriter(path);
			do {
				fridaysInRange.add(currentFriday);
				fileWriter.write(i+",");
				fileWriter.write(currentFriday+"\n");
				currentFriday = currentFriday.plusDays(14);
				i++;
			} while (currentFriday.isBefore(end));
			fileWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
