package fwcd.timetable.model.calendar.recurrence;

import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarConstants;

import org.junit.Test;

public class WeeklyRecurrenceTest {
	@Test
	public void testWeeklyRecurrence() {
		LocalDate start = LocalDate.of(2007, 05, 01);
		LocalDate end = start.plusDays(36);
		Recurrence weeklyRecurrence = new WeeklyRecurrence(start, Option.empty(), Arrays.stream(DayOfWeek.values()).collect(Collectors.toSet()), 1);
		
		for (LocalDate day = start; day.isBefore(end); day = day.plusDays(CalendarConstants.DAYS_OF_WEEK)) {
			assertTrue(day + " should be a weekly recurrence of " + start, weeklyRecurrence.matches(day));
		}
	}
}
