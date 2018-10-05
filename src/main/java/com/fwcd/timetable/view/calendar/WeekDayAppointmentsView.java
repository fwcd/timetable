package com.fwcd.timetable.view.calendar;

import java.time.LocalDate;

import com.fwcd.fructose.structs.ArrayBiList;
import com.fwcd.fructose.structs.BiList;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.fructose.time.LocalTimeInterval;
import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class WeekDayAppointmentsView implements FxView {
	private final WeekDayTimeLayouter layouter;
	private final Pane node;
	private final ObservableList<CalendarModel> calendars;
	private final BiList<LocalTimeInterval, HBox> overlapBoxes = new ArrayBiList<>();
	
	public WeekDayAppointmentsView(WeekDayTimeLayouter layouter, ObservableList<CalendarModel> calendars) {
		this.calendars = calendars;
		this.layouter = layouter;
		node = new StackPane();
	}
	
	public void setDate(LocalDate date) {
		clear();
		calendars.stream()
			.flatMap(it -> it.getAppointments().stream())
			.filter(it -> it.occursOn(date))
			.forEach(it -> addEvent(it, date));
	}
	
	private void addEvent(AppointmentModel event, LocalDate viewedDate) {
		Pane child = new AppointmentView(layouter, event).getNode();
		
		event.getDateTimeInterval().listenAndFire(t -> {
			LocalTimeInterval interval = event.getTimeIntervalOn(viewedDate);
			AnchorPane.setTopAnchor(child, layouter.toPixelY(interval.getStart()));
			child.setPrefHeight(layouter.toPixelHeight(interval.getDuration()));
		});
		
		LocalTimeInterval eventInterval = event.getTimeIntervalOn(viewedDate);
		HBox overlappingBox = null;
		int overlapBoxCount = overlapBoxes.size();
		
		for (int i = 0; i < overlapBoxCount; i++) {
			LocalTimeInterval interval = overlapBoxes.getA(i);
			HBox box = overlapBoxes.getB(i);
			
			if (interval.overlaps(eventInterval)) {
				overlapBoxes.setA(i, interval.merge(eventInterval));
				overlappingBox = box;
				break;
			}
		}
		
		if (overlappingBox == null) {
			overlappingBox = new HBox();
			overlapBoxes.add(eventInterval, overlappingBox);
			node.getChildren().add(overlappingBox);
		}
		
		overlappingBox.getChildren().add(new AnchorPane(child));
	}
	
	private void clear() {
		node.getChildren().clear();
	}
	
	@Override
	public Node getNode() { return node; }
}
