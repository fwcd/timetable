package com.fwcd.timetable.view.sidebar.task;

import com.fwcd.timetable.model.calendar.task.TaskCrateModel;
import com.fwcd.timetable.model.calendar.task.TaskListModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.view.utils.HideableView;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TaskCrateView implements FxView {
	private final BorderPane node;
	private final TaskCrateViewModel viewModel;
	
	public TaskCrateView(TimeTableAppContext context, TaskCrateModel model) {
		if (model.size() == 0) {
			model.getLists().add(new TaskListModel(context.localize("tasks")));
		}
		
		node = new BorderPane();
		viewModel = new TaskCrateViewModel(model);
		
		NewTaskView newTaskView = new NewTaskView(context, viewModel);
		HideableView hideableNewTaskView = new HideableView(newTaskView);
		hideableNewTaskView.hide();
		
		VBox top = new VBox(
			FxUtils.buttonOf(context.localized("newtask"), () -> {
				hideableNewTaskView.toggle();
				newTaskView.focus();
			}),
			hideableNewTaskView.getNode()
		);
		top.setPadding(new Insets(4, 4, 4, 4));
		node.setTop(top);
		
		TaskListsView lists = new TaskListsView(viewModel);
		node.setCenter(lists.getNode());
	}
	
	@Override
	public Node getNode() { return node; }
}
