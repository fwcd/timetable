package fwcd.timetable.viewmodel;

import java.nio.file.Path;

import fwcd.fructose.Option;
import fwcd.fructose.ReadOnlyObservable;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

public class TimeTableApiBackend implements TimeTableAppApi {
	private Option<ReadOnlyObservable<Option<Path>>> path = Option.empty();
	private Option<CalendarCrateViewModel> calendarCrate = Option.empty();
	private Option<Localizer> localizer = Option.empty();
	private Option<TemporalFormatters> formatters = Option.empty();
	
	public void setCurrentPath(ReadOnlyObservable<Option<Path>> path) { this.path = Option.of(path); }
	
	public void setCalendarCrate(CalendarCrateViewModel calendarCrate) { this.calendarCrate = Option.of(calendarCrate); }
	
	public void setLocalizer(Localizer localizer) { this.localizer = Option.of(localizer); }
	
	public void setFormatters(TemporalFormatters formatters) { this.formatters = Option.of(formatters); }
	
	@Override
	public ReadOnlyObservable<Option<Path>> getCurrentPath() { return path.unwrap("Path has not been initializer in TimeTableApiBackend"); }
	
	@Override
	public CalendarCrateViewModel getCalendarCrate() { return calendarCrate.unwrap("Calendar crate has not been initializer in TimeTableApiBackend"); }
	
	@Override
	public Localizer getLocalizer() { return localizer.unwrap("Localizer has not been initializer in TimeTableApiBackend"); }
	
	@Override
	public TemporalFormatters getFormatters() { return formatters.unwrap("Temporal formatters have not been initializer in TimeTableApiBackend"); }
}
