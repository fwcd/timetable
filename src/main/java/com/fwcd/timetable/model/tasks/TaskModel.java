package com.fwcd.timetable.model.tasks;

import com.fwcd.fructose.Observable;

public class TaskModel {
	private final Observable<String> name;
	
	public TaskModel(String name) {
		this.name = new Observable<>(name);
	}
	
	public Observable<String> getName() { return name; }
}
