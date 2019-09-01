package fwcd.timetable.view.sidebar.task;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.model.calendar.recurrence.Recurrence;
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
	private Pane node;
	
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
		title.textProperty().addListener((obs, oldText, newText) -> changeModel(task -> task.with().name(newText).build()));
		title.getStyleClass().add("title-label");
		
		TextField location = new TextField();
		location.setPromptText(localizer.localize("location"));
		location.setText(model.getLocation().map(Location::getLabel).orElse(""));
		location.textProperty().addListener((obs, oldText, newText) ->
			changeModel(task -> task.with()
				.location(Option.of(newText)
				.filter(it -> !it.isEmpty())
				.map(Location::new))
				.build())
		);
		location.getStyleClass().add("location-label");
		
		BorderPane dateTimeGrid = new BorderPane();
		
		CheckBox hasDateTime = new CheckBox();
		hasDateTime.setSelected(model.getDateTime().isPresent());
		hasDateTime.selectedProperty().addListener((obs, wasSelected, isSelected) ->
			changeModel(task -> task.with()
				.dateTime(model.getDateTime().or(() -> Option.of(LocalDateTime.now()))
				.filter(it -> isSelected))
				.build())
		);
		dateTimeGrid.setTop(new HBox(localizedPropertyLabel("hasdatetime", localizer), hasDateTime));
		
		GridPane properties = new GridPane();
		int rowIndex = 0;
		
		DateTimePicker dateTimePicker = new DateTimePicker();
		dateTimePicker.setDateTimeValue(model.getDateTime().orElse(null));
		dateTimePicker.dateTimeValueProperty().addListener((obs, oldDT, newDT) -> changeModel(task -> task.with().dateTime(Option.ofNullable(newDT)).build()));
		properties.addRow(rowIndex++, localizedPropertyLabel("datetime", localizer), dateTimePicker);
		
		TextField recurrence = new TextField();
		recurrence.setText(model.getRawRecurrence());
		recurrence.textProperty().addListener((obs, oldText, newText) -> changeModel(task -> task.with().recurrence(newText).build()));
		properties.addRow(rowIndex++, localizedPropertyLabel("recurrence", localizer), recurrence);
		
		DatePicker recurrenceEnd = new DatePicker();
		FxUtils.setDateFormat(recurrenceEnd, formatters.getDateFormatter());
		recurrenceEnd.setValue(model.getRecurrence().flatMap(Recurrence::getEnd).orElse(null));
		recurrenceEnd.valueProperty().addListener((obs, oldValue, newValue) -> changeModel(task -> task.with().recurrenceEnd(Option.ofNullable(newValue)).build()));
		properties.addRow(rowIndex++, localizedPropertyLabel("recurrenceend", localizer), recurrenceEnd);
		
		Consumer<Boolean> hasDateTimeListener = hasDT -> {
			if (hasDT) {
				dateTimeGrid.setCenter(properties);
			} else {
				dateTimeGrid.setCenter(null);
			}
		};
		hasDateTime.selectedProperty().addListener((obs, wasSelected, isSelected) -> hasDateTimeListener.accept(isSelected));
		hasDateTimeListener.accept(hasDateTime.isSelected());
		
		Button deleteButton = FxUtils.buttonOf(localizer.localized("deletetask"), () -> {
			crate.remove(model);
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
		return new Label(localizer.localize(unlocalized) + ": ");
	}

	public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
	
	@Override
	public Node getNode() { return node; }
}
