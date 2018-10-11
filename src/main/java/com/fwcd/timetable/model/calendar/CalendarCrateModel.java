package com.fwcd.timetable.model.calendar;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.view.utils.SubscriptionStack;

public class CalendarCrateModel {
	private final ObservableList<CalendarModel> calendars = new ObservableList<>();
	
	private final EventListenerList<CalendarCrateModel> changeListeners = new EventListenerList<>();
	private final SubscriptionStack calendarSubscriptions = new SubscriptionStack();
	
	public CalendarCrateModel() {
		calendars.add(new CalendarModel("Calendar"));
		setupChangeListeners();
	}
	
	private void setupChangeListeners() {
		calendars.listenAndFire(it -> {
			changeListeners.fire(this);
			calendarSubscriptions.unsubscribeAll();
			calendarSubscriptions.subscribeAll(calendars, CalendarModel::getChangeListeners, cal -> changeListeners.fire(this));
		});
	}
	
	public EventListenerList<CalendarCrateModel> getChangeListeners() { return changeListeners; }
	
	public ObservableList<CalendarModel> getCalendars() { return calendars; }
}
