package com.fwcd.timetable.view.calendar.monthview;

import java.time.LocalDate;
import java.util.stream.Collectors;

import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.view.utils.SubscriptionStack;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonthDayView implements FxView, AutoCloseable {
	private final BorderPane node;
	private final Label indexLabel;
	private final VBox content;
	
	private final ObservableList<CalendarModel> calendars;
	private final SubscriptionStack calendarSubscriptions = new SubscriptionStack();
	
	public MonthDayView(ObservableList<CalendarModel> calendars, LocalDate date) {
		this.calendars = calendars;
		
		node = new BorderPane();
		
		indexLabel = new Label(Integer.toString(date.getDayOfMonth()));
		indexLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
		BorderPane.setAlignment(indexLabel, Pos.TOP_CENTER);
		node.setTop(indexLabel);
		
		content = new VBox();
		node.setCenter(content);
		
		calendars.listen(it -> updateListenersAndView());
	}
	
	private void updateListenersAndView() {
		calendarSubscriptions.unsubscribeAll();
		calendarSubscriptions.subscribeAll(calendars, CalendarModel::getAppointments, it -> updateView());
		updateView();
	}
	
	private void updateView() {
		content.getChildren().setAll(calendars.stream()
			.flatMap(it -> it.getAppointments().stream())
			.map(it -> new Label(it.getName().get()))
			.collect(Collectors.toList())
		);
	}
	
	@Override
	public void close() {
		calendarSubscriptions.unsubscribeAll();
	}
	
	@Override
	public Node getNode() { return node; }
}
