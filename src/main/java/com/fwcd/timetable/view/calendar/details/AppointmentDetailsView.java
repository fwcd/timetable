package com.fwcd.timetable.view.calendar.details;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class AppointmentDetailsView implements FxView {
	private final Pane node;
	
	public AppointmentDetailsView(AppointmentModel model) {
		node = new Pane();
		
	}
	
	@Override
	public Node getNode() { return node; }
}
