package fwcd.timetable.model.calendar.task;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor;
import fwcd.timetable.model.calendar.CommonEntryType;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.model.calendar.recurrence.Recurrence;
import fwcd.timetable.model.calendar.recurrence.RecurrenceParser;

public class TaskModel implements CalendarEntryModel, Serializable {
	private static final long serialVersionUID = -1219052993628334319L;
	private String name;
	private int taskListId;
	private int calendarId;
	private String description;
	private Option<Location> location;
	private Option<LocalDateTime> dateTime;
	private String rawRecurrence;
	private Option<Recurrence> recurrence;
	
	/** Deserialization constructor. */
	protected TaskModel() {
		this("", 0, 0, "", Option.empty(), Option.empty(), "", Option.empty());
	}
	
	public TaskModel(
		String name,
		int taskListId,
		int calendarId,
		String description,
		Option<Location> location,
		Option<LocalDateTime> dateTime,
		String rawRecurrence,
		Option<Recurrence> recurrence
	) {
		this.name = name;
		this.taskListId = taskListId;
		this.calendarId = calendarId;
		this.description = description;
		this.location = location;
		this.dateTime = dateTime;
		this.rawRecurrence = rawRecurrence;
		this.recurrence = recurrence;
	}
	
	public Builder with() {
		return new Builder(name, taskListId, calendarId)
			.description(description)
			.location(location)
			.dateTime(dateTime)
			.recurrence(rawRecurrence)
			.recurrenceEnd(recurrence.flatMap(Recurrence::getEnd));
	}

	@Override
	public <T> T accept(CalendarEntryVisitor<T> visitor) { return visitor.visitTask(this); }
	
	@Override
	public String getName() { return name; }
	
	public int getTaskListId() { return taskListId; }
	
	@Override
	public int getCalendarId() { return calendarId; }
	
	public Option<LocalDateTime> getDateTime() { return dateTime; }
	
	public Option<Location> getLocation() { return location; }
	
	public Option<Recurrence> getRecurrence() { return recurrence; }
	
	public String getRawRecurrence() { return rawRecurrence; }
	
	public boolean occursOn(LocalDate date) {
		return dateTime.map(it -> it.toLocalDate().equals(date) || repeatsOn(date).orElse(false)).orElse(false);
	}
	
	private Option<Boolean> repeatsOn(LocalDate date) { return recurrence.map(it -> it.matches(date)); }

	@Override
	public String getDescription() { return description; }

	@Override
	public String getType() { return CommonEntryType.TASK; }
	
	public static class Builder {
		private String name;
		private int taskListId;
		private int calendarId;
		private String description;
		private Option<Location> location;
		private Option<LocalDateTime> dateTime;
		private String rawRecurrence = "";
		private Option<LocalDate> recurrenceEnd = Option.empty();
		
		private RecurrenceParser recurrenceParser = new RecurrenceParser();
		
		public Builder(String name, int taskListId, int calendarId) {
			this.name = name;
			this.taskListId = taskListId;
			this.calendarId = calendarId;
		}
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder taskListId(int taskListId) {
			this.taskListId = taskListId;
			return this;
		}
		
		public Builder calendarId(int calendarId) {
			this.calendarId = calendarId;
			return this;
		}
		
		public Builder description(String description) {
			this.description = description;
			return this;
		}
		
		public Builder location(Option<Location> location) {
			this.location = location;
			return this;
		}
		
		public Builder location(Location location) {
			return location(Option.of(location));
		}
		
		public Builder dateTime(Option<LocalDateTime> dateTime) {
			this.dateTime = dateTime;
			return this;
		}
		
		public Builder dateTime(LocalDateTime dateTime) {
			return dateTime(Option.of(dateTime));
		}
		
		public Builder recurrence(String rawRecurrence) {
			this.rawRecurrence = rawRecurrence;
			return this;
		}
		
		public Builder recurrenceEnd(Option<LocalDate> recurrenceEnd) {
			this.recurrenceEnd = recurrenceEnd;
			return this;
		}
		
		public Builder recurrenceEnd(LocalDate recurrenceEnd) {
			return recurrenceEnd(Option.of(recurrenceEnd));
		}
		
		public TaskModel build() {
			return new TaskModel(
				name,
				taskListId,
				calendarId,
				description,
				location,
				dateTime,
				rawRecurrence,
				dateTime.flatMap(dt -> recurrenceParser.parse(rawRecurrence, dt.toLocalDate(), recurrenceEnd, Collections.emptySet()))
			);
		}
	}
}
