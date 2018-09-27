package com.fwcd.timetable.view.calendar;

import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class CalendarView implements FxView {
	private final Pane node;
	
	public CalendarView(CalendarModel model) {
		node = new Pane();
	}
	
	@Override
	public Parent getNode() { return node; }
}
