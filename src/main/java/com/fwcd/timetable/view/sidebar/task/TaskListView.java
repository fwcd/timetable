package com.fwcd.timetable.view.sidebar.task;

import java.util.List;

import com.fwcd.timetable.model.calendar.task.TaskListModel;
import com.fwcd.timetable.model.calendar.task.TaskModel;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TaskListView implements FxView {
	private final Pane node;
	private final TaskListModel model;
	
	public TaskListView(TaskListModel model) {
		this.model = model;
		node = new VBox();
		model.getTasks().listenAndFire(this::setVisibleTasks);
	}
	
	private void setVisibleTasks(List<TaskModel> tasks) {
		node.getChildren().clear();
		
		tasks.stream()
			.map(TaskModel::getName)
			.map(FxUtils::labelOf)
			.forEach(node.getChildren()::add);
	}
	
	public TaskListModel getModel() { return model; }
	
	@Override
	public Node getNode() { return node; }
}
