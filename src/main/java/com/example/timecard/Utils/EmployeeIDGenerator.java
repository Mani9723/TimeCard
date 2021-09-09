package com.example.timecard.Utils;

import java.util.Random;

public final class EmployeeIDGenerator
{
	public static int generateID()
	{
		Random random = new Random();
		return random.ints(700000,900000)
				.findFirst()
				.orElse(-1);
	}
}
