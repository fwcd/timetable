package com.fwcd.timetable.view.sidebar.calendar;

import com.fwcd.timetable.model.TimeTableAppModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;

public class CalendarsSideView implements FxView {
	private final Node node;
	
	public CalendarsSideView(TimeTableAppContext context, TimeTableAppModel model) {
		node = new CalendarManagerView(context, model).getNode();
	}
	
	@Override
	public Node getNode() { return node; }
}
