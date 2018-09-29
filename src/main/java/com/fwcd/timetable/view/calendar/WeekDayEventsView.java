package com.fwcd.timetable.view.calendar;

import java.time.LocalDate;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class WeekDayEventsView implements FxView {
	private final WeekDayTimeLayouter layouter;
	private final Pane node;
	private final CalendarModel calendar;
	
	public WeekDayEventsView(WeekDayTimeLayouter layouter, CalendarModel calendar) {
		this.calendar = calendar;
		this.layouter = layouter;
		node = new Pane();
	}
	
	public void setDate(LocalDate date) {
		clear();
		
		calendar.getAppointments().stream()
			.filter(it -> it.occursOn(date))
			.forEach(this::push);
	}
	
	private void push(AppointmentModel appointment) {
		Node view = new CalendarEventView(layouter, appointment).getNode();
		appointment.getStartTime().listenAndFire(start -> AnchorPane.setTopAnchor(view, layouter.toPixelY(start)));
		node.getChildren().add(new AnchorPane(view));
	}
	
	private void clear() {
		node.getChildren().clear();
	}
	
	@Override
	public Node getNode() { return node; }
}
