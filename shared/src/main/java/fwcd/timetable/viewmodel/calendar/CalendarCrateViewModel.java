package fwcd.timetable.viewmodel.calendar;

import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

import com.google.gson.Gson;

import fwcd.fructose.EventListenerList;
import fwcd.timetable.model.calendar.CalendarCrateModel;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.model.calendar.CalendarSerializationUtils;
import fwcd.timetable.model.calendar.task.TaskListModel;

/**
 * Holds a calendar crate (model) and listener lists
 * for various parts of the crate. Manages
 * interactions with the crate.
 */
public class CalendarCrateViewModel {
	private static final Gson GSON = CalendarSerializationUtils.newGson();
	private CalendarCrateModel crate = new CalendarCrateModel();

	private final EventListenerList<Collection<CalendarModel>> calendarListeners = new EventListenerList<>();
	private final EventListenerList<Collection<TaskListModel>> taskListListeners = new EventListenerList<>();
	private final EventListenerList<Collection<CalendarEntryModel>> entryListeners = new EventListenerList<>();
	
	public EventListenerList<Collection<CalendarModel>> getCalendarListeners() { return calendarListeners; }
	
	public EventListenerList<Collection<TaskListModel>> getTaskListListeners() { return taskListListeners; }

	public EventListenerList<Collection<CalendarEntryModel>> getEntryListeners() { return entryListeners; }
	
	private void fireCalendarListeners() { calendarListeners.fire(crate.getCalendars()); }
	
	private void fireTaskListListeners() { taskListListeners.fire(crate.getTaskLists()); }
	
	private void fireEntryListeners() { entryListeners.fire(crate.getEntries()); }
	
	public CalendarModel getCalendarById(int id) { return crate.getCalendarById(id); }
	
	public TaskListModel getTaskListById(int id) { return crate.getTaskListById(id); }
	
	public Stream<CalendarEntryModel> streamEntries() { return crate.streamEntries(); }

	public int add(CalendarModel calendar) {
		int id = crate.add(calendar);
		fireCalendarListeners();
		return id;
	}
	
	public int add(TaskListModel taskList) {
		int id = crate.add(taskList);
		fireTaskListListeners();
		return id;
	}
	
	public void removeCalendarById(int id) {
		crate.removeCalendarById(id);
		fireCalendarListeners();
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
	}
}
