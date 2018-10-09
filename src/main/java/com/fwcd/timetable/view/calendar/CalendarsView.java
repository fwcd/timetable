package com.fwcd.timetable.view.calendar;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.calendar.listview.CalendarListView;
import com.fwcd.timetable.view.calendar.monthview.MonthView;
import com.fwcd.timetable.view.calendar.weekview.WeekView;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.view.utils.NavigableTabPane;

import javafx.scene.Node;

public class CalendarsView implements FxView {
	private final Node node;
	private final WeekView weekView;
	private final MonthView monthView;
	private final CalendarListView listView;
	
	public CalendarsView(TimeTableAppContext context, ObservableList<CalendarModel> model) {
		weekView = new WeekView(context, model);
		monthView = new MonthView(model);
		listView = new CalendarListView(model);
		
		NavigableTabPane tabPane = new NavigableTabPane();
		tabPane.addTab(context.localized("week"), weekView);
		tabPane.addTab(context.localized("month"), monthView);
		tabPane.addTab(context.localized("list"), listView);
		node = tabPane.getNode();
	}
	
	@Override
	public Node getNode() { return node; }
}
