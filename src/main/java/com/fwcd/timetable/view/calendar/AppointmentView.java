package com.fwcd.timetable.view.calendar;

import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class AppointmentView implements FxView {
	private final Pane node;
	
	public AppointmentView() {
		node = new Pane();
	}
	
	@Override
	public Node getNode() { return node; }
}
