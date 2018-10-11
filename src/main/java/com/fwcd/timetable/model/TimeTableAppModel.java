package com.fwcd.timetable.model;

import com.fwcd.timetable.model.calendar.CalendarCrateModel;

public class TimeTableAppModel {
	private final CalendarCrateModel calendarCrate = new CalendarCrateModel();
	
	public CalendarCrateModel getCalendarCrate() { return calendarCrate; }
}
