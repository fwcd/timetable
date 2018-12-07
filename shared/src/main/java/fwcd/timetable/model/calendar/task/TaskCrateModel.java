package fwcd.timetable.model.calendar.task;

import java.io.Serializable;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.structs.ObservableList;
import fwcd.timetable.model.json.PostDeserializable;
import fwcd.timetable.model.utils.SubscriptionStack;

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
