package com.fwcd.timetable.view.calendar;

import java.time.LocalDate;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class WeekDayView implements FxView {
	private static final Color BORDER_COLOR = new Color(0.9, 0.9, 0.9, 1);
	private final StackPane node;
	private final int dayOffset;
	private Option<LocalDate> weekStart = Option.empty();
	
	public WeekDayView(CalendarModel calendar, int dayOffset) {
		node = new StackPane();
		this.dayOffset = dayOffset;
		
		node.getChildren().add(new WeekDayAppointmentsOverlay().getNode());
		node.setBorder(new Border(new BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 1, 0, 1))));
	}
	
	public void setWeekStart(LocalDate weekStart) {
		this.weekStart = Option.of(weekStart);
	}

	@Override
	public Node getNode() {
		return node;
	}
}
