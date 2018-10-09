package com.fwcd.timetable.view.calendar.monthview;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.structs.ArrayStack;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.fructose.structs.Stack;
import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class MonthView implements FxView {
	private final GridPane node;
	
	private final ObservableList<CalendarModel> calendars;
	private final Stack<MonthDayView> days = new ArrayStack<>();
	
	private final Observable<YearMonth> month;
	
	public MonthView(ObservableList<CalendarModel> calendars) {
		this.calendars = calendars;
		
		month = new Observable<>(YearMonth.now());
		node = new GridPane();
		
		month.listenAndFire(this::updateView);
	}
	
	private void updateView(YearMonth newMonth) {
		while (!days.isEmpty()) {
			days.pop().close();
		}
		
		node.getChildren().clear();
		
		LocalDate first = newMonth.atDay(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate last = newMonth.atEndOfMonth().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		int i = 0;
		
		for (LocalDate date = first; date.compareTo(last) <= 0; date = date.plusDays(1)) {
			MonthDayView day = new MonthDayView(calendars, date);
			days.push(day);
			
			Node dayNode = day.getNode();
			GridPane.setRowIndex(dayNode, i / CalendarConstants.DAYS_OF_WEEK);
			GridPane.setColumnIndex(dayNode, i % CalendarConstants.DAYS_OF_WEEK);
			
			node.getChildren().add(dayNode);
			i++;
		}
	}
	
	public Observable<YearMonth> getMonth() { return month; }
	
	@Override
	public Node getNode() { return node; }
}
