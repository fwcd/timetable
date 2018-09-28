package com.fwcd.timetable.view.calendar;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.view.utils.FxView;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class WeekTimeAxisView implements FxView {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); 
	private final StackPane pane;
	
	public WeekTimeAxisView(WeekDayTimeLayouter layouter) {
		pane = new StackPane();
		pane.setPadding(new Insets(5, 5, 5, 5));
		
		for (int hour = 0; hour < CalendarConstants.HOURS_OF_DAY; hour++) {
			LocalTime time = LocalTime.of(hour, 0);
			Label label = new Label(time.format(formatter));
			AnchorPane.setTopAnchor(label, layouter.toPixelY(time));
			pane.getChildren().add(new AnchorPane(label));
		}
	}
	
	@Override
	public Node getNode() { return pane; }
}
