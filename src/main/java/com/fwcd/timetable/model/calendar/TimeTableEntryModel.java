package com.fwcd.timetable.model.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.time.LocalTimeInterval;

public class TimeTableEntryModel implements CalendarEventModel {
	private final TimeTableContext context;
	private final String name;
	private final Option<Location> location;
	private final Observable<LocalTimeInterval> timeInterval;
	private final DayOfWeek weekDay;
	
	TimeTableEntryModel(TimeTableContext context, String name, Option<Location> location, LocalTimeInterval timeInterval, DayOfWeek weekDay) {
		this.context = context;
		this.name = name;
		this.location = location;
		this.timeInterval = new Observable<>(timeInterval);
		this.weekDay = weekDay;
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
	public boolean occursOn(LocalDate date) { return context.getDateInterval().get().contains(date) && date.getDayOfWeek().equals(weekDay); }
	
	@Override
	public boolean beginsOn(LocalDate date) { return occursOn(date); }
	
	@Override
	public boolean endsOn(LocalDate date) { return occursOn(date); }
	
	public DayOfWeek getWeekDay() { return weekDay; }
}
