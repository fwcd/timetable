package com.fwcd.timetable.view;

import com.fwcd.timetable.model.TimeTableAppModel;
import com.fwcd.timetable.view.calendar.CalendarView;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class TimeTableAppView implements FxView {
	private final BorderPane node;
	private final TimeTableAppModel model = new TimeTableAppModel();
	private final ViewContext context = new ViewContext();
	
	public TimeTableAppView() {
		node = new BorderPane();
		node.setCenter(new CalendarView(model.getCalendar()).getNode());
	}
	
	@Override
	public Parent getNode() { return node; }
}
