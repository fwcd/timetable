package com.fwcd.timetable.view.sidebar.tasks;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.tasks.TaskCrateModel;
import com.fwcd.timetable.model.tasks.TaskListModel;

public class TaskCrateViewModel {
	private final TaskCrateModel model;
	private final Observable<Option<TaskListModel>> selectedList = new Observable<>(Option.empty());
	
	public TaskCrateViewModel(TaskCrateModel model) {
		this.model = model;
	}
	
	public TaskCrateModel getModel() { return model; }
	
	public Observable<Option<TaskListModel>> getSelectedList() { return selectedList; }
	
	public void select(TaskListModel list) { selectedList.set(Option.of(list)); }
	
	public void deselect() { selectedList.set(Option.empty()); }
}
