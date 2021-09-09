package com.example.timecard.Utils;

import java.util.Random;

public final class EmployeeIDGenerator
{
	public static long generateID()
	{
		Random random = new Random();
		return random.longs(700000L,900000L)
				.findFirst()
				.orElse(-1);
	}
}
