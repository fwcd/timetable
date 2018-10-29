package com.fwcd.timetable.view.calendar.weekview;

import com.fwcd.timetable.api.view.FxView;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class CurrentTimeIndicator implements FxView {
	private static final double CIRCLE_RADIUS = 5;
	private final StackPane node;
	
	public CurrentTimeIndicator() {
		node = new StackPane();
		
		ObservableList<Node> childs = node.getChildren();
		
		Circle circle = new Circle(0, 0, CIRCLE_RADIUS);
		StackPane.setAlignment(circle, Pos.CENTER_LEFT);
		circle.setManaged(false);
		circle.getStyleClass().add("current-time-indicator");
		childs.add(circle);
		
		Line line = new Line();
		StackPane.setAlignment(line, Pos.CENTER_RIGHT);
		line.setManaged(false);
		line.getStyleClass().add("current-time-indicator");
		line.setStartX(0);
		line.setStartY(0);
		line.setEndY(0);
		line.endXProperty().bind(node.widthProperty());
		childs.add(line);
	}
	
	@Override
	public Pane getNode() { return node; }
}
