package com.fwcd.timetable.view.calendar.listview;

import java.util.stream.Collectors;

import com.fwcd.timetable.view.utils.FxNavigableView;
import com.fwcd.timetable.view.utils.calendar.CalendarEntryListView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;

public class CalendarListView implements FxNavigableView {
	private final Node node;
	private final CalendarEntryListView entries;
	
	public CalendarListView(TimeTableAppContext context, CalendarsViewModel calendars) {
		entries = new CalendarEntryListView(context);
		node = entries.getNode();
		
		calendars.getChangeListeners().add(it -> updateEntries(calendars));
		updateEntries(calendars);
	}
	
	private void updateEntries(CalendarsViewModel calendars) {
		entries.getNode().getItems().setAll(calendars.getSelectedCalendars()
			.stream()
			.flatMap(it -> it.getAppointments().stream())
			.sorted()
			.collect(Collectors.toList())
		);
	}
	
	@Override
	public Node getContentNode() { return node; }
}
