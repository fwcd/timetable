package fwcd.timetable.model.calendar.recurrence;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import fwcd.fructose.Option;
import fwcd.timetable.model.language.Language;

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
	
	@Override
	public String describeWith(Language language, DateTimeFormatter dateFormatter) {
		// TODO: Specify week days
		return ((weeksBetweenRepeats == 1)
				? describeWeeklyRepetition(language)
				: language.localize("eachxweeks", weeksBetweenRepeats))
			+ end.map(it -> " " + language.localize("untilx", dateFormatter.format(it))).orElse("");
	}

	private String describeWeeklyRepetition(Language language) {
		String key;
		if (weekDays.size() == 1) {
			DayOfWeek day = weekDays.iterator().next();
			switch (day) {
				case MONDAY: key = "onmondays"; break;
				case TUESDAY: key = "ontuesdays"; break;
				case WEDNESDAY: key = "onwednesdays"; break;
				case THURSDAY: key = "onthursdays"; break;
				case FRIDAY: key = "onfridays"; break;
				case SATURDAY: key = "onsaturdays"; break;
				case SUNDAY: key = "onsundays"; break;
				default: throw new IllegalStateException("Invalid week day: " + day);
			}
		} else {
			key = "weekly";
		}
		return language.localize(key);
	}
	
	@Override
	public Option<LocalDate> getEnd() {
		return end;
	}
}
