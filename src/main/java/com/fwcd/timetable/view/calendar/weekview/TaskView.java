package com.fwcd.timetable.view.calendar.weekview;

import com.fwcd.timetable.model.calendar.task.TaskModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

public class TaskView implements FxView {
	private final StackPane node;
	
	public TaskView(TaskModel model) {
		node = new StackPane();
		
		Line line = new Line();
		line.setStartX(0);
		line.setStartY(0);
		line.setEndY(0);
		line.endXProperty().bind(node.widthProperty());
	}
	
	@Override
	public Pane getNode() { return node; }
}
