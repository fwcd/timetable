package fwcd.timetable.view.calendar.popover;

import java.util.Collection;

import fwcd.fructose.Option;
import fwcd.fructose.time.LocalDateTimeInterval;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
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
	private final VBox node;
	private Runnable onDelete = () -> {};
	
	public AppointmentDetailsView(Collection<? extends CalendarEntryModel> parent, Localizer localizer, TemporalFormatters formatters, AppointmentViewModel viewModel) {
		TextField title = new TextField();
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
		
		GridPane properties = new GridPane();
		int rowIndex = 0;
		
		DateTimePicker start = new DateTimePicker();
		start.setFormat(formatters.getRawDateTimeFormat());
		FxUtils.bindBidirectionally(
			model.getDateTimeInterval(),
			start.dateTimeValueProperty(),
			interval -> interval.getStart(),
			dateTime -> new LocalDateTimeInterval(dateTime, model.getEnd())
		);
		properties.addRow(rowIndex++, localizedPropertyLabel("appointmentstart", localizer), start);
		
		DateTimePicker end = new DateTimePicker();
		end.setFormat(formatters.getRawDateTimeFormat());
		FxUtils.bindBidirectionally(
			model.getDateTimeInterval(),
			end.dateTimeValueProperty(),
			interval -> interval.getEnd(),
			dateTime -> new LocalDateTimeInterval(model.getStart(), dateTime)
		);
		properties.addRow(rowIndex++, localizedPropertyLabel("appointmentend", localizer), end);
		
		TextField recurrence = new TextField();
		FxUtils.bindBidirectionally(model.getRecurrence().getRaw(), recurrence.textProperty());
		properties.addRow(rowIndex++, localizedPropertyLabel("recurrence", localizer), recurrence);
		
		DatePicker recurrenceEnd = new DatePicker();
		FxUtils.setDateFormat(recurrenceEnd, formatters.getRawDateFormat());
		FxUtils.bindBidirectionally(
			model.getRecurrenceEnd(),
			recurrenceEnd.valueProperty(),
			optEnd -> optEnd.orElseNull(),
			newEnd -> Option.ofNullable(newEnd)
		);
		properties.addRow(rowIndex++, localizedPropertyLabel("recurrenceend", localizer), recurrenceEnd);
		
		CheckBox ignoreDate = new CheckBox();
		FxUtils.bindBidirectionally(model.ignoresDate(), ignoreDate.selectedProperty());
		properties.addRow(rowIndex++, localizedPropertyLabel("ignoredate", localizer), ignoreDate);
		
		CheckBox ignoreTime = new CheckBox();
		FxUtils.bindBidirectionally(model.ignoresTime(), ignoreTime.selectedProperty());
		properties.addRow(rowIndex++, localizedPropertyLabel("ignoretime", localizer), ignoreTime);
		
		Button deleteButton = FxUtils.buttonOf(localizer.localized("deleteappointment"), () -> {
			parent.remove(model);
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

	private Label localizedPropertyLabel(String unlocalized, Localizer localizer) {
		return FxUtils.labelOf(localizer.localized(unlocalized), ": ");
	}
	
	public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
	
	@Override
	public Node getNode() { return node; }
}
