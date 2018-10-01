package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.time.LocalTimeInterval;

public interface CalendarEventModel {
	String getType();
	
	String getName();
	
	Option<Location> getLocation();
	
	Observable<LocalTimeInterval> getTimeInterval();
	
	boolean occursOn(LocalDate date);
	
	boolean beginsOn(LocalDate date);
	
	boolean endsOn(LocalDate date);
}
