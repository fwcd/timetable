package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Set;

import com.fwcd.fructose.Option;

public class MonthlyDayRecurrence implements Recurrence {
	private final LocalDate start;
	private final Option<LocalDate> end;
	private final Set<Month> months;
	private final int dayOfMonth;
	private final int monthsBetweenRepeats;
	
	public MonthlyDayRecurrence(LocalDate start, Option<LocalDate> end, Set<Month> months, int dayOfMonth, int monthsBetweenRepeats) {
		this.start = start;
		this.end = end;
		this.months = months;
		this.dayOfMonth = dayOfMonth;
		this.monthsBetweenRepeats = monthsBetweenRepeats;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		return months.contains(date.getMonth())
			&& date.getDayOfMonth() == dayOfMonth
			&& (Period.between(start, date).getMonths() % monthsBetweenRepeats == 0)
			&& (date.compareTo(start) >= 0)
			&& (end.map(e -> date.compareTo(e) <= 0).orElse(true));
	}
}
