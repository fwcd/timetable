package com.fwcd.timetable.model.calendar;

import java.time.Duration;
import java.time.LocalDateTime;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;

public class AppointmentModel {
	private final String type;
	private final String name;
	private final Option<Location> location;
	private final Observable<LocalDateTime> start;
	private final Observable<LocalDateTime> end;
	
	private AppointmentModel(String type, String name, Option<Location> location, LocalDateTime start, LocalDateTime end) {
		this.type = type;
		this.name = name;
		this.location = location;
		this.start = new Observable<>(start);
		this.end = new Observable<>(end);
	}
	
	public String getType() { return type; }
	
	public String getName() { return name; }
	
	public Option<Location> getLocation() { return location; }
	
	public Observable<LocalDateTime> getStart() { return start; }
	
	public Observable<LocalDateTime> getEnd() { return end; }
	
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
