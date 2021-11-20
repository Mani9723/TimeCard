package com.project.timecard.Models.Objects;

import java.time.LocalTime;
import java.util.ArrayList;

public class Paystub
{
	private Employee employee;
	private ArrayList<Shift> shifts;
	private double totalHours;
	private double totalGross;
	private double netPay;
	private double tax;


	private static final int BIWEEKLY_HOURS_THRESHOLD = 80;
	private static final float TAX_RATE = 0.20f;
	private static final float OVERTIME_RATE = 1.5f;


	public Paystub(Employee employee, ArrayList<Shift> shifts)
	{
		totalHours = 0.0;
		totalGross = 0.0;
		this.employee = employee;
		this.shifts = shifts;
	}

	public void calcData()
	{
		calcTotalHoursGross();
		adjustGross();
		calcNetPay();
	}

	private void calcTotalHoursGross()
	{
		for (Shift shift : shifts) {
			totalHours += Double.parseDouble(shift.getShiftDuration().toString()
					.replace(":","."));
			totalGross += shift.getYtd_gross();
		}
	}

	private void adjustGross()
	{
		if(totalHours > BIWEEKLY_HOURS_THRESHOLD){
			double overtimeHours = (totalHours - BIWEEKLY_HOURS_THRESHOLD);
			double overtimePay = overtimeHours * (employee.getPay()*OVERTIME_RATE);
			totalGross = ((employee.getPay()*overtimeHours)-totalGross) + overtimePay;
		}

	}

	private void calcNetPay()
	{
		netPay = totalGross*TAX_RATE;
		tax = totalGross - netPay;
	}

	@Override
	public String toString()
	{
		return "Paystub{" +
				"employee=" + employee +
				", totalHours=" + totalHours +
				", totalGross=" + totalGross +
				", netPay=" + netPay +
				", tax=" + tax +
				'}';
	}
}
