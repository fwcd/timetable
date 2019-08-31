package fwcd.timetable.view.calendar.popover;

import java.util.Collection;
import java.util.function.Consumer;

import fwcd.fructose.Option;
import fwcd.fructose.time.LocalDateTimeInterval;
import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.Location;
import fwcd.timetable.model.calendar.recurrence.Recurrence;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import fwcd.timetable.viewmodel.calendar.AppointmentViewModel;
import fwcd.timetable.viewmodel.calendar.CalendarViewModel;
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
	
	private final Consumer<AppointmentViewModel> viewModelListener;
	
	public AppointmentDetailsView(Localizer localizer, TemporalFormatters formatters, CalendarViewModel calendar, AppointmentViewModel viewModel) {
		TextField title = new TextField();
		localizer.localized("title").listenAndFire(title::setPromptText);
		title.getStyleClass().add("title-label");
		
		TextField location = new TextField();
		localizer.localized("location").listenAndFire(location::setPromptText);
		location.getStyleClass().add("location-label");
		
		GridPane properties = new GridPane();
		int rowIndex = 0;
		
		DateTimePicker start = new DateTimePicker();
		start.setFormat(formatters.getRawDateTimeFormat());
		properties.addRow(rowIndex++, localizedPropertyLabel("appointmentstart", localizer), start);
		
		DateTimePicker end = new DateTimePicker();
		end.setFormat(formatters.getRawDateTimeFormat());
		properties.addRow(rowIndex++, localizedPropertyLabel("appointmentend", localizer), end);
		
		TextField recurrence = new TextField();
		properties.addRow(rowIndex++, localizedPropertyLabel("recurrence", localizer), recurrence);
		
		DatePicker recurrenceEnd = new DatePicker();
		FxUtils.setDateFormat(recurrenceEnd, formatters.getRawDateFormat());
		properties.addRow(rowIndex++, localizedPropertyLabel("recurrenceend", localizer), recurrenceEnd);
		
		CheckBox ignoreDate = new CheckBox();
		properties.addRow(rowIndex++, localizedPropertyLabel("ignoredate", localizer), ignoreDate);
		
		CheckBox ignoreTime = new CheckBox();
		properties.addRow(rowIndex++, localizedPropertyLabel("ignoretime", localizer), ignoreTime);
		
		Button deleteButton = FxUtils.buttonOf(localizer.localized("deleteappointment"), () -> {
			calendar.remove(viewModel.getModel());
			onDelete.run();
		});
		
		node = new VBox(
			title,
			location,
			properties,
			deleteButton
		);
		node.getStyleClass().add("details-view");
		
		// Setup view model listener
		
		viewModelListener = vm -> {
			AppointmentModel model = vm.getModel();
			title.setText(model.getName());
			location.setText(model.getLocation().map(Location::getLabel).orElse(""));
			start.setDateTimeValue(model.getDateTimeInterval().getStart());
			end.setDateTimeValue(model.getDateTimeInterval().getEnd());
			recurrence.setText(model.getRawRecurrence());
			recurrenceEnd.setValue(model.getRecurrence().flatMap(Recurrence::getEnd).orElse(null));
			ignoreDate.setSelected(model.ignoresDate());
			ignoreTime.setSelected(model.ignoresTime());
		};
		viewModelListener.accept(viewModel);
		viewModel.getChangeListeners().addWeakListener(viewModelListener);
		
		// TODO: Setup FX listener
	}

	private Label localizedPropertyLabel(String unlocalized, Localizer localizer) {
		return FxUtils.labelOf(localizer.localized(unlocalized), ": ");
	}
	
	public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
	
	@Override
	public Node getNode() { return node; }
}
