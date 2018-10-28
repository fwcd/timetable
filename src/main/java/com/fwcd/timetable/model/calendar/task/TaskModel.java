package com.fwcd.timetable.model.calendar.task;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.calendar.CalendarEntryVisitor;
import com.fwcd.timetable.model.calendar.CommonEntryType;
import com.fwcd.timetable.model.calendar.Location;
import com.fwcd.timetable.model.calendar.recurrence.ParsedRecurrence;
import com.fwcd.timetable.model.json.PostDeserializable;
import com.fwcd.timetable.model.utils.ObservableUtils;

public class TaskModel implements CalendarEntryModel, Serializable, PostDeserializable {
	private static final long serialVersionUID = -1219052993628334319L;
	private final Observable<String> name;
	private final Observable<String> description = new Observable<>("");
	private final Observable<Option<Location>> location = new Observable<>(Option.empty());
	private final Observable<Option<LocalDateTime>> dateTime = new Observable<>(Option.empty());
	private final ParsedRecurrence recurrence = new ParsedRecurrence();
	private final Observable<Option<LocalDate>> recurrenceEnd = new Observable<>(Option.empty());

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
		location.listen(it -> getChangeListeners().fire(this));
		description.listen(it -> getChangeListeners().fire(this));
		dateTime.listen(it -> getChangeListeners().fire(this));
		recurrenceEnd.listen(it -> getChangeListeners().fire(this));
		recurrence.getParsed().listen(it -> getChangeListeners().fire(this));
		
		ObservableUtils.triListen(dateTime, recurrenceEnd, recurrence.getRaw(), (dt, end, raw) -> {
			recurrence.update(dt.map(LocalDateTime::toLocalDate), end, Collections.emptySet());
		});
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
	
	public Observable<Option<LocalDateTime>> getDateTime() { return dateTime; }
	
	public Observable<Option<Location>> getLocation() { return location; }
	
	public ParsedRecurrence getRecurrence() { return recurrence; }
	
	public Observable<Option<LocalDate>> getRecurrenceEnd() { return recurrenceEnd; }
	
	public boolean occursOn(LocalDate date) {
		return dateTime.get().map(it -> it.toLocalDate().equals(date) || repeatsOn(date).orElse(false)).orElse(false);
	}
	
	private Option<Boolean> repeatsOn(LocalDate date) { return recurrence.getParsed().get().map(it -> it.matches(date)); }

	@Override
	public Observable<String> getDescription() { return description; }

	@Override
	public String getType() { return CommonEntryType.TASK; }
}
