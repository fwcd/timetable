package com.fwcd.timetable.model.tasks;

import com.fwcd.fructose.structs.ObservableList;

public class TaskCrateModel {
	private final ObservableList<TaskListModel> lists = new ObservableList<>();
	
	public ObservableList<TaskListModel> getLists() { return lists; }
	
	public int size() { return lists.size(); }
}
