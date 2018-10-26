package com.fwcd.timetable.view.calendar.weekview;

import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class TaskView implements FxView {
	private final StackPane node;
	
	public TaskView() {
		node = new StackPane();
		
	}
	
	@Override
	public Node getNode() { return node; }
}
