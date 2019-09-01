package fwcd.timetable.view.calendar.popover;

import java.util.function.UnaryOperator;

import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.model.calendar.recurrence.Recurrence;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tornadofx.control.DateTimePicker;

public class AppointmentDetailsView implements FxView {
	private VBox node;

	private final CalendarCrateViewModel crate;
	private AppointmentModel model;
	private Runnable onDelete = () -> {};
	
	public AppointmentDetailsView(Localizer localizer, TemporalFormatters formatters, CalendarCrateViewModel crate, AppointmentModel model) {
		this.crate = crate;
		this.model = model;
		createNode(localizer, formatters);
	}
	
	private void createNode(Localizer localizer, TemporalFormatters formatters) {
		TextField title = new TextField();
		title.setText(model.getName());
		title.setPromptText(localizer.localize("title"));
		title.textProperty().addListener((obs, oldText, newText) -> changeModel(app -> app.with().name(newText).build()));
		title.getStyleClass().add("title-label");
		
		TextField location = new TextField();
		location.setText(model.getLocation().map(Location::getLabel).orElse(""));
		location.setPromptText(localizer.localize("location"));
		location.textProperty().addListener((obs, oldText, newText) -> changeModel(app -> app.with().location(new Location(newText)).build()));
		location.getStyleClass().add("location-label");
		
		GridPane properties = new GridPane();
		int rowIndex = 0;
		
		DateTimePicker start = new DateTimePicker();
		start.setFormat(formatters.getRawDateTimeFormat());
		start.setDateTimeValue(model.getDateTimeInterval().getStart());
		start.dateTimeValueProperty().addListener((obs, oldDT, newDT) -> changeModel(app -> app.with().start(newDT).build()));
		properties.addRow(rowIndex++, localizedPropertyLabel("appointmentstart", localizer), start);
		
		DateTimePicker end = new DateTimePicker();
		end.setFormat(formatters.getRawDateTimeFormat());
		end.setDateTimeValue(model.getDateTimeInterval().getEnd());
		end.dateTimeValueProperty().addListener((obs, oldDT, newDT) -> changeModel(app -> app.with().end(newDT).build()));
		properties.addRow(rowIndex++, localizedPropertyLabel("appointmentend", localizer), end);
		
		TextField recurrence = new TextField();
		recurrence.setText(model.getRawRecurrence());
		recurrence.textProperty().addListener((obs, oldText, newText) -> changeModel(app -> app.with().recurrence(newText).build()));
		properties.addRow(rowIndex++, localizedPropertyLabel("recurrence", localizer), recurrence);
		
		DatePicker recurrenceEnd = new DatePicker();
		FxUtils.setDateFormat(recurrenceEnd, formatters.getRawDateFormat());
		recurrenceEnd.setValue(model.getRecurrence().flatMap(Recurrence::getEnd).orElse(null));
		recurrenceEnd.valueProperty().addListener((obs, oldDate, newDate) -> changeModel(app -> app.with().recurrenceEnd(newDate).build()));
		properties.addRow(rowIndex++, localizedPropertyLabel("recurrenceend", localizer), recurrenceEnd);
		
		CheckBox ignoreDate = new CheckBox();
		ignoreDate.setSelected(model.ignoresDate());
		ignoreDate.selectedProperty().addListener((obs, wasSelected, isSelected) -> changeModel(app -> app.with().ignoreDate(isSelected).build()));
		properties.addRow(rowIndex++, localizedPropertyLabel("ignoredate", localizer), ignoreDate);
		
		CheckBox ignoreTime = new CheckBox();
		ignoreTime.setSelected(model.ignoresTime());
		ignoreTime.selectedProperty().addListener((obs, wasSelected, isSelected) -> changeModel(app -> app.with().ignoreTime(isSelected).build()));
		properties.addRow(rowIndex++, localizedPropertyLabel("ignoretime", localizer), ignoreTime);
		
		Button deleteButton = FxUtils.buttonOf(localizer.localized("deleteappointment"), () -> {
			crate.remove(model);
			onDelete.run();
		});
		
		node = new VBox(
			title,
			location,
			properties,
			deleteButton
		);
		node.getStyleClass().add("details-view");
	}
	
	private void changeModel(UnaryOperator<AppointmentModel> transform) {
		AppointmentModel newModel = transform.apply(model);
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
