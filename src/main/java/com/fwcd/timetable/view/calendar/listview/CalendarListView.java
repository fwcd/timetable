package com.fwcd.timetable.view.calendar.listview;

import java.util.stream.Collectors;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarCrateModel;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.view.utils.FxNavigableView;
import com.fwcd.timetable.view.utils.calendar.CalendarEntryListView;

import javafx.scene.Node;

public class CalendarListView implements FxNavigableView {
	private final Node node;
	private final ObservableList<CalendarEntryModel> entries;
	
	public CalendarListView(CalendarCrateModel calendars) {
		entries = new ObservableList<>();
		calendars.getChangeListeners().add(it -> updateEntries(calendars));
		updateEntries(calendars);
		
		CalendarEntryListView entriesView = new CalendarEntryListView();
		entries.listenAndFire(entriesView.getNode().getItems()::setAll);
		
		node = entriesView.getNode();
	}
	
	private void updateEntries(CalendarCrateModel calendars) {
		entries.set(calendars.getCalendars()
			.stream()
			.flatMap(it -> it.getAppointments().stream())
			.collect(Collectors.toList()));
	}
	
	@Override
	public Node getContentNode() { return node; }
}
