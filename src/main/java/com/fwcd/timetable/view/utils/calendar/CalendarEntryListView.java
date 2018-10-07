package com.fwcd.timetable.view.utils.calendar;

import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.control.ListView;

public class CalendarEntryListView implements FxView {
	private final ListView<CalendarEntryModel> node;
	
	public CalendarEntryListView() {
		node = new ListView<>();
		node.setCellFactory(list -> new CalendarEntryListCell());
	}
	
	@Override
	public ListView<CalendarEntryModel> getNode() { return node; }
}
