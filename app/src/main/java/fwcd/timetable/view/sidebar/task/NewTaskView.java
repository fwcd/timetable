package fwcd.timetable.view.sidebar.task;

import fwcd.fructose.ListenerList;
import fwcd.fructose.Observable;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NewTaskView implements FxView {
	private final Pane node;
	private final TextField nameTextField;
	private final ListenerList addedTaskListeners = new ListenerList();
	
	private final TaskManagerViewModel viewModel;
	private final Observable<String> editedTaskName;
	
	public NewTaskView(TimeTableAppContext context, TaskManagerViewModel viewModel) {
		this.viewModel = viewModel;
		
		editedTaskName = new Observable<>(newTaskName());
		nameTextField = FxUtils.textFieldOf(editedTaskName);
		node = new VBox(
			new HBox(new Label(context.localize("name") + ": "), nameTextField),
			FxUtils.buttonOf(context.localize("addtaskbutton"), this::addTaskToList)
		);
		
		nameTextField.setOnAction(e -> addTaskToList());
		node.setPadding(new Insets(6, 6, 6, 6));
	}
	
	public void focus() {
		nameTextField.requestFocus();
	}
	
	private void addTaskToList() {
		viewModel.getSelectedTaskListId().get().ifPresent(taskListId -> {
			if (!editedTaskName.get().isEmpty()) {
				CalendarCrateViewModel crate = viewModel.getCrate();
				int calendarId = crate.getTaskListById(taskListId).getCalendarId();
				editedTaskName.set(newTaskName());
				crate.add(new TaskModel(editedTaskName.get(), taskListId, calendarId));
				addedTaskListeners.fire();
			}
		});
	}

	private String newTaskName() {
		return "";
	}
	
	public ListenerList getAddedTaskListeners() { return addedTaskListeners; }
	
	@Override
	public Node getNode() { return node; }
}
