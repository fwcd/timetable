package com.fwcd.timetable.model.query;

import java.util.List;

import com.fwcd.timetable.model.calendar.CalendarEvent;

public interface QueryResult {
	List<CalendarEvent> getEvents();
	
	
}
