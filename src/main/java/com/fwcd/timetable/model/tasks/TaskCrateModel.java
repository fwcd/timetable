package com.fwcd.timetable.model.tasks;

import com.fwcd.fructose.structs.ObservableList;

public class TaskCrateModel {
	private final ObservableList<TaskModel> tasks = new ObservableList<>();
	
	public ObservableList<TaskModel> getTasks() { return tasks; }
}
