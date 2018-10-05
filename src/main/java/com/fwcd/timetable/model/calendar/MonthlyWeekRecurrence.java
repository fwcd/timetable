package com.fwcd.timetable.model.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.WeekFields;
import java.util.Set;

import com.fwcd.fructose.Option;

public class MonthlyWeekRecurrence implements Recurrence {
	private final LocalDate start;
	private final Option<LocalDate> end;
	private final Set<Month> months;
	private final int weekOfMonth;
	private final DayOfWeek dayOfWeek;
	private final int monthsBetweenRepeats;
	
	public MonthlyWeekRecurrence(LocalDate start, Option<LocalDate> end, Set<Month> months, int weekOfMonth, DayOfWeek dayOfWeek, int monthsBetweenRepeats) {
		this.start = start;
		this.end = end;
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
			&& (Period.between(start, date).getMonths() % monthsBetweenRepeats == 0)
			&& (date.compareTo(start) >= 0)
			&& (end.map(e -> date.compareTo(e) <= 0).orElse(true));
	}
}
