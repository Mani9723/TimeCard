package com.project.timecard.Models.Objects;

import java.time.LocalTime;

public class TimeCard
{
	private LocalTime shiftBegin;
	private LocalTime shiftEnd;
	private LocalTime mealBreakBegin;
	private LocalTime mealBreakEnd;
	private boolean isMealBreakStarted;
	private boolean isMealBreakFinished;

	public TimeCard()
	{
		this.shiftBegin = null;
		this.shiftEnd = null;
		this.mealBreakBegin = null;
		this.mealBreakEnd = null;
		this.isMealBreakStarted = false;
		this.isMealBreakFinished = false;
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
		this.isMealBreakStarted = true;
		this.mealBreakBegin = mealBreakBegin;
	}

	public void clockMealBreakEnd(LocalTime mealBreakEnd)
	{
		this.isMealBreakFinished = true;
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

	public boolean isMealBreakStarted()
	{
		return isMealBreakStarted;
	}

	public boolean isMealBreakFinished()
	{
		return isMealBreakFinished;
	}

	@Override
	public String toString()
	{
		return "TimeCard{" +
				"shiftBegin='" + shiftBegin + '\'' +
				", shiftEnd='" + shiftEnd + '\'' +
				", mealBreakBegin='" + mealBreakBegin + '\'' +
				", mealBreakEnd='" + mealBreakEnd + '\'' +
				", isMealStarted='" + isMealBreakStarted + '\'' +
				", isMealEnded='" + isMealBreakFinished + '\'' +
				'}';
	}
}
