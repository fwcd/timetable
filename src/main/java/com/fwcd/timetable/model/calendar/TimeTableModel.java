package com.fwcd.timetable.model.calendar;

import java.time.DayOfWeek;

import com.fwcd.fructose.Option;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.fructose.time.LocalDateInterval;
import com.fwcd.fructose.time.LocalTimeInterval;

public class TimeTableModel {
	private final ObservableList<TimeTableEntryModel> entries = new ObservableList<>();
	private final TimeTableContext context;
	
	public TimeTableModel(LocalDateInterval dateInterval) {
		context = new TimeTableContext(dateInterval);
	}
	
	public TimeTableEntryModel createEntry(String name, Option<Location> location, LocalTimeInterval timeInterval, DayOfWeek weekDay) {
		return new TimeTableEntryModel(context, name, location, timeInterval, weekDay);
	}
	
	public ObservableList<TimeTableEntryModel> getEntries() { return entries; }
}
