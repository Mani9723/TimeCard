package com.project.timecard.Models.Objects;

import java.time.LocalTime;

public class TimeCard
{
	private LocalTime shiftBegin;
	private LocalTime shiftEnd;
	private LocalTime mealBreakBegin;
	private LocalTime mealBreakEnd;
	private boolean tookMealBreak;

	public TimeCard()
	{
		this.shiftBegin = null;
		this.shiftEnd = null;
		this.mealBreakBegin = null;
		this.mealBreakEnd = null;
		this.tookMealBreak = false;
	}

	public void clockIn(LocalTime shiftBegin)
	{
		this.shiftBegin = shiftBegin;
	}

	public void clockOut(LocalTime shiftEnd)
	{
		this.shiftEnd = shiftEnd;
	}

	public void clockMealBreakBegin(LocalTime mealBreakBegin)
	{
		this.tookMealBreak = true;
		this.mealBreakBegin = mealBreakBegin;
	}

	public void clockMealBreakEnd(LocalTime mealBreakEnd)
	{
		this.mealBreakEnd = mealBreakEnd;
	}

	public LocalTime getShiftBegin()
	{
		return shiftBegin;
	}

	public LocalTime getShiftEnd()
	{
		return shiftEnd;
	}

	public LocalTime getMealBreakBegin()
	{
		return mealBreakBegin;
	}

	public LocalTime getMealBreakEnd()
	{
		return mealBreakEnd;
	}

	public boolean tookMealBreak()
	{
		return tookMealBreak;
	}


	@Override
	public String toString()
	{
		return "TimeCard{" +
				"shiftBegin='" + shiftBegin + '\'' +
				", shiftEnd='" + shiftEnd + '\'' +
				", mealBreakBegin='" + mealBreakBegin + '\'' +
				", mealBreakEnd='" + mealBreakEnd + '\'' +
				", tookMealBreak='" + tookMealBreak + '\'' +
				'}';
	}
}
