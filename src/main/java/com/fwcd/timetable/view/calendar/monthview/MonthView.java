package com.fwcd.timetable.view.calendar.monthview;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class MonthView implements FxView {
	private final GridPane node;
	
	public MonthView(ObservableList<CalendarModel> calendars) {
		node = new GridPane();
	}
	
	@Override
	public Node getNode() { return node; }
}
