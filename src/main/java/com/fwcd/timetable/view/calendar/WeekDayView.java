package com.fwcd.timetable.view.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class WeekDayView implements FxView {
	private static final Color BORDER_COLOR = new Color(0.9, 0.9, 0.9, 1);
	private final StackPane node;
	private final WeekDayTimeLayouter layouter;
	
	private final int dayOffset;
	private Option<LocalDate> weekStart = Option.empty();
	
	public WeekDayView(WeekDayTimeLayouter layouter, CalendarModel calendar, int dayOffset) {
		this.dayOffset = dayOffset;
		this.layouter = layouter;
		
		node = new StackPane();
		node.setBorder(new Border(new BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 1, 0, 1))));
		
		addHourMarks();
	}
	
	private void addHourMarks() {
		Pane hourMarks = new Pane();
		
		for (int hour=0; hour<CalendarConstants.HOURS_OF_DAY; hour++) {
			double y = layouter.toPixelY(LocalTime.of(hour, 0));
			Line mark = new Line();
			mark.setStroke(BORDER_COLOR);
			mark.setStartY(y);
			mark.setEndY(y);
			mark.endXProperty().bind(node.widthProperty());
			hourMarks.getChildren().add(mark);
		}
		
		node.getChildren().add(hourMarks);
	}
	
	public void setWeekStart(LocalDate weekStart) {
		this.weekStart = Option.of(weekStart);
	}

	@Override
	public Node getNode() {
		return node;
	}
}
