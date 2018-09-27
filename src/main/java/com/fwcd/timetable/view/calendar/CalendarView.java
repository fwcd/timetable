package com.fwcd.timetable.view.calendar;

import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;

public class CalendarView implements FxView {
	private final Node node;
	private final WeekView week;
	
	public CalendarView(CalendarModel model) {
		week = new WeekView(model);
		node = week.getNode();
	}
	
	@Override
	public Node getNode() { return node; }
}
