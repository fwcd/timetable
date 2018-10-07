package com.fwcd.timetable.view.sidebar.task;

import java.util.List;

import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.calendar.task.TaskListModel;
import com.fwcd.timetable.model.calendar.task.TaskModel;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.view.utils.calendar.CalendarEntryListView;

import javafx.scene.Node;
import javafx.scene.control.ListView;

public class TaskListView implements FxView {
	private final ListView<CalendarEntryModel> node;
	private final TaskListModel model;
	
	public TaskListView(TaskListModel model) {
		this.model = model;
		node = new CalendarEntryListView().getNode();
		model.getTasks().listenAndFire(this::setVisibleTasks);
	}
	
	private void setVisibleTasks(List<TaskModel> tasks) {
		node.getItems().setAll(tasks);
	}
	
	public TaskListModel getModel() { return model; }
	
	@Override
	public Node getNode() { return node; }
}
