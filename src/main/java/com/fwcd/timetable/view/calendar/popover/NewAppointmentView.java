package com.fwcd.timetable.view.calendar.popover;

import java.time.LocalDateTime;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class NewAppointmentView implements FxView {
	private final Pane node;
	private final LocalDateTime start;
	private final CalendarModel calendar;
	
	public NewAppointmentView(TimeTableAppContext context, CalendarModel calendar, LocalDateTime start) {
		this.start = start;
		this.calendar = calendar;
		
		Button newAppointmentButton = FxUtils.buttonOf(context.localized("newappointment"), this::createAppointment);
		node = new Pane(
			newAppointmentButton
		);
	}
	
	private void createAppointment() {
		AppointmentModel appointment = new AppointmentModel.Builder("")
			.start(start)
			.end(start.plusHours(1))
			.build();
		calendar.getAppointments().add(appointment);
		node.getChildren().setAll(new AppointmentDetailsView(appointment).getNode());
	}
	
	@Override
	public Node getNode() { return node; }
}