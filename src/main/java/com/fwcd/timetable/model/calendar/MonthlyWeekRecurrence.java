package com.fwcd.timetable.model.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.WeekFields;
import java.util.Set;

public class MonthlyWeekRecurrence implements Recurrence {
	private final LocalDate start;
	private final Set<Month> months;
	private final int weekOfMonth;
	private final DayOfWeek dayOfWeek;
	private final int monthsBetweenRepeats;
	
	public MonthlyWeekRecurrence(LocalDate start, Set<Month> months, int weekOfMonth, DayOfWeek dayOfWeek, int monthsBetweenRepeats) {
		this.start = start;
		this.months = months;
		this.weekOfMonth = weekOfMonth;
		this.dayOfWeek = dayOfWeek;
		this.monthsBetweenRepeats = monthsBetweenRepeats;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		return months.contains(date.getMonth())
			&& date.getDayOfWeek().equals(dayOfWeek)
			&& (date.get(WeekFields.ISO.weekOfMonth()) == weekOfMonth)
			&& (Period.between(start, date).getMonths() % monthsBetweenRepeats == 0);
	}
}
