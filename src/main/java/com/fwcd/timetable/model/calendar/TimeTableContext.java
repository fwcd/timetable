package com.fwcd.timetable.model.calendar;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.time.LocalDateInterval;

public class TimeTableContext {
	private final Observable<LocalDateInterval> dateInterval;
	
	public TimeTableContext(LocalDateInterval dateInterval) {
		this.dateInterval = new Observable<>(dateInterval);
	}
	
	public Observable<LocalDateInterval> getDateInterval() { return dateInterval; }
}
