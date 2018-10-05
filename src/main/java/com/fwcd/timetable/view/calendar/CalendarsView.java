package com.fwcd.timetable.view.calendar;

import java.time.LocalTime;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class CalendarsView implements FxView {
	private final ScrollPane node;
	private final WeekView week;
	
	public CalendarsView(ObservableList<CalendarModel> model) {
		week = new WeekView(model);
		node = new ScrollPane(week.getNode());
		
		FxUtils.setVerticalScrollSpeed(node, 2);
		node.setFitToWidth(true);
		
		WeekDayTimeLayouter layouter = week.getDayLayouter();
		double vmax = node.getVmax();
		double vmin = node.getVmin();
		double normalizedValue = layouter.toPixelY(LocalTime.now()) / layouter.toPixelY(LocalTime.MAX);
		node.setVvalue((normalizedValue * (vmax - vmin)) + vmin);
	}
	
	@Override
	public Node getNode() { return node; }
}
