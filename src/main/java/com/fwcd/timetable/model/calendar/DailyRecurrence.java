package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.Period;

public class DailyRecurrence implements Recurrence {
	private final LocalDate start;
	private final int daysBetweenRepeats;
	
	public DailyRecurrence(LocalDate start, int daysBetweenRepeats) {
		this.start = start;
		this.daysBetweenRepeats = daysBetweenRepeats;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		return Period.between(start, date).getDays() % daysBetweenRepeats == 0;
	}
}
