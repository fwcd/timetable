package com.fwcd.timetable.model.calendar;

import com.fwcd.fructose.structs.ObservableList;

public class CalendarModel {
	private final ObservableList<CalendarEvent> events = new ObservableList<>();
	
	public ObservableList<CalendarEvent> getEvents() { return events; }
}
