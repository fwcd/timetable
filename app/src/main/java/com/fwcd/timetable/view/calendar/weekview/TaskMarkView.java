package com.fwcd.timetable.view.calendar.weekview;

import com.fwcd.timetable.model.calendar.task.TaskModel;
import com.fwcd.timetable.view.FxView;
import com.fwcd.timetable.model.utils.SubscriptionStack;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class TaskMarkView implements FxView, AutoCloseable {
	private final StackPane node;
	
	private final SubscriptionStack subscriptions = new SubscriptionStack();
	
	public TaskMarkView(TaskModel model) {
		node = new StackPane();
		
		ObservableList<Node> childs = node.getChildren();
		
		Line line = new Line();
		line.getStyleClass().add("task-mark-view");
		line.setManaged(false);
		line.setStartX(0);
		line.setStartY(0);
		line.setEndY(0);
		line.endXProperty().bind(node.widthProperty());
		childs.add(line);
		
		Text text = new Text("Test");
		text.getStyleClass().add("task-mark-view");
		text.setManaged(false);
		text.setLayoutX(0);
		text.setLayoutY(0);
		subscriptions.push(model.getName().subscribeAndFire(text::setText));
		childs.add(text);
	}
	
	@Override
	public Pane getNode() { return node; }

	@Override
	public void close() {
		subscriptions.unsubscribeAll();
	}
}
