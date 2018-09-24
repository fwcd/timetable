package com.fwcd.timetable.model.calendar;

import java.time.LocalDateTime;

import com.fwcd.fructose.Option;

public class CalendarEvent {
	private final String type;
	private final String name;
	private final Option<Location> location;
	private final LocalDateTime start;
	private final LocalDateTime end;
	
	private CalendarEvent(String type, String name, Option<Location> location, LocalDateTime start, LocalDateTime end) {
		this.type = type;
		this.name = name;
		this.location = location;
		this.start = start;
		this.end = end;
	}
	
	public String getType() { return type; }
	
	public String getName() { return name; }
	
	public Option<Location> getLocation() { return location; }
	
	public LocalDateTime getStart() { return start; }
	
	public LocalDateTime getEnd() { return end; }
	
	public static class Builder {
		private final String name;
		private String type = "Event";
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
		
		public CalendarEvent build() {
			return new CalendarEvent(type, name, location, start, end);
		}
	}
}
