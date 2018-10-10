package com.fwcd.timetable.view.calendar.weekview;

import java.time.format.DateTimeFormatter;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.model.calendar.CommonEntryType;
import com.fwcd.timetable.view.calendar.weekview.WeekDayTimeLayouter;
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

public class AppointmentView implements FxView {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); 
	private final Pane node;
	
	public AppointmentView(WeekDayTimeLayouter layouter, AppointmentModel model) {
		node = new VBox();
		node.setBackground(new Background(new BackgroundFill(colorOf(model), new CornerRadii(3), Insets.EMPTY)));
		node.getStyleClass().add("appointment");
		
		Label nameLabel = new Label();
		nameLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		model.getName().listenAndFire(nameLabel::setText);
		node.getChildren().add(nameLabel);
		
		Label timeLabel = new Label();
		timeLabel.setFont(Font.font(11));
		model.getDateTimeInterval().listenAndFire(it -> timeLabel.setText(TIME_FORMATTER.format(it.getStart()) + " - " + TIME_FORMATTER.format(it.getEnd())));
		node.getChildren().add(timeLabel);
	}
	
	private Color colorOf(AppointmentModel model) {
		// TODO: Use calendar color as appointment color
		switch (model.getType()) {
			case CommonEntryType.APPOINTMENT: return Color.LIGHTSALMON;
			case CommonEntryType.TIME_TABLE_ENTRY: return Color.AQUA;
			default: return Color.LIGHTGRAY;
		}
	}
	
	@Override
	public Pane getNode() { return node; }
}
