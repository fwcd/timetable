package com.fwcd.timetable.view.calendar;

import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;
import com.jibbow.fastis.WeekView;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class CalendarView implements FxView {
	private final Pane node;
	private final CalendarModel model;
	
	public CalendarView(CalendarModel model) {
		this.model = model;
		node = new WeekView();
	}
	
	@Override
	public Parent getNode() { return node; }
}
