package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;

public class AppointmentModel implements CalendarEventModel {
	private final String type;
	private final String name;
	private final Option<Location> location;
	private final Observable<LocalTime> startTime;
	private final Observable<LocalTime> endTime;
	private final Observable<LocalDate> startDate;
	private final Observable<LocalDate> endDate;
	
	private AppointmentModel(String type, String name, Option<Location> location, LocalDateTime start, LocalDateTime end) {
		this.type = type;
		this.name = name;
		this.location = location;
		startTime = new Observable<>(start.toLocalTime());
		endTime = new Observable<>(end.toLocalTime());
		startDate = new Observable<>(start.toLocalDate());
		endDate = new Observable<>(end.toLocalDate());
	}
	
	@Override
	public String getType() { return type; }
	
	@Override
	public String getName() { return name; }
	
	public Option<Location> getLocation() { return location; }
	
	public LocalDateTime getStart() { return LocalDateTime.of(startDate.get(), startTime.get()); }
	
	public LocalDateTime getEnd() { return LocalDateTime.of(endDate.get(), endTime.get()); }
	
	public Observable<LocalDate> getStartDate() { return startDate; }
	
	public Observable<LocalDate> getEndDate() { return endDate; }
	
	@Override
	public Observable<LocalTime> getStartTime() { return startTime; }
	
	@Override
	public Observable<LocalTime> getEndTime() { return endTime; }
	
	@Override
	public boolean occursOn(LocalDate date) {
		return (date.compareTo(startDate.get()) >= 0)
			&& (date.compareTo(endDate.get()) <= 0);
	}
	
	public static class Builder {
		private final String name;
		private String type = "Appointment";
		private Option<Location> location = Option.empty();
		private LocalDateTime start = LocalDateTime.now();
		private LocalDateTime end = LocalDateTime.now();
		
		public Builder(String name) {
			this.name = name;
		}
		
		public Builder type(String type) {
			this.type = type;
			return this;
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
			return new AppointmentModel(type, name, location, start, end);
		}
	}
}
