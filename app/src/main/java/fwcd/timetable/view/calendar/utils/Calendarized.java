package fwcd.timetable.view.calendar.utils;

import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarModel;

public class Calendarized<E extends CalendarEntryModel> {
	private final E entry;
	private final CalendarModel calendar;
	
	public Calendarized(E entry, CalendarModel calendar) {
		this.entry = entry;
		this.calendar = calendar;
	}
	
	public E getEntry() { return entry; }
	
	public CalendarModel getCalendar() { return calendar; }
}
