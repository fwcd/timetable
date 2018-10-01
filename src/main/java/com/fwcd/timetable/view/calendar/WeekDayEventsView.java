package com.fwcd.timetable.view.calendar;

import java.time.LocalDate;

import com.fwcd.fructose.time.LocalDateInterval;
import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.model.utils.DateUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class WeekDayEventsView implements FxView {
	private final WeekDayTimeLayouter layouter;
	private final Pane node;
	private final CalendarModel calendar;
	
	public WeekDayEventsView(WeekDayTimeLayouter layouter, CalendarModel calendar) {
		this.calendar = calendar;
		this.layouter = layouter;
		node = new StackPane();
	}
	
	public void setDate(LocalDate date) {
		clear();
		
		calendar.getAppointments().stream()
			.filter(it -> it.occursOn(date))
			.sorted()
			.forEach(it -> push(it, date));
	}
	
	private void push(AppointmentModel appointment, LocalDate viewedDate) {
		Pane child = new CalendarEventView(layouter, appointment).getNode();
		appointment.getTimeInterval().listenAndFire(timeInterval -> {
			LocalDateInterval dateInterval = appointment.getDateInterval().get();
			
			boolean isFirstDate = viewedDate.equals(DateUtils.firstDateIn(dateInterval));
			boolean isLastDate = viewedDate.equals(DateUtils.lastDateIn(dateInterval));
			AnchorPane.clearConstraints(child);
			
			if (isFirstDate && isLastDate) {
				AnchorPane.setTopAnchor(child, layouter.toPixelY(timeInterval.getStart()));
				child.setPrefHeight(layouter.toPixelHeight(timeInterval.getDuration()));
			} else if (isFirstDate) {
				AnchorPane.setTopAnchor(child, layouter.toPixelY(timeInterval.getStart()));
				AnchorPane.setBottomAnchor(child, 0D);
			} else if (isLastDate) {
				AnchorPane.setTopAnchor(child, 0D);
				child.setPrefHeight(layouter.toPixelY(timeInterval.getEnd()));
			} else {
				AnchorPane.setTopAnchor(child, 0D);
				AnchorPane.setBottomAnchor(child, 0D);
			}
		});
		node.getChildren().add(new AnchorPane(child));
	}
	
	private void clear() {
		node.getChildren().clear();
	}
	
	@Override
	public Node getNode() { return node; }
}
