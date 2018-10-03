package com.fwcd.timetable.view.calendar;

import java.time.format.DateTimeFormatter;

import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.timetable.model.calendar.CalendarEventModel;
import com.fwcd.timetable.model.calendar.CommonEventType;
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
		colorOf(model).listenAndFire(it -> node.setBackground(new Background(new BackgroundFill(it, new CornerRadii(3), Insets.EMPTY))));
		
		Label nameLabel = new Label();
		nameLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		model.getName().listenAndFire(nameLabel::setText);
		node.getChildren().add(nameLabel);
		
		Label timeLabel = new Label();
		timeLabel.setFont(Font.font(11));
		model.getTimeInterval().listenAndFire(it -> timeLabel.setText(formatter.format(it.getStart()) + " - " + formatter.format(it.getEnd())));
		node.getChildren().add(timeLabel);
	}
	
	private ReadOnlyObservable<Color> colorOf(CalendarEventModel model) {
		return model.getType().mapStrongly(type -> {
			switch (type) {
				case CommonEventType.APPOINTMENT: return Color.LIGHTSALMON;
				case CommonEventType.TIME_TABLE_ENTRY: return Color.AQUA;
				default: return Color.LIGHTGRAY;
			}
		});
	}
	
	@Override
	public Pane getNode() { return node; }
}
