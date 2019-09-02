package fwcd.timetable.viewmodel.calendar;

import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.google.gson.Gson;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.ListenerList;
import fwcd.fructose.Pair;
import fwcd.timetable.model.calendar.CalendarCrateModel;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.model.calendar.CalendarSerializationUtils;
import fwcd.timetable.model.calendar.task.TaskListModel;
import fwcd.timetable.model.utils.Identified;

/**
 * Holds a calendar crate (model) and listener lists
 * for various parts of the crate. Manages
 * interactions with the crate.
 */
public class CalendarCrateViewModel {
	private static final Gson GSON = CalendarSerializationUtils.newGson();
	private CalendarCrateModel crate = new CalendarCrateModel();
	private final Set<Integer> selectedCalendarIds = new HashSet<>();

	private final EventListenerList<Collection<Identified<CalendarModel>>> calendarListeners = new EventListenerList<>();
	private final EventListenerList<Collection<Identified<TaskListModel>>> taskListListeners = new EventListenerList<>();
	private final EventListenerList<Collection<CalendarEntryModel>> entryListeners = new EventListenerList<>();
	private final EventListenerList<Pair<CalendarEntryModel, CalendarEntryModel>> replaceListeners = new EventListenerList<>();
	private final ListenerList changeListeners = new ListenerList();
	
	public EventListenerList<Collection<Identified<CalendarModel>>> getCalendarListeners() { return calendarListeners; }
	
	public EventListenerList<Collection<Identified<TaskListModel>>> getTaskListListeners() { return taskListListeners; }

	public EventListenerList<Collection<CalendarEntryModel>> getEntryListeners() { return entryListeners; }
	
	public EventListenerList<Pair<CalendarEntryModel, CalendarEntryModel>> getReplaceListeners() { return replaceListeners; }
	
	public ListenerList getChangeListeners() { return changeListeners; }
	
	private void fireCalendarListeners() { calendarListeners.fire(crate.getCalendars()); }
	
	private void fireTaskListListeners() { taskListListeners.fire(crate.getTaskLists()); }
	
	private void fireEntryListeners() { entryListeners.fire(crate.getEntries()); }
	
	private void fireReplaceListeners(CalendarEntryModel oldEntry, CalendarEntryModel newEntry) { replaceListeners.fire(new Pair<>(oldEntry, newEntry)); }
	
	private void fireChangeListeners() { changeListeners.fire(); }
	
	public CalendarModel getCalendarById(int id) { return crate.getCalendarById(id); }
	
	public TaskListModel getTaskListById(int id) { return crate.getTaskListById(id); }
	
	public Stream<CalendarEntryModel> streamEntries() { return crate.streamEntries(); }
	
	/** @return a read-only view of the entries. */
	public Collection<CalendarEntryModel> getEntries() { return crate.getEntries(); }
	
	/** @return a read-only view of the calendars. */
	public Collection<Identified<CalendarModel>> getCalendars() { return crate.getCalendars(); }
	
	/** @return a read-only view of the task lists. */
	public Collection<Identified<TaskListModel>> getTaskLists() { return crate.getTaskLists(); }
	
	/** @return a read-only view of the calendar ids. */
	public Set<Integer> getCalendarIds() { return crate.getCalendarIds(); }
	
	/** @return a read-only view of the task list ids. */
	public Set<Integer> getTaskListIds() { return crate.getTaskListIds(); }
	
	/** @return a read-only view of the selected calendar ids. */
	public Set<Integer> getSelectedCalendarIds() { return Collections.unmodifiableSet(selectedCalendarIds); }
	
	/**
	 * Removes all calendars and creates a default one.
	 * 
	 * @return the ID of the new default calendar
	 */
	public int resetToDefaultCalendar() {
		int id = crate.resetToDefaultCalendar();
		fireCalendarListeners();
		fireChangeListeners();
		fireEntryListeners();
		return id;
	}
	
	public void selectCalendar(int calendarId) {
		selectedCalendarIds.add(calendarId);
		fireCalendarListeners();
		fireChangeListeners();
	}
	
	public void deselectCalendar(int calendarId) {
		selectedCalendarIds.remove(calendarId);
		fireCalendarListeners();
		fireChangeListeners();
	}
	
	public int add(CalendarModel calendar) {
		int id = crate.add(calendar);
		fireCalendarListeners();
		fireChangeListeners();
		return id;
	}
	
	public int add(TaskListModel taskList) {
		int id = crate.add(taskList);
		fireTaskListListeners();
		fireChangeListeners();
		return id;
	}
	
	public void add(CalendarEntryModel entry) {
		crate.add(entry);
		fireEntryListeners();
		fireChangeListeners();
	}
	
	public void remove(CalendarEntryModel entry) {
		crate.remove(entry);
		fireEntryListeners();
		fireChangeListeners();
	}
	
	public void replace(CalendarEntryModel oldEntry, CalendarEntryModel newEntry) {
		crate.replace(oldEntry, newEntry);
		fireReplaceListeners(oldEntry, newEntry);
		fireEntryListeners();
		fireChangeListeners();
	}
	
	public void setCalendarById(int id, CalendarModel newCalendar) {
		crate.setCalendarById(id, newCalendar);
		fireCalendarListeners();
		fireChangeListeners();
	}
	
	public void setTaskListById(int id, TaskListModel newTaskList) {
		crate.setTaskListById(id, newTaskList);
		fireTaskListListeners();
		fireChangeListeners();
	}
	
	public void removeCalendarById(int id) {
		crate.removeCalendarById(id);
		selectedCalendarIds.remove(id);
		fireCalendarListeners();
		fireChangeListeners();
	}
	
	public void removeTaskListById(int id) {
		crate.removeTaskListById(id);
		fireCalendarListeners();
	}

	public void saveCrate(Appendable writer) {
		GSON.toJson(crate, writer);
	}
	
	public void loadCrate(Reader reader) {
		crate = GSON.fromJson(reader, CalendarCrateModel.class);
		fireCalendarListeners();
		fireTaskListListeners();
		fireEntryListeners();
		fireChangeListeners();
	}
}
