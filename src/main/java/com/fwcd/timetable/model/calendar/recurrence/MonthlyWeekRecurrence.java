package com.fwcd.timetable.model.calendar.recurrence;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Set;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.language.Language;

public class MonthlyWeekRecurrence implements Recurrence {
	private static final long serialVersionUID = 2552822285349700093L;
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
	
	@Override
	public String describeWith(Language language, DateTimeFormatter dateFormatter) {
		// TODO: Specify months
		return ((monthsBetweenRepeats == 1)
				? language.localize("monthly")
				: language.localize("eachxmonths", monthsBetweenRepeats))
			+ " " + language.localize("everyxth", weekOfMonth)
			+ " " + language.localize(dayOfWeek.name().toLowerCase())
			+ end.map(it -> " " + language.localize("untilx", dateFormatter.format(it))).orElse("");
	}
}
