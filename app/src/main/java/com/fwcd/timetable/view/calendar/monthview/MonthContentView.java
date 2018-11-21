package com.fwcd.timetable.view.calendar.monthview;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.structs.ArrayStack;
import com.fwcd.fructose.structs.Stack;
import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.view.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class MonthContentView implements FxView {
	private final TimeTableAppContext context;
	private final GridPane node;
	
	private final CalendarsViewModel calendars;
	private final Stack<MonthDayView> days = new ArrayStack<>();
	
	private final Observable<YearMonth> month;
	
	public MonthContentView(TimeTableAppContext context, CalendarsViewModel calendars) {
		this.calendars = calendars;
		this.context = context;
		
		node = new GridPane();
		
		month = new Observable<>(YearMonth.now());
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
			MonthDayView day = new MonthDayView(context, calendars, date);
			days.push(day);
			
			Node dayNode = day.getNode();
			GridPane.setHgrow(dayNode, Priority.ALWAYS);
			GridPane.setVgrow(dayNode, Priority.ALWAYS);
			GridPane.setRowIndex(dayNode, i / CalendarConstants.DAYS_OF_WEEK);
			GridPane.setColumnIndex(dayNode, i % CalendarConstants.DAYS_OF_WEEK);
			
			node.getChildren().add(dayNode);
			i++;
		}
	}
	
	public Observable<YearMonth> getMonth() { return month; }
	
	public void showNextMonth() { month.set(month.get().plusMonths(1)); }
	
	public void showCurrentMonth() { month.set(YearMonth.now()); }
	
	public void showPreviousMonth() { month.set(month.get().minusMonths(1)); }
	
	@Override
	public Node getNode() { return node; }
}
