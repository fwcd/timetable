package com.fwcd.timetable.view.calendar.listview;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Collectors;

import com.fwcd.fructose.function.Subscription;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.view.utils.calendar.CalendarEntryListView;

import javafx.scene.Node;

public class CalendarListView implements FxView {
	private final Node node;
	private final ObservableList<CalendarEntryModel> entries;
	private final Queue<Subscription> calendarSubscriptions = new ArrayDeque<>();
	
	public CalendarListView(ObservableList<CalendarModel> calendars) {
		entries = new ObservableList<>();
		calendars.listenAndFire(it -> updateListenersAndEntries(calendars));
		
		CalendarEntryListView entriesView = new CalendarEntryListView();
		entries.listenAndFire(entriesView.getNode().getItems()::setAll);
		
		node = entriesView.getNode();
	}
	
	private void updateListenersAndEntries(ObservableList<CalendarModel> calendars) {
		while (!calendarSubscriptions.isEmpty()) {
			calendarSubscriptions.poll().unsubscribe();
		}
		
		for (CalendarModel calendar : calendars) {
			calendarSubscriptions.offer(calendar.getAppointments().subscribe(it -> updateEntries(calendars)));
		}
		
		updateEntries(calendars);
	}
	
	private void updateEntries(ObservableList<CalendarModel> calendars) {
		entries.set(calendars.stream().flatMap(it -> it.getAppointments().stream()).collect(Collectors.toList()));
	}
	
	@Override
	public Node getNode() { return node; }
}
