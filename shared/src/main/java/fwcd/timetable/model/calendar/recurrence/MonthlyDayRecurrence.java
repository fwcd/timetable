package fwcd.timetable.model.calendar.recurrence;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import fwcd.fructose.Option;
import fwcd.timetable.model.language.Language;

public class MonthlyDayRecurrence implements Recurrence {
	private static final long serialVersionUID = -8602850189639879253L;
	private final LocalDate start;
	private final Option<LocalDate> end;
	private final Set<Month> months;
	private final int dayOfMonth;
	private final int monthsBetweenRepeats;
	
	public MonthlyDayRecurrence(LocalDate start, Option<LocalDate> end, Set<Month> months, int dayOfMonth, int monthsBetweenRepeats) {
		this.start = start;
		this.end = end;
		this.months = months;
		this.dayOfMonth = dayOfMonth;
		this.monthsBetweenRepeats = monthsBetweenRepeats;
	}
	
	@Override
	public boolean matches(LocalDate date) {
		return months.contains(date.getMonth())
			&& date.getDayOfMonth() == dayOfMonth
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
			+ " " + language.localize("everyxth", dayOfMonth)
			+ end.map(it -> " " + language.localize("untilx", dateFormatter.format(it))).orElse("");
	}
	
	@Override
	public Option<LocalDate> getEnd() {
		return end;
	}
}
