package fwcd.timetable.view.sidebar.task;

import fwcd.timetable.model.calendar.task.TaskCrateModel;
import fwcd.timetable.model.calendar.task.TaskListModel;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.HideableView;

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
		
		TaskListsView lists = new TaskListsView(context, viewModel);
		node.setCenter(lists.getNode());
	}
	
	@Override
	public Node getNode() { return node; }
}
