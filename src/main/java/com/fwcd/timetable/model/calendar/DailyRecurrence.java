package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.Period;

import com.fwcd.fructose.Option;

public class DailyRecurrence implements Recurrence {
	private final LocalDate start;
	private final Option<LocalDate> end;
	private final int daysBetweenRepeats;
	
	public DailyRecurrence(LocalDate start, Option<LocalDate> end, int daysBetweenRepeats) {
		this.start = start;
		this.end = end;
		this.daysBetweenRepeats = daysBetweenRepeats;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		return (Period.between(start, date).getDays() % daysBetweenRepeats == 0)
			&& (date.compareTo(start) >= 0)
			&& (end.map(e -> date.compareTo(e) <= 0).orElse(true));
	}
}
