package com.fwcd.timetable.view.calendar;

import java.time.LocalTime;

import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class CalendarView implements FxView {
	private final ScrollPane node;
	private final WeekView week;
	
	public CalendarView(CalendarModel model) {
		week = new WeekView(model);
		node = new ScrollPane(week.getNode());
		
		FxUtils.setVerticalScrollSpeed(node, 2);
		node.setFitToWidth(true);
		node.setVvalue(week.getDayLayouter().toPixelY(LocalTime.now()));
	}
	
	@Override
	public Node getNode() { return node; }
}
