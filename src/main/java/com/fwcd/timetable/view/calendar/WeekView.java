package com.fwcd.timetable.view.calendar;

import java.util.ArrayList;
import java.util.List;

import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class WeekView implements FxView {
	private static final int DAYS_OF_WEEK = 7;
	private final GridPane node;
	private final List<WeekDayView> days = new ArrayList<>();
	
	public WeekView(CalendarModel calendar) {
		node = new GridPane();
		
		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setVgrow(Priority.ALWAYS);
		node.getRowConstraints().add(rowConstraints);
		
		WeekDayLayouter dayLayouter = new WeekDayLayouter();
		node.addColumn(0, new WeekTimeAxisView(dayLayouter).getNode());
		
		for (int i=0; i<DAYS_OF_WEEK; i++) {
			WeekDayView day = new WeekDayView(dayLayouter, calendar, i);
			ColumnConstraints colConstraints = new ColumnConstraints();
			colConstraints.setHgrow(Priority.ALWAYS);
			node.getColumnConstraints().add(colConstraints);
			days.add(day);
			node.addColumn(i + 1, day.getNode());
		}
	}
	
	@Override
	public Node getNode() { return node; }
}
