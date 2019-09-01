package fwcd.timetable.viewmodel;

import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

public class TimeTableAppViewModel {
	private final CalendarCrateViewModel calendars = new CalendarCrateViewModel();
	
	public CalendarCrateViewModel getCalendars() { return calendars; }
}
