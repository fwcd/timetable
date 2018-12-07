package fwcd.timetable.viewmodel;

import java.nio.file.Path;

import fwcd.fructose.Option;
import fwcd.fructose.ReadOnlyObservable;
import fwcd.timetable.model.calendar.CalendarCrateModel;

public class TimeTableApiBackend implements TimeTableAppApi {
	private Option<ReadOnlyObservable<Option<Path>>> path = Option.empty();
	private Option<CalendarCrateModel> calendarCrate = Option.empty();
	
	public void setCurrentPath(ReadOnlyObservable<Option<Path>> path) { this.path = Option.of(path); }
	
	public void setCalendarCrate(CalendarCrateModel calendarCrate) { this.calendarCrate = Option.of(calendarCrate); }
	
	@Override
	public ReadOnlyObservable<Option<Path>> getCurrentPath() { return path.unwrap(); }
	
	@Override
	public CalendarCrateModel getCalendarCrate() { return calendarCrate.unwrap(); }
}