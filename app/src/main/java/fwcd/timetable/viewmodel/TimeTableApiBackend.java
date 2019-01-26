package fwcd.timetable.viewmodel;

import java.nio.file.Path;

import fwcd.fructose.Option;
import fwcd.fructose.ReadOnlyObservable;
import fwcd.timetable.model.calendar.CalendarCrateModel;

public class TimeTableApiBackend implements TimeTableAppApi {
	private Option<ReadOnlyObservable<Option<Path>>> path = Option.empty();
	private Option<CalendarCrateModel> calendarCrate = Option.empty();
	private Option<Localizer> localizer = Option.empty();
	
	public void setCurrentPath(ReadOnlyObservable<Option<Path>> path) { this.path = Option.of(path); }
	
	public void setCalendarCrate(CalendarCrateModel calendarCrate) { this.calendarCrate = Option.of(calendarCrate); }
	
	public void setLocalizer(Localizer localizer) { this.localizer = Option.of(localizer); }
	
	@Override
	public ReadOnlyObservable<Option<Path>> getCurrentPath() { return path.unwrap("Path has not been initializer in TimeTableApiBackend"); }
	
	@Override
	public CalendarCrateModel getCalendarCrate() { return calendarCrate.unwrap("Calendar crate has not been initializer in TimeTableApiBackend"); }
	
	@Override
	public Localizer getLocalizer() { return localizer.unwrap("Localizer has not been initializer in TimeTableApiBackend"); }
}
