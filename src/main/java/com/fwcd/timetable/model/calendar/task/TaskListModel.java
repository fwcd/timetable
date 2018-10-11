package com.fwcd.timetable.model.calendar.task;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.fructose.Observable;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.view.utils.SubscriptionStack;

public class TaskListModel {
	private final Observable<String> name;
	private final ObservableList<TaskModel> tasks = new ObservableList<>();
	
	private final EventListenerList<TaskListModel> changeListeners = new EventListenerList<>();
	private final SubscriptionStack taskSubscriptions = new SubscriptionStack();
	
	public TaskListModel(String name) {
		this.name = new Observable<>(name);
		setupChangeListeners();
	}
	
	private void setupChangeListeners() {
		name.listen(it -> changeListeners.fire(this));
		tasks.listenAndFire(it -> {
			changeListeners.fire(this);
			taskSubscriptions.unsubscribeAll();
			taskSubscriptions.subscribeAll(tasks, TaskModel::getChangeListeners, task -> changeListeners.fire(this));
		});
	}
	
	public EventListenerList<TaskListModel> getChangeListeners() { return changeListeners; }

	public Observable<String> getName() { return name; }
	
	public ObservableList<TaskModel> getTasks() { return tasks; }
}
