package com.fwcd.timetable.view.calendar;

import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class CalendarView implements FxView {
	private final ScrollPane node;
	private final WeekView week;
	
	public CalendarView(CalendarModel model) {
		week = new WeekView(model);
		node = new ScrollPane(week.getNode());
		node.setFitToWidth(true);
	}
	
	@Override
	public Node getNode() { return node; }
}
