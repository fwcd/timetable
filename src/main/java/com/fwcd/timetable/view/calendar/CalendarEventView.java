package com.fwcd.timetable.view.calendar;

import java.time.format.DateTimeFormatter;

import com.fwcd.timetable.model.calendar.CalendarEventModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CalendarEventView implements FxView {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); 
	private final Pane node;
	
	public CalendarEventView(WeekDayTimeLayouter layouter, CalendarEventModel model) {
		node = new VBox();
		node.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(3), Insets.EMPTY)));
		
		Label nameLabel = new Label();
		nameLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		nameLabel.setText(model.getName());
		node.getChildren().add(nameLabel);
		
		Label timeLabel = new Label();
		timeLabel.setFont(Font.font(11));
		timeLabel.setText(formatter.format(model.getTimeInterval().get().getStart()) + " - " + formatter.format(model.getTimeInterval().get().getEnd()));
		node.getChildren().add(timeLabel);
	}
	
	@Override
	public Pane getNode() { return node; }
}
