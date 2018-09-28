package com.fwcd.timetable.view.calendar;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import com.fwcd.timetable.model.calendar.AppointmentModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.geometry.Insets;
import javafx.scene.Node;
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
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); 
	private final Pane node;
	
	public AppointmentView(WeekDayTimeLayouter layouter, AppointmentModel model) {
		node = new VBox();
		node.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(3), Insets.EMPTY)));
		
		Label nameLabel = new Label();
		nameLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		nameLabel.setText(model.getName());
		node.getChildren().add(nameLabel);
		
		Label timeLabel = new Label();
		timeLabel.setFont(Font.font(11));
		timeLabel.setText(formatter.format(model.getStart().get()) + " - " + formatter.format(model.getEnd().get()));
		node.getChildren().add(timeLabel);
		
		model.getEnd().listenAndFire(end -> node.setPrefHeight(layouter.toPixelHeight(Duration.between(model.getStart().get(), end))));
	}
	
	@Override
	public Node getNode() { return node; }
}
