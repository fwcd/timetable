package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.time.LocalTimeInterval;

/**
 * A calendar event without a date/time interval.
 */
public class PlainEntryModel implements CalendarEventModel {
	private final Observable<LocalTimeInterval> emptyTimeInterval = new Observable<>(new LocalTimeInterval(LocalTime.MIN, LocalTime.MIN));
	private final Observable<String> type = new Observable<>(CommonEventType.PLAIN_ENTRY);
	private final Observable<String> name;
	
	public PlainEntryModel(String name) {
		this.name = new Observable<>(name);
	}
	
	@Override
	public Observable<String> getType() { return type; }

	@Override
	public Observable<String> getName() { return name; }

	@Override
	public Option<Location> getLocation() { return Option.empty(); }

	@Override
	public Observable<LocalTimeInterval> getTimeInterval() { return emptyTimeInterval; }

	@Override
	public boolean occursOn(LocalDate date) { return false; }

	@Override
	public boolean beginsOn(LocalDate date) { return false; }

	@Override
	public boolean endsOn(LocalDate date) { return false; }
}
