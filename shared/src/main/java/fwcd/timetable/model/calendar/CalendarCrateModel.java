package fwcd.timetable.model.calendar;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fwcd.timetable.model.calendar.task.TaskListModel;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.model.utils.IdGenerator;
import fwcd.timetable.model.utils.Identified;

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
	private final Set<CalendarEntryModel> entries = new HashSet<>();
	
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
	
	/** @return a read-only collection of the entries. */
	public Collection<CalendarEntryModel> getEntries() { return Collections.unmodifiableCollection(entries); }
	
	/** @return a read-only view of the calendars. */
	public Collection<Identified<CalendarModel>> getCalendars() {
		return calendars.keySet()
			.stream()
			.map(id -> new Identified<>(getCalendarById(id), id))
			.collect(Collectors.toList());
	}
	
	/** @return a read-only view of the task lists. */
	public Collection<Identified<TaskListModel>> getTaskLists() {
		return taskLists.keySet()
			.stream()
			.map(id -> new Identified<>(getTaskListById(id), id))
			.collect(Collectors.toList());
	}
	
	/** @return a read-only view of the calendar ids. */
	public Set<Integer> getCalendarIds() { return Collections.unmodifiableSet(calendars.keySet()); }
	
	/** @return a read-only view of the task list ids. */
	public Set<Integer> getTaskListIds() { return Collections.unmodifiableSet(taskLists.keySet()); }
	
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
	
	/** Adds an entry to the crate. */
	public void add(CalendarEntryModel entry) {
		validate(entry);
		entries.add(entry);
	}
	
	/** Removes an entry from the crate. */
	public void remove(CalendarEntryModel entry) { entries.remove(entry); }
	
	/** Replaces an entry. */
	public void replace(CalendarEntryModel oldEntry, CalendarEntryModel newEntry) {
		entries.remove(oldEntry);
		entries.add(newEntry);
	}
	
	/** Tests whether the IDs referenced by the entry are valid. */
	private void validate(CalendarEntryModel entry) {
		if (entry.getCalendarId() == IdGenerator.MISSING_ID) {
			throw new IllegalArgumentException("The calendar entry " + entry + " is missing a calendar ID");
		}
		entry.accept(new CalendarEntryVisitor<Void>() {
			@Override
			public Void visitCalendarEntry(CalendarEntryModel entry) {
				if (!calendars.containsKey(entry.getCalendarId())) {
					throw new IllegalArgumentException("The calendar entry " + entry + " references the non-existing calendar id " + entry.getCalendarId() + ". Existing calendar ids: " + calendars.keySet());
				}
				return null;
			}

			@Override
			public Void visitTask(TaskModel task) {
				CalendarEntryVisitor.super.visitTask(task);
				if (!taskLists.containsKey(task.getTaskListId())) {
					throw new IllegalArgumentException("The task " + task + " references the non-existing task list id " + task.getTaskListId() + ". Existing task list ids: " + taskLists.keySet());
				}
				if (task.getCalendarId() != getTaskListById(task.getTaskListId()).getCalendarId()) {
					throw new IllegalArgumentException("The task " + task + " references a different calendar id than its task list");
				}
				return null;
			}
		});
	}
	
	/** Replaces an existing calendar by id. */
	public void setCalendarById(int id, CalendarModel calendar) {
		if (!calendars.containsKey(id)) {
			throw new IllegalArgumentException("Calendar with id " + id + " does not exist!");
		}
		calendars.put(id, calendar);
	}
	
	/** Replaces an existing task list by id. */
	public void setTaskListById(int id, TaskListModel taskList) {
		if (!taskLists.containsKey(id)) {
			throw new IllegalArgumentException("Task list with id " + id + " does not exist!");
		}
		taskLists.put(id, taskList);
	}
	
	/**
	 * Removes all calendars and adds an empty
	 * default calendar named "Calendar".
	 */
	public int resetToDefaultCalendar() {
		calendars.clear();
		return add(new CalendarModel("Calendar"));
	}
}
