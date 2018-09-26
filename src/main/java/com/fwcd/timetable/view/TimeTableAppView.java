package com.fwcd.timetable.view;

import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class TimeTableAppView implements FxView {
	private final Pane node;
	
	public TimeTableAppView() {
		node = new Pane();
		node.getChildren().add(new Label("Hello FX!"));
	}
	
	@Override
	public Parent getNode() { return node; }
}
