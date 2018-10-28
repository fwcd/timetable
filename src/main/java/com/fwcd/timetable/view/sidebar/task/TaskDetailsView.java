package com.fwcd.timetable.view.sidebar.task;

import java.time.LocalDateTime;
import java.util.Collection;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.CalendarEntryModel;
import com.fwcd.timetable.model.calendar.task.TaskModel;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import tornadofx.control.DateTimePicker;

public class TaskDetailsView implements FxView {
	private final Pane node;
	private Runnable onDelete = () -> {};
	
	public TaskDetailsView(Collection<? extends CalendarEntryModel> parent, TimeTableAppContext context, TaskModel model) {
		TextField title = new TextField();
		FxUtils.bindBidirectionally(model.getName(), title.textProperty());
		
		BorderPane dateTimeGrid = new BorderPane();
		
		CheckBox hasDueDateTime = new CheckBox();
		FxUtils.bindBidirectionally(
			model.getDueDateTime(),
			hasDueDateTime.selectedProperty(),
			optDT -> optDT.isPresent(),
			selected -> selected
				? model.getDueDateTime().get().or(() -> Option.of(LocalDateTime.now()))
				: Option.empty()
		);
		dateTimeGrid.setTop(new HBox(localizedPropertyLabel("hasduedatetime", context), hasDueDateTime));
		
		DateTimePicker dueDateTime = new DateTimePicker();
		FxUtils.bindBidirectionally(
			model.getDueDateTime(),
			dueDateTime.dateTimeValueProperty(),
			optDT -> optDT.orElseNull(),
			newDT -> Option.ofNullable(newDT)
		);
		
		model.getDueDateTime().listenAndFire(dateTime -> {
			if (dateTime.isPresent()) {
				dateTimeGrid.setCenter(dueDateTime);
			} else {
				dateTimeGrid.setCenter(null);
			}
		});
		
		Button deleteButton = FxUtils.buttonOf(context.localized("deletetask"), () -> {
			parent.remove(model);
			onDelete.run();
		});
		
		node = new VBox(
			title,
			dateTimeGrid,
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
