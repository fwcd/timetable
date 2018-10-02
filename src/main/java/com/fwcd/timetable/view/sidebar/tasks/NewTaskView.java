package com.fwcd.timetable.view.sidebar.tasks;

import com.fwcd.fructose.ListenerList;
import com.fwcd.timetable.model.tasks.TaskModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NewTaskView implements FxView {
	private final Pane node;
	private final ListenerList addedTaskListeners = new ListenerList();
	
	private final TimeTableAppContext context;
	private final TaskCrateViewModel crate;
	private TaskModel editedTask;
	
	public NewTaskView(TimeTableAppContext context, TaskCrateViewModel crate) {
		this.context = context;
		this.crate = crate;
		
		editedTask = newEmptyTask();
		node = new VBox(
			new HBox(FxUtils.labelOf(context.localized("name"), ": "), FxUtils.textFieldOf(editedTask.getName())),
			FxUtils.buttonOf(context.localized("addtaskbutton"), this::addTaskToList)
		);
		node.setPadding(new Insets(6, 6, 6, 6));
	}
	
	private void addTaskToList() {
		crate.getSelectedList().get().ifPresent(list -> {
			list.getTasks().add(editedTask);
			editedTask = newEmptyTask();
			addedTaskListeners.fire();
		});
	}
	
	private TaskModel newEmptyTask() {
		return new TaskModel(context.localize("newtask"));
	}
	
	public ListenerList getAddedTaskListeners() { return addedTaskListeners; }
	
	@Override
	public Node getNode() { return node; }
}
