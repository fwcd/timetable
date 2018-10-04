package com.fwcd.timetable.model.calendar.task;

import com.fwcd.fructose.Observable;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.calendar.CommonEntryType;

public class TaskModel implements CalendarEntryModel {
	private final Observable<String> name;
	
	public TaskModel(String name) {
		this.name = new Observable<>(name);
	}
	
	public Observable<String> getName() { return name; }

	@Override
	public String getType() { return CommonEntryType.TASK; }
}
