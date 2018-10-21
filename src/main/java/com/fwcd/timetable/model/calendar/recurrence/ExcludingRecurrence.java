package com.fwcd.timetable.model.calendar.recurrence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fwcd.timetable.model.language.Language;

public class ExcludingRecurrence implements Recurrence {
	private static final long serialVersionUID = -2092341529610795565L;
	private final Recurrence base;
	private final LocalDate excluded;
	
	public ExcludingRecurrence(Recurrence base, LocalDate excluded) {
		this.base = base;
		this.excluded = excluded;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		return !date.equals(excluded) && base.matches(date);
	}
	
	@Override
	public String describeWith(Language language, DateTimeFormatter dateFormatter) {
		return base.describeWith(language, dateFormatter)
			+ " " + language.localize("exceptfor")
			+ " " + dateFormatter.format(excluded);
	}
}
