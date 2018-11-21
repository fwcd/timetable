package com.fwcd.timetable.view.utils.calendar;

import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.utils.Contained;
import com.fwcd.timetable.view.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;

import javafx.scene.control.ListView;

public class CalendarEntryListView implements FxView {
	private final ListView<Contained<CalendarEntryModel>> node;
	
	public CalendarEntryListView(TimeTableAppContext context) {
		node = new ListView<>();
		node.setCellFactory(list -> new CalendarEntryListCell(context));
	}
	
	@Override
	public ListView<Contained<CalendarEntryModel>> getNode() { return node; }
}
