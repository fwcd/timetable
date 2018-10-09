package com.fwcd.timetable.view.calendar.listview;

import java.util.stream.Collectors;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxNavigableView;
import com.fwcd.timetable.view.utils.SubscriptionStack;
import com.fwcd.timetable.view.utils.calendar.CalendarEntryListView;

import javafx.scene.Node;

public class CalendarListView implements FxNavigableView {
	private final Node node;
	private final ObservableList<CalendarEntryModel> entries;
	private final SubscriptionStack calendarSubscriptions = new SubscriptionStack();
	
	public CalendarListView(ObservableList<CalendarModel> calendars) {
		entries = new ObservableList<>();
		calendars.listenAndFire(it -> updateListenersAndEntries(calendars));
		
		CalendarEntryListView entriesView = new CalendarEntryListView();
		entries.listenAndFire(entriesView.getNode().getItems()::setAll);
		
		node = entriesView.getNode();
	}
	
	private void updateListenersAndEntries(ObservableList<CalendarModel> calendars) {
		calendarSubscriptions.unsubscribeAll();
		calendarSubscriptions.subscribeAll(calendars, CalendarModel::getAppointments, it -> updateEntries(calendars));
		updateEntries(calendars);
	}
	
	private void updateEntries(ObservableList<CalendarModel> calendars) {
		entries.set(calendars.stream().flatMap(it -> it.getAppointments().stream()).collect(Collectors.toList()));
	}
	
	@Override
	public Node getContent() { return node; }
}
