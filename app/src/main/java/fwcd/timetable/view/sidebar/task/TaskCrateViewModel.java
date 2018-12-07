package fwcd.timetable.view.sidebar.task;

import fwcd.fructose.Observable;
import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.task.TaskCrateModel;
import fwcd.timetable.model.calendar.task.TaskListModel;

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
