package fwcd.timetable.viewmodel.calendar.task;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.viewmodel.Responder;

public class TaskViewModel implements Responder {
	private final TaskModel model;
	
	private final EventListenerList<TaskViewModel> changeListeners = new EventListenerList<>();
	private Option<Responder> nextResponder = Option.empty();
	
	public TaskViewModel(TaskModel model) {
		this.model = model;
	}
	
	@Override
	public void setNextResponder(Option<Responder> responder) {
		nextResponder = responder;
	}
	
	@Override
	public void fire() {
		changeListeners.fire(this);
	}
	
	/**
	 * Returns the underlying (immutable) model.
	 */
	public TaskModel getModel() { return model; }
	
	public EventListenerList<TaskViewModel> getChangeListeners() { return changeListeners; }
}
