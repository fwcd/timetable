package fwcd.timetable.model.calendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

import fwcd.fructose.Option;
import fwcd.fructose.time.LocalDateTimeInterval;
import fwcd.fructose.time.LocalTimeInterval;
import fwcd.timetable.model.calendar.recurrence.Recurrence;
import fwcd.timetable.model.calendar.recurrence.RecurrenceParser;

public class AppointmentModel implements Serializable, CalendarEntryModel, Comparable<AppointmentModel> {
	private static final long serialVersionUID = 6135909125862494477L;
	private String name;
	private int calendarId;
	private Option<Location> location;
	private LocalDateTimeInterval dateTimeInterval;
	private String description;
	private boolean ignoreDate;
	private boolean ignoreTime;
	private String rawRecurrence;
	private Option<Recurrence> recurrence;
	
	/** Deserialization constructor. */
	protected AppointmentModel() {
		this("", 0, Option.empty(), LocalDateTime.now(), LocalDateTime.now(), "", false, false, "", Option.empty());
	}
	
	private AppointmentModel(
		String name,
		int calendarId,
		Option<Location> location,
		LocalDateTime startInclusive,
		LocalDateTime endExclusive,
		String description,
		boolean ignoreDate,
		boolean ignoreTime,
		String rawRecurrence,
		Option<Recurrence> recurrence
	) {
		this.name = name;
		this.location = location;
		dateTimeInterval = new LocalDateTimeInterval(startInclusive, endExclusive);
		this.description = description;
		this.ignoreDate = ignoreDate;
		this.ignoreTime = ignoreTime;
		this.rawRecurrence = rawRecurrence;
		this.recurrence = recurrence;
	}
	
	@Override
	public <T> T accept(CalendarEntryVisitor<T> visitor) { return visitor.visitAppointment(this); }
	
	@Override
	public String getType() { return CommonEntryType.APPOINTMENT; }
	
	@Override
	public String getName() { return name; }
	
	@Override
	public int getCalendarId() { return calendarId; }
	
	public Option<Location> getLocation() { return location; }
	
	public LocalDateTimeInterval getDateTimeInterval() { return dateTimeInterval; }
	
	/** The inclusive start date */
	public LocalDateTime getStart() { return dateTimeInterval.getStart(); }
	
	/** The exclusive end date time*/
	public LocalDateTime getEnd() { return dateTimeInterval.getEnd(); }
	
	/** The inclusive start date */
	public LocalDate getStartDate() { return getStart().toLocalDate(); }
	
	/** The inclusive end date */
	public LocalDate getLastDate() { return getEnd().toLocalDate(); }
	
	/** The inclusive start time */
	public LocalTime getStartTime() { return getStart().toLocalTime(); }
	
	/** The exclusive end time */
	public LocalTime getEndTime() { return getEnd().toLocalTime(); }
	
	public Option<Recurrence> getRecurrence() { return recurrence; }
	
	public String getRawRecurrence() { return rawRecurrence; }
	
	@Override
	public String getDescription() { return description; }
	
	public boolean occursOn(LocalDate date) { return ignoreDate ? false : repeatsOn(date).orElseGet(() -> dateTimeInterval.toLocalDateInterval().contains(date)); }

	private Option<Boolean> repeatsOn(LocalDate date) { return recurrence.map(it -> it.matches(date)); }
	
	@Override
	public int compareTo(AppointmentModel o) { return getStart().compareTo(o.getStart()); }
	
	public boolean overlaps(AppointmentModel other) { return (getStart().compareTo(other.getEnd()) <= 0) && (getEnd().compareTo(other.getStart()) <= 0); }
	
	public boolean beginsOn(LocalDate date) { return ignoreDate ? false : date.equals(getStartDate()); }
	
	public boolean endsOn(LocalDate date) { return ignoreDate ? false : date.equals(getLastDate()); }
	
	public boolean ignoresDate() { return ignoreDate; }
	
	public boolean ignoresTime() { return ignoreTime; }
	
	/** Constructs a builder from this appointment to create a derived appoinment. */
	public Builder with() {
		return new Builder(name, calendarId)
			.location(location)
			.start(getStart())
			.end(getEnd())
			.excludes(recurrence.map(Recurrence::getExcludes).orElseGet(Collections::emptySet))
			.description(description)
			.ignoreDate(ignoreDate)
			.ignoreTime(ignoreTime)
			.recurrence(rawRecurrence)
			.recurrenceEnd(recurrence.flatMap(Recurrence::getEnd));
	}
	
	public LocalTimeInterval getTimeIntervalOn(LocalDate date) {
		if (occursOn(date)) {
			boolean repeats = repeatsOn(date).orElse(false);
			boolean begins = beginsOn(date);
			boolean ends = endsOn(date);
			boolean allDay = ignoreTime;
			
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
		private final int calendarId;
		private Option<Location> location = Option.empty();
		private LocalDateTime start = LocalDateTime.now();
		private LocalDateTime end = LocalDateTime.now();
		private Set<? extends LocalDate> excludes = Collections.emptySet();
		private String description = "";
		private boolean ignoreDate = false;
		private boolean ignoreTime = false;
		private String rawRecurrence = "";
		private Option<LocalDate> recurrenceEnd = Option.empty();
		
		private RecurrenceParser recurrenceParser = new RecurrenceParser();
		
		public Builder(String name, int calendarId) {
			this.name = name;
			this.calendarId = calendarId;
		}
		
		public Builder location(Location location) {
			return location(Option.of(location));
		}
		
		public Builder location(Option<Location> location) {
			this.location = location;
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

		public Builder excludes(Set<? extends LocalDate> excludes) {
			this.excludes = excludes;
			return this;
		}
		
		public Builder recurrenceEnd(LocalDate recurrenceEnd) {
			return recurrenceEnd(Option.of(recurrenceEnd));
		}
		
		public Builder recurrenceEnd(Option<LocalDate> recurrenceEnd) {
			this.recurrenceEnd = recurrenceEnd;
			return this;
		}
		
		public AppointmentModel build() {
			return new AppointmentModel(
				name,
				calendarId,
				location,
				start,
				end,
				description,
				ignoreDate,
				ignoreTime,
				rawRecurrence,
				recurrenceParser.parse(rawRecurrence, start.toLocalDate(), recurrenceEnd, excludes)
			);
		}
	}
}
