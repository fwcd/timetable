package com.fwcd.timetable.view.calendar.monthview;

import java.time.LocalDate;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.view.utils.SubscriptionStack;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class MonthDayView implements FxView, AutoCloseable {
	private final BorderPane node;
	private final Label indexLabel;
	
	private final ObservableList<CalendarModel> calendars;
	private final SubscriptionStack calendarSubscriptions = new SubscriptionStack();
	
	public MonthDayView(ObservableList<CalendarModel> calendars, LocalDate date) {
		this.calendars = calendars;
		
		node = new BorderPane();
		
		indexLabel = new Label(Integer.toString(date.getDayOfMonth()));
		indexLabel.setFont(Font.font(14));
		node.setTop(indexLabel);
		
		calendars.listen(it -> updateListenersAndView());
	}
	
	private void updateListenersAndView() {
		calendarSubscriptions.unsubscribeAll();
		calendarSubscriptions.subscribeAll(calendars, CalendarModel::getAppointments, it -> updateView());
		updateView();
	}
	
	private void updateView() {
		// TODO
	}
	
	@Override
	public void close() {
		calendarSubscriptions.unsubscribeAll();
	}
	
	@Override
	public Node getNode() { return node; }
}
