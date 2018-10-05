package com.fwcd.timetable.model;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarModel;

public class TimeTableAppModel {
	private final ObservableList<CalendarModel> calendars = new ObservableList<>();
	
	public TimeTableAppModel() {
		calendars.add(new CalendarModel("Calendar"));
	}
	
	public ObservableList<CalendarModel> getCalendars() { return calendars; }
}
