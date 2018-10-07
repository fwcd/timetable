package com.fwcd.timetable.view.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

public class WeekView implements FxView {
	private static final int MIN_DAY_WIDTH = 40;
	private final GridPane node;
	private final List<WeekDayView> days = new ArrayList<>();
	private final WeekDayTimeLayouter dayLayouter = new WeekDayTimeLayouter();
	
	public WeekView(ObservableList<CalendarModel> calendars) {
		node = new GridPane();
		node.setMinWidth(Region.USE_PREF_SIZE);
		
		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setVgrow(Priority.ALWAYS);
		node.getRowConstraints().add(rowConstraints);
		
		ColumnConstraints timeAxisColConstraints = new ColumnConstraints();
		timeAxisColConstraints.setHgrow(Priority.NEVER);
		node.addColumn(0, new WeekTimeAxisView(dayLayouter).getNode());
		node.getColumnConstraints().add(timeAxisColConstraints);
		
		LocalDate weekStart = LocalDate.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.getValue());
		
		for (int i = 0; i < CalendarConstants.DAYS_OF_WEEK; i++) {
			WeekDayView day = new WeekDayView(dayLayouter, calendars, i);
			day.setWeekStart(weekStart);
			
			ColumnConstraints colConstraints = new ColumnConstraints();
			colConstraints.setMinWidth(MIN_DAY_WIDTH);
			colConstraints.setPrefWidth(MIN_DAY_WIDTH);
			colConstraints.setHgrow(Priority.ALWAYS);
			node.getColumnConstraints().add(colConstraints);
			
			days.add(day);
			node.addColumn(i + 1, day.getNode());
		}
	}
	
	public WeekDayTimeLayouter getDayLayouter() { return dayLayouter; }
	
	@Override
	public Node getNode() { return node; }
}
