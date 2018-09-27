package com.fwcd.timetable.view.calendar;

import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class WeekDayAppointmentsOverlay implements FxView {
	private final Pane node;
	
	public WeekDayAppointmentsOverlay() {
		node = new Pane();
	}
	
	@Override
	public Node getNode() { return node; }
}
