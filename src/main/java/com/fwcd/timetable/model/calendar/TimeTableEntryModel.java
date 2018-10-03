package com.fwcd.timetable.model.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.time.LocalTimeInterval;

public class TimeTableEntryModel implements CalendarEventModel {
	private final TimeTableContext context;
	private final Observable<String> name;
	private final Observable<String> type = new Observable<>(CommonEventType.TIME_TABLE_ENTRY);
	private final Observable<Option<Location>> location;
	private final Observable<LocalTimeInterval> timeInterval;
	private final DayOfWeek weekDay;
	
	TimeTableEntryModel(TimeTableContext context, String name, Option<Location> location, LocalTimeInterval timeInterval, DayOfWeek weekDay) {
		this.context = context;
		this.name = new Observable<>(name);
		this.location = new Observable<>(location);
		this.timeInterval = new Observable<>(timeInterval);
		this.weekDay = weekDay;
	}
	
	@Override
	public Observable<String> getName() { return name; }
	
	@Override
	public Observable<Option<Location>> getLocation() { return location; }
	
	@Override
	public Observable<LocalTimeInterval> getTimeInterval() { return timeInterval; }
	
	@Override
	public Observable<String> getType() { return type; }

	@Override
	public boolean occursOn(LocalDate date) { return context.getDateInterval().get().contains(date) && date.getDayOfWeek().equals(weekDay); }
	
	@Override
	public boolean beginsOn(LocalDate date) { return occursOn(date); }
	
	@Override
	public boolean endsOn(LocalDate date) { return occursOn(date); }
	
	public DayOfWeek getWeekDay() { return weekDay; }
}
