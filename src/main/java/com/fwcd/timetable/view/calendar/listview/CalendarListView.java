package com.fwcd.timetable.view.calendar.listview;

import java.util.stream.Collectors;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.view.utils.FxNavigableView;
import com.fwcd.timetable.view.utils.calendar.CalendarEntryListView;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;

public class CalendarListView implements FxNavigableView {
	private final Node node;
	private final ObservableList<CalendarEntryModel> entries;
	
	public CalendarListView(CalendarsViewModel calendars) {
		entries = new ObservableList<>();
		calendars.getModel().getChangeListeners().add(it -> updateEntries(calendars));
		updateEntries(calendars);
		
		CalendarEntryListView entriesView = new CalendarEntryListView();
		entries.listenAndFire(entriesView.getNode().getItems()::setAll);
		
		node = entriesView.getNode();
	}
	
	private void updateEntries(CalendarsViewModel calendars) {
		entries.set(calendars.getModel().getCalendars()
			.stream()
			.flatMap(it -> it.getAppointments().stream())
			.collect(Collectors.toList()));
	}
	
	@Override
	public Node getContentNode() { return node; }
}
