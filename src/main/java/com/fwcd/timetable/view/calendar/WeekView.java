package com.fwcd.timetable.view.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class WeekView implements FxView {
	private final GridPane node;
	private final List<WeekDayView> days = new ArrayList<>();
	
	public WeekView(CalendarModel calendar) {
		node = new GridPane();
		
		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setVgrow(Priority.ALWAYS);
		node.getRowConstraints().add(rowConstraints);
		
		WeekDayTimeLayouter dayLayouter = new WeekDayTimeLayouter();
		ColumnConstraints timeAxisColConstraints = new ColumnConstraints();
		timeAxisColConstraints.setHgrow(Priority.NEVER);
		node.addColumn(0, new WeekTimeAxisView(dayLayouter).getNode());
		node.getColumnConstraints().add(timeAxisColConstraints);
		
		LocalDate weekStart = LocalDate.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.getValue());
		
		for (int i = 0; i < CalendarConstants.DAYS_OF_WEEK; i++) {
			WeekDayView day = new WeekDayView(dayLayouter, calendar, i);
			ColumnConstraints colConstraints = new ColumnConstraints();
			
			day.setWeekStart(weekStart);
			colConstraints.setHgrow(Priority.ALWAYS);
			node.getColumnConstraints().add(colConstraints);
			days.add(day);
			node.addColumn(i + 1, day.getNode());
		}
	}
	
	@Override
	public Node getNode() { return node; }
}
