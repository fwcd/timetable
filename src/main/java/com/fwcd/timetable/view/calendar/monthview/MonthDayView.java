package com.fwcd.timetable.view.calendar.monthview;

import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class MonthDayView implements FxView {
	private final Pane node;
	
	public MonthDayView() {
		node = new Pane();
	}
	
	@Override
	public Node getNode() { return node; }
}
