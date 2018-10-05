package com.fwcd.timetable.model.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

public class WeeklyRecurrence implements Recurrence {
	private final LocalDate start;
	private final int weeksBetweenRepeats;
	private final Set<DayOfWeek> weekDays;
	
	public WeeklyRecurrence(LocalDate start, Set<DayOfWeek> weekDays, int weeksBetweenRepeats) {
		this.start = start;
		this.weeksBetweenRepeats = weeksBetweenRepeats;
		this.weekDays = weekDays;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		Period period = Period.between(start, date);
		return weekDays.contains(date.getDayOfWeek())
			&& (period.getDays() % (7 * weeksBetweenRepeats) == 0);
	}
}
