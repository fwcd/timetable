package fwcd.timetable.view.calendar.utils;

import fwcd.timetable.viewmodel.calendar.CalendarViewModel;

public class Calendarized<E> {
	private final E entry;
	private final CalendarViewModel calendar;
	
	public Calendarized(E entry, CalendarViewModel calendar) {
		this.entry = entry;
		this.calendar = calendar;
	}
	
	public E getEntry() { return entry; }
	
	public CalendarViewModel getCalendar() { return calendar; }
}
