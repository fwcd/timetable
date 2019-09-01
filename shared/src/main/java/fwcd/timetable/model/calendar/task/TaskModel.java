package fwcd.timetable.model.calendar.task;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarEntryVisitor;
import fwcd.timetable.model.calendar.CommonEntryType;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.model.calendar.recurrence.Recurrence;

public class TaskModel implements CalendarEntryModel, Serializable {
	private static final long serialVersionUID = -1219052993628334319L;
	private String name;
	private int taskListId;
	private int calendarId;
	private String description;
	private Option<Location> location;
	private Option<LocalDateTime> dateTime;
	private Option<Recurrence> recurrence;
	
	/** Deserialization constructor. */
	protected TaskModel() {
		this("", 0, 0, "", Option.empty(), Option.empty(), Option.empty());
	}
	
	public TaskModel(
		String name,
		int taskListId,
		int calendarId,
		String description,
		Option<Location> location,
		Option<LocalDateTime> dateTime,
		Option<Recurrence> recurrence
	) {
		this.name = name;
		this.taskListId = taskListId;
		this.calendarId = calendarId;
		this.description = description;
		this.location = location;
		this.dateTime = dateTime;
		this.recurrence = recurrence;
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
	
	public boolean occursOn(LocalDate date) {
		return dateTime.map(it -> it.toLocalDate().equals(date) || repeatsOn(date).orElse(false)).orElse(false);
	}
	
	private Option<Boolean> repeatsOn(LocalDate date) { return recurrence.map(it -> it.matches(date)); }

	@Override
	public String getDescription() { return description; }

	@Override
	public String getType() { return CommonEntryType.TASK; }
}
