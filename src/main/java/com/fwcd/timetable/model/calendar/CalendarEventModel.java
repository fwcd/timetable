package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fwcd.fructose.Observable;

public interface CalendarEventModel {
	String getType();
	
	String getName();
	
	Observable<LocalTime> getStartTime();
	
	Observable<LocalTime> getEndTime();
	
	boolean occursOn(LocalDate date);
}
