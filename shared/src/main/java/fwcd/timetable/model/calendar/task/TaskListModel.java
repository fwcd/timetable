package fwcd.timetable.model.calendar.task;

import java.io.Serializable;

/**
 * Metadata information about a task list. The entries
 * are stored in a calendar crate and refer to a task
 * list by its ID.
 */
public class TaskListModel implements Serializable {
	private static final long serialVersionUID = 3478629580505983160L;
	private String name;
	private int calendarId;
	
	/** Deserialization constructor. */
	protected TaskListModel() {}
	
	public TaskListModel(String name, int calendarId) {
		this.name = name;
		this.calendarId = calendarId;
	}
	
	public String getName() { return name; }
	
	public int getCalendarId() { return calendarId; }
}
