package fwcd.timetable.view.calendar.popover;

import java.time.LocalDateTime;

import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NewAppointmentView implements FxView {
	private final Pane node;
	private final LocalDateTime start;
	private final CalendarCrateViewModel crate;
	private Runnable onDelete = () -> {};
	
	public NewAppointmentView(Localizer localizer, TemporalFormatters formatters, CalendarCrateViewModel crate, LocalDateTime start) {
		this.start = start;
		this.crate = crate;
		
		node = new VBox(
			crate.getCalendars().stream()
				.map(cal -> FxUtils.buttonOf(
					localizer.localize("newappointment") + " in " + cal.getValue().getName(),
					() -> createAppointment(localizer, formatters, cal.getId())
				))
				.toArray(Button[]::new)
		);
		node.setPadding(new Insets(10));
	}
	
	private void createAppointment(Localizer localizer, TemporalFormatters formatters, int calendarId) {
		AppointmentModel appointment = new AppointmentModel.Builder("", calendarId)
			.start(start)
			.end(start.plusHours(1))
			.build();
		node.setPadding(Insets.EMPTY);
		crate.add(appointment);
		
		AppointmentDetailsView detailsView = new AppointmentDetailsView(localizer, formatters, crate, appointment);
		detailsView.setOnDelete(onDelete);
		node.getChildren().setAll(detailsView.getNode());
	}
	
	@Override
	public Pane getNode() { return node; }

	public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
}
