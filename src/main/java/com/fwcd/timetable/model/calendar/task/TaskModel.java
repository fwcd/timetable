package com.fwcd.timetable.model.calendar.task;

import com.fwcd.fructose.Observable;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.calendar.CalendarEntryVisitor;
import com.fwcd.timetable.model.calendar.CommonEntryType;

public class TaskModel implements CalendarEntryModel {
	private final Observable<String> name;
	private final Observable<String> description = new Observable<>("");
	
	public TaskModel(String name) {
		this.name = new Observable<>(name);
	}
	
	@Override
	public void accept(CalendarEntryVisitor visitor) { visitor.visitTask(this); }
	
	public Observable<String> getName() { return name; }
	
	@Override
	public Observable<String> getDescription() { return description; }

	@Override
	public String getType() { return CommonEntryType.TASK; }
}
