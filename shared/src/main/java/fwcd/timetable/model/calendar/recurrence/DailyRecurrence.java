package fwcd.timetable.model.calendar.recurrence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import fwcd.fructose.Option;
import fwcd.timetable.model.language.Language;

public class DailyRecurrence implements Recurrence {
	private static final long serialVersionUID = -2555555069797368936L;
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
	
	@Override
	public String describeWith(Language language, DateTimeFormatter dateFormatter) {
		return ((daysBetweenRepeats == 1)
				? language.localize("daily")
				: language.localize("eachxdays", daysBetweenRepeats))
			+ end.map(it -> " " + language.localize("untilx", dateFormatter.format(it))).orElse("");
	}
}
