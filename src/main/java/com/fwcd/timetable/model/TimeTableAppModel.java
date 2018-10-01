package com.fwcd.timetable.model;

import com.fwcd.timetable.model.calendar.CalendarModel;

public class TimeTableAppModel {
	private final CalendarModel calendar = new CalendarModel();
	
	public CalendarModel getCalendar() { return calendar; }
}
