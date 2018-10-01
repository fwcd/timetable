package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.fructose.time.LocalTimeInterval;

public interface CalendarEventModel {
	String getType();
	
	String getName();
	
	Option<Location> getLocation();
	
	Observable<LocalTimeInterval> getTimeInterval();
	
	boolean occursOn(LocalDate date);
	
	default ReadOnlyObservable<LocalTime> getStartTime() { return getTimeInterval().map(LocalTimeInterval::getStart); }
	
	default ReadOnlyObservable<LocalTime> getEndTime() { return getTimeInterval().map(LocalTimeInterval::getEnd); }
}
