package fwcd.timetable.model.calendar;

import java.time.LocalDate;

/**
 * An entry in the calenndar such as an appointment
 * or a task. It holds an ID reference to its parent
 * calendar.
 */
public interface CalendarEntryModel {
	/**
	 * Fetches the "type" of this entry, e.g. appointment
	 * or task. Known entry types can be found in
	 * {@link CommonEntryType}.
	 */
	String getType();
	
	String getName();
	
	String getDescription();
	
	int getCalendarId();
	
	boolean occursOn(LocalDate date);
	
	<T> T accept(CalendarEntryVisitor<T> visitor);
}
