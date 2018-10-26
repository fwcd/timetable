package com.fwcd.timetable.view.calendar.popover;

import java.time.LocalDateTime;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NewAppointmentView implements FxView {
	private final Pane node;
	private final LocalDateTime start;
	private Runnable onDelete = () -> {};
	
	public NewAppointmentView(TimeTableAppContext context, CalendarsViewModel calendars, LocalDateTime start) {
		this.start = start;
		
		node = new VBox(
			calendars.getModel().getCalendars().stream()
				.map(cal -> FxUtils.buttonOf(
					context.localized("newappointment").mapStrongly(it -> it + " in " + cal.toString()),
					() -> createAppointment(context, cal))
				)
				.toArray(Button[]::new)
		);
		node.setPadding(new Insets(10));
	}
	
	private void createAppointment(TimeTableAppContext context, CalendarModel calendar) {
		AppointmentModel appointment = new AppointmentModel.Builder("")
			.start(start)
			.end(start.plusHours(1))
			.build();
		node.setPadding(Insets.EMPTY);
		calendar.getAppointments().add(appointment);
		
		AppointmentDetailsView detailsView = new AppointmentDetailsView(calendar.getAppointments(), context, appointment);
		detailsView.setOnDelete(onDelete);
		node.getChildren().setAll(detailsView.getNode());
	}
	
	@Override
	public Pane getNode() { return node; }

	public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
}
