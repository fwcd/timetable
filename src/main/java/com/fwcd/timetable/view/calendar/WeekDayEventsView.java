package com.fwcd.timetable.view.calendar;

import java.time.LocalDate;

import com.fwcd.fructose.structs.ArrayBiList;
import com.fwcd.fructose.structs.BiList;
import com.fwcd.fructose.time.LocalTimeInterval;
import com.fwcd.timetable.model.calendar.CalendarEventModel;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class WeekDayEventsView implements FxView {
	private final WeekDayTimeLayouter layouter;
	private final Pane node;
	private final CalendarModel calendar;
	private final BiList<LocalTimeInterval, HBox> overlapBoxes = new ArrayBiList<>();
	
	public WeekDayEventsView(WeekDayTimeLayouter layouter, CalendarModel calendar) {
		this.calendar = calendar;
		this.layouter = layouter;
		node = new StackPane();
	}
	
	public void setDate(LocalDate date) {
		clear();
		calendar.getAppointments().stream()
			.filter(it -> it.occursOn(date))
			.forEach(it -> addEvent(it, date));
	}
	
	private void addEvent(CalendarEventModel event, LocalDate viewedDate) {
		Pane child = new CalendarEventView(layouter, event).getNode();
		
		event.getTimeInterval().listenAndFire(timeInterval -> {
			boolean isFirstDate = event.beginsOn(viewedDate);
			boolean isLastDate = event.endsOn(viewedDate);
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
		
		// FIXME: Handle multi-day-events properly (getTimeInterval() is not accurate for multi-day-events)
		
		LocalTimeInterval eventInterval = event.getTimeInterval().get();
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
