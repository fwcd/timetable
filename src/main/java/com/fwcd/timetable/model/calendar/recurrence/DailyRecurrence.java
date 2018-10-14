package com.fwcd.timetable.model.calendar.recurrence;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
		return (daysBetweenRepeats != 0)
			&& (ChronoUnit.DAYS.between(start, date) % daysBetweenRepeats == 0)
			&& (date.compareTo(start) >= 0)
			&& (end.map(e -> date.compareTo(e) <= 0).orElse(true));
	}
}
