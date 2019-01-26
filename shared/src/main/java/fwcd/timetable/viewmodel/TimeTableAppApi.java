package fwcd.timetable.viewmodel;

import java.nio.file.Path;

import fwcd.fructose.Option;
import fwcd.fructose.ReadOnlyObservable;
import fwcd.timetable.model.calendar.CalendarCrateModel;

/**
 * An API that exposes details about the current
 * application state to plugins.
 */
public interface TimeTableAppApi {
	/**
	 * Provides the path to the currently
	 * loaded file (if there is one).
	 */
	ReadOnlyObservable<Option<Path>> getCurrentPath();
	
	/**
	 * Provides the calendar crate.
	 */
	CalendarCrateModel getCalendarCrate();
	
	/**
	 * Provides a facility that converts unlocalized
	 * strings into localized ones.
	 */
	Localizer getLocalizer();
	
	/**
	 * Provides formatters for temporal objects such
	 * as LocalDate, LocalTime or LocalDateTime.
	 */
	TemporalFormatters getFormatters();
}
