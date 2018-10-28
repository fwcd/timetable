package com.fwcd.timetable.view.sidebar.task;

import java.util.Collection;

import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.calendar.task.TaskModel;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TaskDetailsView implements FxView {
	private final Pane node;
	private Runnable onDelete = () -> {};
	
	public TaskDetailsView(Collection<? extends CalendarEntryModel> parent, TimeTableAppContext context, TaskModel model) {
		TextField title = new TextField();
		FxUtils.bindBidirectionally(model.getName(), title.textProperty());
		
		GridPane properties = new GridPane();
		int rowIndex = 0;
		
		Button deleteButton = FxUtils.buttonOf(context.localized("deletetask"), () -> {
			parent.remove(model);
			onDelete.run();
		});
		
		node = new VBox(
			title,
			properties,
			deleteButton
		);
		node.getStyleClass().add("details-view");
	}

	private Label localizedPropertyLabel(String unlocalized, TimeTableAppContext context) {
		return FxUtils.labelOf(context.localized(unlocalized), ": ");
	}

	public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
	
	@Override
	public Node getNode() { return node; }
}
