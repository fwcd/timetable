package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.time.LocalDateTimeInterval;
import com.fwcd.fructose.time.LocalTimeInterval;
import com.fwcd.timetable.model.calendar.recurrence.ParsedRecurrence;

public class AppointmentModel implements CalendarEntryModel, Comparable<AppointmentModel> {
	private final Observable<String> name;
	private final Observable<Option<Location>> location;
	private final Observable<LocalDateTimeInterval> dateTimeInterval;
	private final Observable<String> description;
	private final Observable<Boolean> ignoreDate;
	private final Observable<Boolean> ignoreTime;
	private final ParsedRecurrence recurrence;
	private final Observable<Option<LocalDate>> recurrenceEnd;
	
	private AppointmentModel(
		String name,
		Option<Location> location,
		LocalDateTime startInclusive,
		LocalDateTime endExclusive,
		String description,
		boolean ignoreDate,
		boolean ignoreTime,
		String rawRecurrence,
		Option<LocalDate> recurrenceEnd
	) {
		this.name = new Observable<>(name);
		this.location = new Observable<>(location);
		dateTimeInterval = new Observable<>(new LocalDateTimeInterval(startInclusive, endExclusive));
		this.description = new Observable<>(description);
		this.ignoreDate = new Observable<>(ignoreDate);
		this.ignoreTime = new Observable<>(ignoreTime);
		this.recurrenceEnd = new Observable<>(recurrenceEnd);
		recurrence = new ParsedRecurrence(dateTimeInterval, this.recurrenceEnd);
		recurrence.getRaw().set(rawRecurrence);
	}
	
	@Override
	public void accept(CalendarEntryVisitor visitor) { visitor.visitAppointment(this); }
	
	@Override
	public String getType() { return CommonEntryType.APPOINTMENT; }
	
	@Override
	public Observable<String> getName() { return name; }
	
	public Observable<Option<Location>> getLocation() { return location; }
	
	public Observable<LocalDateTimeInterval> getDateTimeInterval() { return dateTimeInterval; }
	
	/** The inclusive start date */
	public LocalDateTime getStart() { return dateTimeInterval.get().getStart(); }
	
	/** The exclusive end date time*/
	public LocalDateTime getEnd() { return dateTimeInterval.get().getEnd(); }
	
	/** The inclusive start date */
	public LocalDate getStartDate() { return getStart().toLocalDate(); }
	
	/** The inclusive end date */
	public LocalDate getLastDate() { return getEnd().toLocalDate(); }
	
	/** The inclusive start time */
	public LocalTime getStartTime() { return getStart().toLocalTime(); }
	
	/** The exclusive end time */
	public LocalTime getEndTime() { return getEnd().toLocalTime(); }
	
	public ParsedRecurrence getRecurrence() { return recurrence; }
	
	public Observable<Option<LocalDate>> getRecurrenceEnd() { return recurrenceEnd; }
	
	@Override
	public Observable<String> getDescription() { return description; }
	
	public boolean occursOn(LocalDate date) { return ignoreDate.get() ? false : dateTimeInterval.get().contains(date.atStartOfDay()) || repeatsOn(date); }

	public boolean repeatsOn(LocalDate date) { return recurrence.getParsed().get().filter(it -> it.matches(date)).isPresent(); }
	
	@Override
	public int compareTo(AppointmentModel o) { return getStart().compareTo(o.getStart()); }
	
	public boolean overlaps(AppointmentModel other) { return (getStart().compareTo(other.getEnd()) <= 0) && (getEnd().compareTo(other.getStart()) <= 0); }
	
	public boolean beginsOn(LocalDate date) { return ignoreDate.get() ? false : date.equals(getStartDate()); }
	
	public boolean endsOn(LocalDate date) { return ignoreDate.get() ? false : date.equals(getLastDate()); }
	
	public Observable<Boolean> ignoresDate() { return ignoreDate; }
	
	public Observable<Boolean> ignoresTime() { return ignoreTime; }
	
	public LocalTimeInterval getTimeIntervalOn(LocalDate date) {
		if (occursOn(date)) {
			boolean repeats = repeatsOn(date);
			boolean begins = beginsOn(date);
			boolean ends = endsOn(date);
			boolean allDay = ignoreTime.get();
			
			if (!allDay) {
				if ((begins && ends) || repeats) {
					return new LocalTimeInterval(getStartTime(), getEndTime());
				} if (begins) {
					return new LocalTimeInterval(getStartTime(), LocalTime.MAX);
				} else if (ends) {
					return new LocalTimeInterval(LocalTime.MIN, getEndTime());
				}
			}
			
			// TODO: Find a better way to deal with all day events than all-day time intervals
			return new LocalTimeInterval(LocalTime.MIN, LocalTime.MAX);
		} else {
			throw new IllegalArgumentException("Calendar event does not occur on " + date);
		}
	}
	
	public static class Builder {
		private final String name;
		private Option<Location> location = Option.empty();
		private LocalDateTime start = LocalDateTime.now();
		private LocalDateTime end = LocalDateTime.now();
		private String description = "";
		private boolean ignoreDate = false;
		private boolean ignoreTime = false;
		private String rawRecurrence = "";
		private Option<LocalDate> recurrenceEnd = Option.empty();
		
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
		
		public Builder startDate(LocalDate start) {
			this.start = LocalDateTime.of(start, this.start.toLocalTime());
			return this;
		}
		
		public Builder lastDate(LocalDate end) {
			this.end = LocalDateTime.of(end, this.end.toLocalTime());
			return this;
		}
		
		public Builder startTime(LocalTime start) {
			this.start = LocalDateTime.of(this.start.toLocalDate(), start);
			return this;
		}
		
		public Builder endTime(LocalTime end) {
			this.end = LocalDateTime.of(this.end.toLocalDate(), end);
			return this;
		}
		
		public Builder ignoreDate(boolean ignoreDate) {
			this.ignoreDate = ignoreDate;
			return this;
		}
		
		public Builder ignoreTime(boolean ignoreTime) {
			this.ignoreTime = ignoreTime;
			return this;
		}
		
		public Builder description(String description) {
			this.description = description;
			return this;
		}
		
		public Builder recurrence(String rawRecurrence) {
			this.rawRecurrence = rawRecurrence;
			return this;
		}
		
		public Builder recurrenceEnd(LocalDate recurrenceEnd) {
			this.recurrenceEnd = Option.of(recurrenceEnd);
			return this;
		}
		
		public AppointmentModel build() {
			return new AppointmentModel(name, location, start, end, description, ignoreDate, ignoreTime, rawRecurrence, recurrenceEnd);
		}
	}
}
