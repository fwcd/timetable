package com.fwcd.timetable.view.calendar.weekview;

import java.time.LocalDate;

import com.fwcd.fructose.Option;
import com.fwcd.fructose.structs.ArrayBiList;
import com.fwcd.fructose.structs.BiList;
import com.fwcd.fructose.time.LocalTimeInterval;
import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarCrateModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class WeekDayAppointmentsView implements FxView {
	private final WeekDayTimeLayouter layouter;
	private final Pane node;
	
	private final CalendarCrateModel calendars;
	
	private final BiList<LocalTimeInterval, StackPane> overlapBoxes = new ArrayBiList<>();
	private Option<LocalDate> currentDate = Option.empty();
	
	public WeekDayAppointmentsView(WeekDayTimeLayouter layouter, CalendarCrateModel calendars) {
		this.layouter = layouter;
		this.calendars = calendars;
		
		node = new StackPane();
		node.setPickOnBounds(false);
		
		calendars.getChangeListeners().add(it -> { updateView(); });
	}
	
	public void setDate(LocalDate date) {
		currentDate = Option.of(date);
		updateView();
	}
	
	private static class AppointmentWithCalendar {
		AppointmentModel appointment;
		CalendarModel calendar;
		
		public AppointmentWithCalendar(AppointmentModel appointment, CalendarModel calendar) {
			this.appointment = appointment;
			this.calendar = calendar;
		}
	}

	private void updateView() {
		clear();
		currentDate.ifPresent(date -> calendars.getCalendars().stream()
			.flatMap(cal -> cal.getAppointments().stream().map(app -> new AppointmentWithCalendar(app, cal)))
			.filter(it -> it.appointment.occursOn(date))
			.forEach(it -> add(it, date)));
	}

	private void add(AppointmentWithCalendar appWithCal, LocalDate viewedDate) {
		AppointmentModel appointment = appWithCal.appointment;
		Pane child = new AppointmentView(layouter, appointment, FxUtils.toFxColor(appWithCal.calendar.getColor().get())).getNode();
		
		LocalTimeInterval eventInterval = appointment.getTimeIntervalOn(viewedDate);
		AnchorPane.setTopAnchor(child, layouter.toPixelY(eventInterval.getStart()));
		child.setPrefHeight(layouter.toPixelHeight(eventInterval.getDuration()));
		
		StackPane overlappingBox = null;
		int overlapBoxCount = overlapBoxes.size();
		
		for (int i = 0; i < overlapBoxCount; i++) {
			LocalTimeInterval interval = overlapBoxes.getA(i);
			StackPane box = overlapBoxes.getB(i);
			
			if (interval.overlaps(eventInterval)) {
				overlapBoxes.setA(i, interval.merge(eventInterval));
				overlappingBox = box;
				break;
			}
		}
		
		if (overlappingBox == null) {
			overlappingBox = new StackPane();
			overlappingBox.setPickOnBounds(false);
			
			overlapBoxes.add(eventInterval, overlappingBox);
			node.getChildren().add(overlappingBox);
		}
		
		int overlaps = overlappingBox.getChildren().size();
		double indent = overlaps * 10;
		AnchorPane anchor = new AnchorPane(child);
		anchor.setPickOnBounds(false);
		AnchorPane.setLeftAnchor(child, indent);
		overlappingBox.getChildren().add(anchor);
	}
	
	private void clear() {
		node.getChildren().clear();
		overlapBoxes.clear();
	}
	
	@Override
	public Node getNode() { return node; }
}
