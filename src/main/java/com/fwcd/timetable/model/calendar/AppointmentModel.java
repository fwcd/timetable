package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.time.LocalDateInterval;
import com.fwcd.fructose.time.LocalTimeInterval;

public class AppointmentModel implements CalendarEventModel, Comparable<AppointmentModel> {
	private final Observable<String> name;
	private final Option<Location> location;
	private final Observable<LocalDateInterval> dateInterval;
	private final Observable<LocalTimeInterval> timeInterval;
	
	private AppointmentModel(String name, Option<Location> location, LocalDateTime startInclusive, LocalDateTime endExclusive) {
		this.name = new Observable<>(name);
		this.location = location;
		dateInterval = new Observable<>(new LocalDateInterval(startInclusive.toLocalDate(), endExclusive.toLocalDate().plusDays(1)));
		timeInterval = new Observable<>(new LocalTimeInterval(startInclusive.toLocalTime(), endExclusive.toLocalTime()));
	}
	
	@Override
	public String getType() { return CommonEventType.APPOINTMENT; }
	
	@Override
	public Observable<String> getName() { return name; }
	
	@Override
	public Option<Location> getLocation() { return location; }
	
	public LocalDateTime getStart() { return LocalDateTime.of(dateInterval.get().getStart(), timeInterval.get().getStart()); }
	
	public LocalDateTime getEnd() { return LocalDateTime.of(dateInterval.get().getEnd(), timeInterval.get().getEnd()); }
	
	@Override
	public Observable<LocalTimeInterval> getTimeInterval() { return timeInterval; }
	
	public Observable<LocalDateInterval> getDateInterval() { return dateInterval; }
	
	@Override
	public boolean occursOn(LocalDate date) { return dateInterval.get().contains(date); }
	
	@Override
	public int compareTo(AppointmentModel o) { return getStart().compareTo(o.getStart()); }
	
	public boolean overlaps(AppointmentModel other) { return (getStart().compareTo(other.getEnd()) <= 0) && (getEnd().compareTo(other.getStart()) <= 0); }
	
	@Override
	public boolean beginsOn(LocalDate date) { return date.equals(dateInterval.get().getStart()); }
	
	@Override
	public boolean endsOn(LocalDate date) { return date.equals(dateInterval.get().getLastDate()); }
	
	public static class Builder {
		private final String name;
		private Option<Location> location = Option.empty();
		private LocalDateTime start = LocalDateTime.now();
		private LocalDateTime end = LocalDateTime.now();
		
		public Builder(String name) {
			this.name = name;
		}
		
		public Builder location(Location location) {
			this.location = Option.of(location);
			return this;
		}
		
		public Builder start(LocalDateTime start) {
			this.start = start;
			return this;
		}
		
		public Builder end(LocalDateTime end) {
			this.end = end;
			return this;
		}
		
		public AppointmentModel build() {
			return new AppointmentModel(name, location, start, end);
		}
	}
}
