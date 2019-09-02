package fwcd.timetable.view.sidebar.task;

import fwcd.timetable.model.calendar.task.TaskListModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.view.utils.HideableView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TaskManagerView implements FxView {
	private final BorderPane node;
	private final TaskManagerViewModel viewModel;
	
	public TaskManagerView(TimeTableAppContext context, CalendarCrateViewModel crate, int calendarId) {
		if (crate.getTaskLists().stream().noneMatch(it -> it.getValue().getCalendarId() == calendarId)) {
			crate.add(new TaskListModel(context.localize("tasks"), calendarId));
			// TODO: Select this task list already?
		}
		
		node = new BorderPane();
		viewModel = new TaskManagerViewModel(crate);
		
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
		
		TaskListsView lists = new TaskListsView(context, viewModel, calendarId);
		node.setCenter(lists.getNode());
	}
	
	@Override
	public Node getNode() { return node; }
}
