package com.fwcd.timetable.view.calendar;

import java.time.LocalTime;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.calendar.monthview.MonthView;
import com.fwcd.timetable.view.calendar.weekview.WeekDayTimeLayouter;
import com.fwcd.timetable.view.calendar.weekview.WeekView;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;

public class CalendarsView implements FxView {
	private final TabPane node;
	private final WeekView weekView;
	private final MonthView monthView;
	
	public CalendarsView(TimeTableAppContext context, ObservableList<CalendarModel> model) {
		weekView = new WeekView(model);
		monthView = new MonthView(model);
		
		ScrollPane weekScrollView = new ScrollPane(weekView.getNode());
		FxUtils.setVerticalScrollSpeed(weekScrollView, 2);
		weekScrollView.setFitToWidth(true);
		
		// Automatically scroll to the current time indicator
		WeekDayTimeLayouter layouter = weekView.getDayLayouter();
		double vmax = weekScrollView.getVmax();
		double vmin = weekScrollView.getVmin();
		double normalizedValue = layouter.toPixelY(LocalTime.now()) / layouter.toPixelY(LocalTime.MAX);
		weekScrollView.setVvalue((normalizedValue * (vmax - vmin)) + vmin);
		
		node = new TabPane(
			FxUtils.tabOf(context.localized("week"), weekScrollView),
			FxUtils.tabOf(context.localized("month"), monthView.getNode())
		);
	}
	
	@Override
	public Node getNode() { return node; }
}
