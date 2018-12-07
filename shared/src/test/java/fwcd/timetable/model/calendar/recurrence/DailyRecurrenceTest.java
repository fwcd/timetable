package fwcd.timetable.model.calendar.recurrence;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import fwcd.fructose.Option;

import org.junit.Test;

public class DailyRecurrenceTest {
	@Test
	public void testDailyRecurrence() {
		LocalDate start = LocalDate.of(2007, 05, 01);
		LocalDate end = start.plusDays(36);
		int daysBetweenRepeats = 2;
		Recurrence dailyRecurrence = new DailyRecurrence(start, Option.empty(), daysBetweenRepeats);
		
		for (LocalDate day = start; day.isBefore(end); day = day.plusDays(daysBetweenRepeats)) {
			assertTrue(day + " should be a daily recurrence of " + start, dailyRecurrence.matches(day));
		}
	}
}
