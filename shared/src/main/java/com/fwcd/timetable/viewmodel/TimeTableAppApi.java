package com.fwcd.timetable.viewmodel;

import java.nio.file.Path;

import com.fwcd.fructose.Option;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.timetable.model.calendar.CalendarCrateModel;

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
}