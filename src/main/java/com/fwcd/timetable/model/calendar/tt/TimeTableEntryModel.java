package com.fwcd.timetable.model.calendar.tt;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.time.LocalTimeInterval;
import com.fwcd.timetable.model.calendar.CalendarEventModel;
import com.fwcd.timetable.model.calendar.CommonEntryType;
import com.fwcd.timetable.model.calendar.Location;

public class TimeTableEntryModel implements CalendarEventModel {
	private final TimeTableContext context;
	private final Observable<String> name;
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
	public String getType() { return CommonEntryType.TIME_TABLE_ENTRY; }

	@Override
	public boolean occursOn(LocalDate date) { return context.getDateInterval().get().contains(date) && date.getDayOfWeek().equals(weekDay); }
	
	@Override
	public boolean beginsOn(LocalDate date) { return occursOn(date); }
	
	@Override
	public boolean endsOn(LocalDate date) { return occursOn(date); }
	
	public DayOfWeek getWeekDay() { return weekDay; }
}
