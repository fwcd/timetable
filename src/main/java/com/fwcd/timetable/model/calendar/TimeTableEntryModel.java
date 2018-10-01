package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.time.LocalTimeInterval;

public class TimeTableEntryModel implements CalendarEventModel {
	private final TimeTableContext context;
	private final String name;
	private final Option<Location> location;
	private final Observable<LocalTimeInterval> timeInterval;
	
	TimeTableEntryModel(TimeTableContext context, String name, Option<Location> location, LocalTimeInterval timeInterval) {
		this.context = context;
		this.name = name;
		this.location = location;
		this.timeInterval = new Observable<>(timeInterval);
	}
	
	@Override
	public String getName() { return name; }
	
	@Override
	public Option<Location> getLocation() { return location; }
	
	@Override
	public Observable<LocalTimeInterval> getTimeInterval() { return timeInterval; }
	
	@Override
	public String getType() { return CommonEventType.TIME_TABLE_ENTRY; }

	@Override
	public boolean occursOn(LocalDate date) { return context.getDateInterval().get().contains(date); }
}
