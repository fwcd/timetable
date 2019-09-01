package fwcd.timetable.view.sidebar.task;

import fwcd.fructose.Observable;
import fwcd.fructose.Option;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

public class TaskManagerViewModel {
	private final CalendarCrateViewModel crate;
	private final Observable<Option<Integer>> selectedTaskListId = new Observable<>(Option.empty());
	
	public TaskManagerViewModel(CalendarCrateViewModel crate) {
		this.crate = crate;
	}
	
	public CalendarCrateViewModel getCrate() { return crate; }
	
	public Observable<Option<Integer>> getSelectedTaskListId() { return selectedTaskListId; }
	
	public void select(int taskListId) { selectedTaskListId.set(Option.of(taskListId)); }
	
	public void deselect() { selectedTaskListId.set(Option.empty()); }
}
