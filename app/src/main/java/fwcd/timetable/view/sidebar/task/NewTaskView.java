package fwcd.timetable.view.sidebar.task;

import fwcd.fructose.ListenerList;
import fwcd.fructose.Observable;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.view.FxView;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NewTaskView implements FxView {
	private final Pane node;
	private final TextField nameTextField;
	private final ListenerList addedTaskListeners = new ListenerList();
	
	private final TaskCrateViewModel crate;
	private final Observable<String> editedTaskName;
	
	public NewTaskView(TimeTableAppContext context, TaskCrateViewModel crate) {
		this.crate = crate;
		
		editedTaskName = new Observable<>(newTaskName());
		nameTextField = FxUtils.textFieldOf(editedTaskName);
		node = new VBox(
			new HBox(FxUtils.labelOf(context.localized("name"), ": "), nameTextField),
			FxUtils.buttonOf(context.localized("addtaskbutton"), this::addTaskToList)
		);
		
		nameTextField.setOnAction(e -> addTaskToList());
		node.setPadding(new Insets(6, 6, 6, 6));
	}
	
	public void focus() {
		nameTextField.requestFocus();
	}
	
	private void addTaskToList() {
		crate.getSelectedList().get().ifPresent(list -> {
			if (!editedTaskName.get().isEmpty()) {
				list.getTasks().add(new TaskModel(editedTaskName.get()));
				editedTaskName.set(newTaskName());
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
