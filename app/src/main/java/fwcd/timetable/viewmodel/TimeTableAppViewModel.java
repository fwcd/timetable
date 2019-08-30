package fwcd.timetable.viewmodel;

import fwcd.timetable.model.calendar.CalendarCrateModel;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

public class TimeTableAppViewModel {
	private final CalendarCrateViewModel calendars = new CalendarCrateViewModel(new CalendarCrateModel());
	
	public CalendarCrateViewModel getCalendars() { return calendars; }
}
