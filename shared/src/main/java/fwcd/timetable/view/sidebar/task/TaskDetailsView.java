package fwcd.timetable.view.sidebar.task;

import java.time.LocalDateTime;
import java.util.function.UnaryOperator;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.model.calendar.task.TaskModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import tornadofx.control.DateTimePicker;

public class TaskDetailsView implements FxView {
	private final Pane node;
	
	private final CalendarCrateViewModel crate;
	private TaskModel model;
	private Runnable onDelete = () -> {};
	
	public TaskDetailsView(Localizer localizer, TemporalFormatters formatters, CalendarCrateViewModel crate, TaskModel model) {
		this.crate = crate;
		this.model = model;
		createNode(localizer, formatters);
	}
	
	private void createNode(Localizer localizer, TemporalFormatters formatters) {
		TextField title = new TextField();
		title.setPromptText(localizer.localize("title"));
		title.setText(model.getName());
		title.textProperty().addListener((obs, oldText, newText) -> changeModel(task));
		FxUtils.bindBidirectionally(model.getName(), title.textProperty());
		localizer.localized("title").listenAndFire(title::setPromptText);
		title.getStyleClass().add("title-label");
		
		TextField location = new TextField();
		FxUtils.bindBidirectionally(
			model.getLocation(),
			location.textProperty(),
			optLocation -> optLocation.map(Location::getLabel).orElse(""),
			newLocation -> Option.of(newLocation).filter(it -> !it.isEmpty()).map(Location::new)
		);
		localizer.localized("location").listenAndFire(location::setPromptText);
		location.getStyleClass().add("location-label");
		
		BorderPane dateTimeGrid = new BorderPane();
		
		CheckBox hasDateTime = new CheckBox();
		FxUtils.bindBidirectionally(
			model.getDateTime(),
			hasDateTime.selectedProperty(),
			optDT -> optDT.isPresent(),
			selected -> selected
				? model.getDateTime().get().or(() -> Option.of(LocalDateTime.now()))
				: Option.empty()
		);
		dateTimeGrid.setTop(new HBox(localizedPropertyLabel("hasdatetime", localizer), hasDateTime));
		
		GridPane properties = new GridPane();
		int rowIndex = 0;
		
		DateTimePicker dateTimePicker = new DateTimePicker();
		FxUtils.bindBidirectionally(
			model.getDateTime(),
			dateTimePicker.dateTimeValueProperty(),
			optDT -> optDT.orElseNull(),
			newDT -> Option.ofNullable(newDT)
		);
		properties.addRow(rowIndex++, localizedPropertyLabel("datetime", localizer), dateTimePicker);
		
		TextField recurrence = new TextField();
		FxUtils.bindBidirectionally(model.getRecurrence().getRaw(), recurrence.textProperty());
		properties.addRow(rowIndex++, localizedPropertyLabel("recurrence", localizer), recurrence);
		
		DatePicker recurrenceEnd = new DatePicker();
		FxUtils.setDateFormat(recurrenceEnd, formatters.getDateFormatter());
		FxUtils.bindBidirectionally(
			model.getRecurrenceEnd(),
			recurrenceEnd.valueProperty(),
			optEnd -> optEnd.orElseNull(),
			newEnd -> Option.ofNullable(newEnd)
		);
		properties.addRow(rowIndex++, localizedPropertyLabel("recurrenceend", localizer), recurrenceEnd);
		
		model.getDateTime().listenAndFire(dateTime -> {
			if (dateTime.isPresent()) {
				dateTimeGrid.setCenter(properties);
			} else {
				dateTimeGrid.setCenter(null);
			}
		});
		
		Button deleteButton = FxUtils.buttonOf(localizer.localized("deletetask"), () -> {
			parent.remove(model);
			onDelete.run();
		});
		
		node = new VBox(
			title,
			location,
			dateTimeGrid,
			deleteButton
		);
		node.getStyleClass().add("details-view");
	}
	
	private void changeModel(UnaryOperator<TaskModel> transform) {
		TaskModel newModel = transform.apply(model);
		crate.replace(model, newModel);
		model = newModel;
	}

	private Label localizedPropertyLabel(String unlocalized, Localizer localizer) {
		return FxUtils.labelOf(localizer.localized(unlocalized), ": ");
	}

	public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
	
	@Override
	public Node getNode() { return node; }
}
