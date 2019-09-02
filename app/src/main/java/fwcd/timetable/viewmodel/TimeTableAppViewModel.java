package fwcd.timetable.viewmodel;

import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

public class TimeTableAppViewModel {
	private final CalendarCrateViewModel calendars = new CalendarCrateViewModel();
	
	{
		int id = calendars.resetToDefaultCalendar();
		calendars.selectCalendar(id);
	}
	
	public CalendarCrateViewModel getCalendars() { return calendars; }
}
