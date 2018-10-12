package com.fwcd.timetable.view.calendar.popover;

import java.time.LocalDateTime;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class NewAppointmentView implements FxView {
	private final Pane node;
	private final LocalDateTime start;
	private final CalendarModel calendar;
	
	public NewAppointmentView(TimeTableAppContext context, CalendarModel calendar, LocalDateTime start) {
		this.start = start;
		this.calendar = calendar;
		
		Button newAppointmentButton = FxUtils.buttonOf(context.localized("newappointment"), () -> createAppointment(context));
		StackPane.setAlignment(newAppointmentButton, Pos.CENTER);
		node = new StackPane(
			newAppointmentButton
		);
		node.setPadding(new Insets(10));
	}
	
	private void createAppointment(TimeTableAppContext context) {
		AppointmentModel appointment = new AppointmentModel.Builder("")
			.start(start)
			.end(start.plusHours(1))
			.build();
		node.setPadding(Insets.EMPTY);
		calendar.getAppointments().add(appointment);
		node.getChildren().setAll(new AppointmentDetailsView(calendar, context, appointment).getNode());
	}
	
	@Override
	public Pane getNode() { return node; }
}
