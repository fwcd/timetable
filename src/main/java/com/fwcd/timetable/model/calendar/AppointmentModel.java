package com.fwcd.timetable.model.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.time.LocalDateInterval;
import com.fwcd.fructose.time.LocalTimeInterval;

public class AppointmentModel implements CalendarEntryModel, Comparable<AppointmentModel> {
	private final Observable<String> name;
	private final Observable<Option<Location>> location;
	private final Observable<LocalDateInterval> dateInterval;
	private final Observable<LocalTimeInterval> timeInterval;
	private final Observable<String> description;
	private final Observable<Boolean> ignoreDate;
	private final Observable<Boolean> ignoreTime;
	private final ParsedRecurrence recurrence;
	
	private AppointmentModel(
		String name,
		Option<Location> location,
		LocalDateTime startInclusive,
		LocalDateTime endExclusive,
		String description,
		boolean ignoreDate,
		boolean ignoreTime,
		String rawRecurrence
	) {
		this.name = new Observable<>(name);
		this.location = new Observable<>(location);
		dateInterval = new Observable<>(new LocalDateInterval(startInclusive.toLocalDate(), endExclusive.toLocalDate().plusDays(1)));
		timeInterval = new Observable<>(new LocalTimeInterval(startInclusive.toLocalTime(), endExclusive.toLocalTime()));
		this.description = new Observable<>(description);
		this.ignoreDate = new Observable<>(ignoreDate);
		this.ignoreTime = new Observable<>(ignoreTime);
		recurrence = new ParsedRecurrence(dateInterval);
		recurrence.getRaw().set(rawRecurrence);
	}
	
	@Override
	public void accept(CalendarEntryVisitor visitor) { visitor.visitAppointment(this); }
	
	@Override
	public String getType() { return CommonEntryType.APPOINTMENT; }
	
	@Override
	public Observable<String> getName() { return name; }
	
	public Observable<Option<Location>> getLocation() { return location; }
	
	public LocalDateTime getStart() { return LocalDateTime.of(dateInterval.get().getStart(), timeInterval.get().getStart()); }
	
	public LocalDateTime getEnd() { return LocalDateTime.of(dateInterval.get().getEnd(), timeInterval.get().getEnd()); }
	
	public ParsedRecurrence getRecurrence() { return recurrence; }
	
	@Override
	public Observable<String> getDescription() { return description; }
	
	/**
	 * Fetches the time interval of this calendar
	 * event.
	 * 
	 * <p>With single-day events, this interval
	 * is guaranteed to reflect the actual event time span,
	 * otherwise the interval will only consist
	 * of the start time on the first day and the
	 * end time on the last day.
	 * 
	 * <p><b>Careful attention is thus required
	 * when calling {@code merge}/{@code overlaps}
	 * on the obtained interval of a multi-day-event!
	 * In this case {@code getTimeIntervalOn} should be preferred.</b></p>
	 */
	public Observable<LocalTimeInterval> getTimeInterval() { return timeInterval; }
	
	public Observable<LocalDateInterval> getDateInterval() { return dateInterval; }
	
	public boolean occursOn(LocalDate date) { return ignoreDate.get() ? false : dateInterval.get().contains(date); }
	
	@Override
	public int compareTo(AppointmentModel o) { return getStart().compareTo(o.getStart()); }
	
	public boolean overlaps(AppointmentModel other) { return (getStart().compareTo(other.getEnd()) <= 0) && (getEnd().compareTo(other.getStart()) <= 0); }
	
	public boolean beginsOn(LocalDate date) { return ignoreDate.get() ? false : date.equals(dateInterval.get().getStart()); }
	
	public boolean endsOn(LocalDate date) { return ignoreDate.get() ? false : date.equals(dateInterval.get().getLastDate()); }
	
	public Observable<Boolean> ignoresDate() { return ignoreDate; }
	
	public Observable<Boolean> ignoresTime() { return ignoreTime; }
	
	public LocalTimeInterval getTimeIntervalOn(LocalDate date) {
		if (occursOn(date)) {
			boolean begins = beginsOn(date);
			boolean ends = endsOn(date);
			if (begins && ends) {
				LocalTimeInterval interval = getTimeInterval().get();
				return new LocalTimeInterval(interval.getStart(), interval.getEnd());
			} if (begins) {
				return new LocalTimeInterval(getTimeInterval().get().getStart(), LocalTime.MAX);
			} else if (ends) {
				return new LocalTimeInterval(LocalTime.MIN, getTimeInterval().get().getEnd());
			} else {
				return new LocalTimeInterval(LocalTime.MIN, LocalTime.MAX);
			}
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
		
		public AppointmentModel build() {
			return new AppointmentModel(name, location, start, end, description, ignoreDate, ignoreTime, rawRecurrence);
		}
	}
}
