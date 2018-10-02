package com.fwcd.timetable.view.sidebar.tasks;

import com.fwcd.timetable.model.tasks.TaskCrateModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.view.utils.HideableView;

import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TaskCrateView implements FxView {
	private final BorderPane node;
	private final Accordion listsView;
	
	private final TaskCrateViewModel viewModel;
	
	public TaskCrateView(TimeTableAppContext context, TaskCrateModel model) {
		node = new BorderPane();
		listsView = new Accordion(new TitledPane("Test 1", new Pane()));
		viewModel = new TaskCrateViewModel(model);
		
		NewTaskView newTaskView = new NewTaskView(context, viewModel);
		HideableView hideableNewTaskView = new HideableView(newTaskView);
		
		hideableNewTaskView.hide();
		newTaskView.getAddedTaskListeners().add(hideableNewTaskView::hide);
		
		node.setTop(new VBox(
			FxUtils.buttonOf(context.localized("newtask"), hideableNewTaskView::toggle),
			hideableNewTaskView.getNode()
		)); 
		node.setCenter(listsView);
	}
	
	@Override
	public Node getNode() { return node; }
}
