package fwcd.timetable.model.calendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import fwcd.timetable.model.calendar.task.TaskListModel;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.model.utils.IdGenerator;

/**
 * A collection of calendars together with their
 * entries (such as appointments). Usually the
 * "root" object of a serialized JSON file.
 */
public class CalendarCrateModel implements Serializable {
	private static final long serialVersionUID = -1914115703122727566L;
	private final IdGenerator idGenerator = new IdGenerator();
	
	/** Contains the mappings from IDs to calendar metadata. */
	private final Map<Integer, CalendarModel> calendars = new LinkedHashMap<>();
	/** Contains the mappings from IDs to task list metadata. */
	private final Map<Integer, TaskListModel> taskLists = new LinkedHashMap<>();
	/** Stores all entries in this calendar. */
	private final List<CalendarEntryModel> entries = new ArrayList<>();
	
	public CalendarModel getCalendarById(int id) { return calendars.get(id); }
	
	public TaskListModel getTaskListById(int id) { return taskLists.get(id); }
	
	/**
	 * Removes a calendar and all of its entries.
	 */
	public void removeCalendarById(int id) {
		calendars.remove(id);
		entries.removeIf(it -> it.getCalendarId() == id);
	}
	
	/**
	 * Removes a task list and all of its entries.
	 */
	public void removeTaskListById(int id) {
		taskLists.remove(id);
		entries.removeIf(it -> it.accept(new CalendarEntryVisitor<Boolean>() {
			@Override
			public Boolean visitCalendarEntry(CalendarEntryModel entry) { return false; }
			
			@Override
			public Boolean visitTask(TaskModel task) { return task.getTaskListId() == id; }
		}));
	}
	
	public Stream<CalendarEntryModel> streamEntries() { return entries.stream(); }
	
	/** Returns a read-only list of the entries. */
	public List<CalendarEntryModel> getEntries() { return Collections.unmodifiableList(entries); }
	
	/** Returns a read-only view of the calendars. */
	public Collection<CalendarModel> getCalendars() { return Collections.unmodifiableCollection(calendars.values()); }
	
	/** Returns a read-only view of the task lists. */
	public Collection<TaskListModel> getTaskLists() { return Collections.unmodifiableCollection(taskLists.values()); }
	
	/**
	 * Adds a calendar to this crate.
	 * 
	 * @param calendar - The calendar to be added
	 * @return The ID of the new calendar
	 */
	public int add(CalendarModel calendar) {
		int id = idGenerator.next();
		calendars.put(id, calendar);
		return id;
	}
	
	/**
	 * Adds a task list to this crate.
	 * 
	 * @param taskList - The task list to be added
	 * @return The ID of the new task list
	 */
	public int add(TaskListModel taskList) {
		int id = idGenerator.next();
		taskLists.put(id, taskList);
		return id;
	}
	
	/**
	 * Removes all calendars and adds an empty
	 * default calendar named "Calendar".
	 */
	public void createDefaultCalendars() {
		calendars.clear();
		add(new CalendarModel("Calendar"));
	}
}
