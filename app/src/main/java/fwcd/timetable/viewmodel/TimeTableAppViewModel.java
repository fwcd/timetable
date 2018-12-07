package fwcd.timetable.viewmodel;

import fwcd.timetable.model.calendar.CalendarCrateModel;
import fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

public class TimeTableAppViewModel {
	private final CalendarsViewModel calendars = new CalendarsViewModel(new CalendarCrateModel());
	
	public CalendarsViewModel getCalendars() { return calendars; }
}
