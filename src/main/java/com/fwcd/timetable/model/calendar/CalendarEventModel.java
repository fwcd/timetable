package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.time.LocalTimeInterval;

/**
 * Common interface for a calendar entry (e.g. an
 * appointment, a time table entry, a recurring event, ...)
 */
public interface CalendarEventModel {
	Observable<String> getType();
	
	Observable<String> getName();
	
	Observable<Option<Location>> getLocation();
	
	/**
	 * Fetches the time interval of this calendar
	 * event.
	 * 
	 * <p>With single-day events, this interval
	 * is guaranteed to reflect the actual event time span,
	 * otherwise the interval will only consist
	 * of the start time on the first day and the
	 * end time on the last day.
	 * 
	 * <p><b>Careful attention is thus required
	 * when calling {@code merge}/{@code overlaps}
	 * on the obtained interval of a multi-day-event!
	 * In this case {@code getTimeIntervalOn} should be preferred.</b></p>
	 */
	Observable<LocalTimeInterval> getTimeInterval();
	
	boolean occursOn(LocalDate date);
	
	boolean beginsOn(LocalDate date);
	
	boolean endsOn(LocalDate date);
	
	default LocalTimeInterval getTimeIntervalOn(LocalDate date) {
		if (occursOn(date)) {
			boolean begins = beginsOn(date);
			boolean ends = endsOn(date);
			if (begins && ends) {
				LocalTimeInterval interval = getTimeInterval().get();
				return new LocalTimeInterval(interval.getStart(), interval.getEnd());
			} if (begins) {
				return new LocalTimeInterval(getTimeInterval().get().getStart(), LocalTime.MAX);
			} else if (ends) {
				return new LocalTimeInterval(LocalTime.MIN, getTimeInterval().get().getEnd());
			} else {
				return new LocalTimeInterval(LocalTime.MIN, LocalTime.MAX);
			}
		} else {
			throw new IllegalArgumentException("Calendar event does not occur on " + date);
		}
	}
}
