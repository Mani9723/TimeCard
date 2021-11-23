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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PayrollCalendar
{

	private static final String path = "src/main/java/com/project/timecard/Utils/TextFiles/payrollCalendar.txt";

	public static void initPayrollCalendar()
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

	// TODO: Append "Done" after completing the payweek
	public static String isPayDay()
	{
//		String date = LocalDate.now().toString();
		Stream<String> dates = null;
		try {
//			dates = Files.lines(Path.of(path)).filter(s -> s.trim().contains(date));
			dates = Files.lines(Path.of(path));
			String[] data = dates.collect(Collectors.joining("\n")).split("\n");
			dates.close();
			for (String datum : data) {
				if (datum.contains("Done")) {
					continue;
				}
				int result = LocalDate.now().toString().compareTo(datum.split(",")[1]);
				if (result > 0) {
					return datum;
				}

			}
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert dates != null;
		return null;
	}
}
