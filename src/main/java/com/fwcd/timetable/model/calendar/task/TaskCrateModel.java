package com.fwcd.timetable.model.calendar.task;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.view.utils.SubscriptionStack;

public class TaskCrateModel {
	private final ObservableList<TaskListModel> lists = new ObservableList<>();
	
	private final EventListenerList<TaskCrateModel> changeListeners = new EventListenerList<>();
	private final SubscriptionStack listSubscriptions = new SubscriptionStack();
	
	public TaskCrateModel() {
		setupChangeListeners();
	}
	
	private void setupChangeListeners() {
		lists.listenAndFire(it -> {
			changeListeners.fire(this);
			listSubscriptions.unsubscribeAll();
			listSubscriptions.subscribeAll(lists, TaskListModel::getChangeListeners, list -> changeListeners.fire(this));
		});
	}
	
	public ObservableList<TaskListModel> getLists() { return lists; }
	
	public int size() { return lists.size(); }
}
