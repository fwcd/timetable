package com.fwcd.timetable.model.calendar.recurrence;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import com.fwcd.fructose.Option;

public class WeeklyRecurrence implements Recurrence {
	private static final long serialVersionUID = -4477528751020266582L;
	private final LocalDate start;
	private final Option<LocalDate> end;
	private final int weeksBetweenRepeats;
	private final Set<DayOfWeek> weekDays;
	
	public WeeklyRecurrence(LocalDate start, Option<LocalDate> end, Set<DayOfWeek> weekDays, int weeksBetweenRepeats) {
		this.start = start;
		this.end = end;
		this.weeksBetweenRepeats = weeksBetweenRepeats;
		this.weekDays = weekDays;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		return weekDays.contains(date.getDayOfWeek())
			&& (ChronoUnit.WEEKS.between(start, date) % weeksBetweenRepeats == 0)
			&& (date.compareTo(start) >= 0)
			&& (end.map(e -> date.compareTo(e) <= 0).orElse(true));
	}
}
