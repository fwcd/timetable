package com.fwcd.timetable.viewmodel;

import com.fwcd.timetable.model.calendar.CalendarCrateModel;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

public class TimeTableAppViewModel {
	private final CalendarsViewModel calendars = new CalendarsViewModel(new CalendarCrateModel());
	
	public CalendarsViewModel getCalendars() { return calendars; }
}
