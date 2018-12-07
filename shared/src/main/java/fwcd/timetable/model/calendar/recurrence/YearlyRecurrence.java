package fwcd.timetable.model.calendar.recurrence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import fwcd.fructose.Option;
import fwcd.timetable.model.language.Language;

public class YearlyRecurrence implements Recurrence {
	private static final long serialVersionUID = 3528256984722663329L;
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
		return (ChronoUnit.YEARS.between(start, date) % yearsBetweenRepeats == 0)
			&& (date.compareTo(start) >= 0)
			&& (end.map(e -> date.compareTo(e) <= 0).orElse(true));
	}
	
	@Override
	public String describeWith(Language language, DateTimeFormatter dateFormatter) {
		return ((yearsBetweenRepeats == 1)
				? language.localize("yearly")
				: language.localize("eachxyears", yearsBetweenRepeats))
			+ end.map(it -> " " + language.localize("untilx", dateFormatter.format(it))).orElse("");
	}
}
