package com.fwcd.timetable.view.sidebar;

import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class TasksView implements FxView {
	private final Pane node;
	
	public TasksView() {
		node = new Pane();
	}
	
	@Override
	public Node getNode() { return node; }
}
