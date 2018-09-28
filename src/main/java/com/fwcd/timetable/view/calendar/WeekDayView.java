package com.fwcd.timetable.view.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.model.calendar.CalendarModel;
import com.fwcd.timetable.view.utils.FxView;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class WeekDayView implements FxView {
	private static final Color BORDER_COLOR = new Color(0.9, 0.9, 0.9, 1);
	private final StackPane node;
	private final WeekDayAppointmentsView appointments;
	private final WeekDayTimeLayouter layouter;
	
	private final int dayOffset;
	private Observable<Option<LocalDate>> currentDate = new Observable<>(Option.empty());
	
	public WeekDayView(WeekDayTimeLayouter layouter, CalendarModel calendar, int dayOffset) {
		this.dayOffset = dayOffset;
		this.layouter = layouter;
		
		node = new StackPane();
		node.setBorder(new Border(new BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 1, 0, 1))));
		
		appointments = new WeekDayAppointmentsView(layouter, calendar);
		
		// Add layered nodes
		
		addHourMarks();
		node.getChildren().add(appointments.getNode());
		addTimeIndicator();
	}
	
	private void addHourMarks() {
		Pane hourMarks = new Pane();
		
		for (int hour = 0; hour < CalendarConstants.HOURS_OF_DAY; hour++) {
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
	
	private void addTimeIndicator() {
		AnchorPane anchor = new AnchorPane();
		Node indicator = new CurrentTimeIndicator().getNode();
		Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> updateIndicatorY(indicator)));
		
		updateIndicatorY(indicator);
		
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		
		currentDate.listenAndFire(d -> {
			if (currentDate.get().filter(LocalDate.now()::equals).isPresent()) {
				anchor.getChildren().add(indicator);
			} else {
				anchor.getChildren().clear();
			}
		});
		
		GridPane.setFillWidth(anchor, true);
		AnchorPane.setLeftAnchor(indicator, 0D);
		AnchorPane.setRightAnchor(indicator, 0D);
		
		node.getChildren().add(anchor);
	}
	
	private void updateIndicatorY(Node indicator) {
		AnchorPane.setTopAnchor(indicator, layouter.toPixelY(LocalTime.now()) - (indicator.getBoundsInParent().getHeight() / 2));
	}
	
	public void setWeekStart(LocalDate weekStart) {
		setDate(weekStart.plusDays(dayOffset));
	}
	
	private void setDate(LocalDate date) {
		currentDate.set(Option.of(date));
		appointments.setDate(date);
	}

	@Override
	public Node getNode() {
		return node;
	}
}
