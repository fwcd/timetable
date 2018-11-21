package com.fwcd.timetable.view.sidebar.calendar;

import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.view.FxView;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;

public class CalendarsSideView implements FxView {
	private final Node node;
	
	public CalendarsSideView(TimeTableAppContext context, CalendarsViewModel viewModel) {
		node = new CalendarManagerView(context, viewModel).getNode();
	}
	
	@Override
	public Node getNode() { return node; }
}
