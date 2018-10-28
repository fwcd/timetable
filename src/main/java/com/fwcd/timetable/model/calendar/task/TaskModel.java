package com.fwcd.timetable.model.calendar.task;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.calendar.CalendarEntryVisitor;
import com.fwcd.timetable.model.calendar.CommonEntryType;
import com.fwcd.timetable.model.json.PostDeserializable;

public class TaskModel implements CalendarEntryModel, Serializable, PostDeserializable {
	private static final long serialVersionUID = -1219052993628334319L;
	private final Observable<String> name;
	private final Observable<String> description = new Observable<>("");
	private final Observable<Option<LocalDateTime>> dueDateTime = new Observable<>(Option.empty());
	
	private transient EventListenerList<TaskModel> nullableChangeListeners;
	
	public TaskModel(String name) {
		this.name = new Observable<>(name);
		setupChangeListeners();
	}
	
	@Override
	public void postDeserialize() {
		setupChangeListeners();
	}
	
	private void setupChangeListeners() {
		name.listen(it -> getChangeListeners().fire(this));
		description.listen(it -> getChangeListeners().fire(this));
		dueDateTime.listen(it -> getChangeListeners().fire(this));
	}
	
	public EventListenerList<TaskModel> getChangeListeners() {
		if (nullableChangeListeners == null) {
			nullableChangeListeners = new EventListenerList<>();
		}
		return nullableChangeListeners;
	}

	@Override
	public void accept(CalendarEntryVisitor visitor) { visitor.visitTask(this); }
	
	public Observable<String> getName() { return name; }
	
	public Observable<Option<LocalDateTime>> getDueDateTime() { return dueDateTime; }
	
	@Override
	public Observable<String> getDescription() { return description; }

	@Override
	public String getType() { return CommonEntryType.TASK; }
}
