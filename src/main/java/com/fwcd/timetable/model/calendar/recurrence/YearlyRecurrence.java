package com.fwcd.timetable.model.calendar.recurrence;

import java.time.LocalDate;
import java.time.Period;

import com.fwcd.fructose.Option;

public class YearlyRecurrence implements Recurrence {
	private final LocalDate start;
	private final Option<LocalDate> end;
	private final int yearsBetweenRepeats;
	
	public YearlyRecurrence(LocalDate start, Option<LocalDate> end, int yearsBetweenRepeats) {
		this.start = start;
		this.end = end;
		this.yearsBetweenRepeats = yearsBetweenRepeats;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		return (Period.between(start, date).getYears() % yearsBetweenRepeats == 0)
			&& (date.compareTo(start) >= 0)
			&& (end.map(e -> date.compareTo(e) <= 0).orElse(true));
	}
}
