package fwcd.timetable.view.calendar.popover;

import java.time.LocalDateTime;

import fwcd.timetable.model.calendar.AppointmentModel;
import fwcd.timetable.model.calendar.CalendarCrateModel;
import fwcd.timetable.model.calendar.CalendarModel;
import fwcd.timetable.view.FxView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NewAppointmentView implements FxView {
	private final Pane node;
	private final LocalDateTime start;
	private Runnable onDelete = () -> {};
	
	public NewAppointmentView(Localizer localizer, CalendarCrateModel calendars, TemporalFormatters formatters, LocalDateTime start) {
		this.start = start;
		
		node = new VBox(
			calendars.getCalendars().stream()
				.map(cal -> FxUtils.buttonOf(
					localizer.localized("newappointment").mapStrongly(it -> it + " in " + cal.toString()),
					() -> createAppointment(localizer, formatters, cal))
				)
				.toArray(Button[]::new)
		);
		node.setPadding(new Insets(10));
	}
	
	private void createAppointment(Localizer localizer, TemporalFormatters formatters, CalendarModel calendar) {
		AppointmentModel appointment = new AppointmentModel.Builder("")
			.start(start)
			.end(start.plusHours(1))
			.build();
		node.setPadding(Insets.EMPTY);
		calendar.getAppointments().add(appointment);
		
		AppointmentDetailsView detailsView = new AppointmentDetailsView(calendar.getAppointments(), localizer, formatters, appointment);
		detailsView.setOnDelete(onDelete);
		node.getChildren().setAll(detailsView.getNode());
	}
	
	@Override
	public Pane getNode() { return node; }

	public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
}
