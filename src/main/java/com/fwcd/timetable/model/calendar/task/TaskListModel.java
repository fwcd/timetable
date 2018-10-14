package com.fwcd.timetable.model.calendar.task;

import java.io.Serializable;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.fructose.Observable;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.view.utils.SubscriptionStack;

public class TaskListModel implements Serializable {
	private static final long serialVersionUID = 3478629580505983160L;
	private final Observable<String> name;
	private final ObservableList<TaskModel> tasks = new ObservableList<>();
	
	private transient EventListenerList<TaskListModel> nullableChangeListeners;
	private transient SubscriptionStack nullableTaskSubscriptions;
	
	public TaskListModel(String name) {
		this.name = new Observable<>(name);
		setupChangeListeners();
	}
	
	private void setupChangeListeners() {
		name.listen(it -> getChangeListeners().fire(this));
		tasks.listenAndFire(it -> {
			getChangeListeners().fire(this);
			getTaskSubscriptions().unsubscribeAll();
			getTaskSubscriptions().subscribeAll(tasks, TaskModel::getChangeListeners, task -> getChangeListeners().fire(this));
		});
	}
	
	public EventListenerList<TaskListModel> getChangeListeners() {
		if (nullableChangeListeners == null) {
			nullableChangeListeners = new EventListenerList<>();
		}
		return nullableChangeListeners;
	}
	
	public SubscriptionStack getTaskSubscriptions() {
		if (nullableTaskSubscriptions == null) {
			nullableTaskSubscriptions = new SubscriptionStack();
		}
		return nullableTaskSubscriptions;
	}

	public Observable<String> getName() { return name; }
	
	public ObservableList<TaskModel> getTasks() { return tasks; }
}
