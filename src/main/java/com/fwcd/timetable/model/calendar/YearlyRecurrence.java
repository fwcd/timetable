package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.Period;

public class YearlyRecurrence implements Recurrence {
	private final LocalDate start;
	private final int yearsBetweenRepeats;
	
	public YearlyRecurrence(LocalDate start, int yearsBetweenRepeats) {
		this.start = start;
		this.yearsBetweenRepeats = yearsBetweenRepeats;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		return Period.between(start, date).getYears() % yearsBetweenRepeats == 0;
	}
}
