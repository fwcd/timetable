package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Set;

public class MonthlyDayRecurrence implements Recurrence {
	private final LocalDate start;
	private final Set<Month> months;
	private final int dayOfMonth;
	private final int monthsBetweenRepeats;
	
	public MonthlyDayRecurrence(LocalDate start, Set<Month> months, int dayOfMonth, int monthsBetweenRepeats) {
		this.start = start;
		this.months = months;
		this.dayOfMonth = dayOfMonth;
		this.monthsBetweenRepeats = monthsBetweenRepeats;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		return months.contains(date.getMonth())
			&& date.getDayOfMonth() == dayOfMonth
			&& (Period.between(start, date).getMonths() % monthsBetweenRepeats == 0);
	}
}
