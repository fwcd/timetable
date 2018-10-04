package com.fwcd.timetable.model.calendar.task;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.structs.ObservableList;

public class TaskListModel {
	private final Observable<String> name;
	private final ObservableList<TaskModel> tasks = new ObservableList<>();
	
	public TaskListModel(String name) {
		this.name = new Observable<>(name);
	}
	
	public Observable<String> getName() { return name; }
	
	public ObservableList<TaskModel> getTasks() { return tasks; }
}
