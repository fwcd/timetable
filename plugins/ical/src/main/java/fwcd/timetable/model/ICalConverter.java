package fwcd.timetable.model;

import java.util.Collection;

import fwcd.timetable.model.calendar.CalendarEntryModel;
import net.fortuna.ical4j.model.Calendar;

/**
 * A converter between TimeTable and iCal events.
 */
public interface ICalConverter {
	/** Converts an iCal calendar to TimeTable calendar entries. */
	Collection<? extends CalendarEntryModel> toTimeTableEntries(Calendar iCal, int calendarId);
	
	/** Converts TimeTable calendar entries to an iCal calendar. */
	Calendar toiCalCalendar(Collection<? extends CalendarEntryModel> entries);
}
