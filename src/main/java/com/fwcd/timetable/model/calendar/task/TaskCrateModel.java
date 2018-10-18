package com.fwcd.timetable.model.calendar.task;

import java.io.Serializable;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.json.PostDeserializable;
import com.fwcd.timetable.view.utils.SubscriptionStack;

public class TaskCrateModel implements Serializable, PostDeserializable {
	private static final long serialVersionUID = -1485835006395536825L;
	private final ObservableList<TaskListModel> lists = new ObservableList<>();
	
	private transient EventListenerList<TaskCrateModel> nullableChangeListeners;
	private transient SubscriptionStack nullableListSubscriptions;
	
	public TaskCrateModel() {
		setupChangeListeners();
	}
	
	@Override
	public void postDeserialize() {
		setupChangeListeners();
	}
	
	private void setupChangeListeners() {
		lists.listenAndFire(it -> {
			getChangeListeners().fire(this);
			getListSubscriptions().unsubscribeAll();
			getListSubscriptions().subscribeAll(lists, TaskListModel::getChangeListeners, list -> getChangeListeners().fire(this));
		});
	}
	
	public ObservableList<TaskListModel> getLists() { return lists; }
	
	public int size() { return lists.size(); }
	
	private SubscriptionStack getListSubscriptions() {
		if (nullableListSubscriptions == null) {
			nullableListSubscriptions = new SubscriptionStack();
		}
		return nullableListSubscriptions;
	}

	public EventListenerList<TaskCrateModel> getChangeListeners() {
		if (nullableChangeListeners == null) {
			nullableChangeListeners = new EventListenerList<>();
		}
		return nullableChangeListeners;
	}
}
