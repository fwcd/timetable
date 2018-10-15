package com.fwcd.timetable.view.calendar.weekview;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.view.calendar.popover.NewAppointmentView;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
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
	private final CalendarsViewModel calendars;
	
	private final int dayOffset;
	private final Observable<Option<LocalDate>> date = new Observable<>(Option.empty());
	
	public WeekDayView(WeekDayTimeLayouter layouter, TimeTableAppContext context, CalendarsViewModel calendars, int dayOffset) {
		this.dayOffset = dayOffset;
		this.layouter = layouter;
		this.calendars = calendars;
		
		node = new StackPane();
		node.setMinWidth(0);
		node.setBorder(new Border(new BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 1, 0, 1))));
		
		appointments = new WeekDayAppointmentsView(layouter, context, calendars);
		date.listenAndFire(it -> it.ifPresent(appointments::setDate));
		
		node.setOnMouseClicked(e -> {
			NewAppointmentView newAppointmentView = createNewAppointmentView(context, e);
			double yOffset = -30; // TODO: Dynamic calculation of the y-offset
			FxUtils.newPopOver(newAppointmentView.getNode())
				.show(node.getScene().getWindow(), e.getScreenX(), e.getScreenY() + yOffset);
		});
		
		// Add layered nodes
		
		addHourMarks();
		node.getChildren().add(appointments.getNode());
		addTimeIndicator();
	}

	private NewAppointmentView createNewAppointmentView(TimeTableAppContext context, MouseEvent e) {
		LocalDate newDate = date.get().unwrap("Can not create appointment without a date");
		LocalTime newTime = layouter.toTime(e.getY());
		LocalDateTime dateTime = LocalDateTime.of(newDate, newTime);
		return new NewAppointmentView(context, calendars, dateTime);
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
		CurrentTimeIndicator indicator = new CurrentTimeIndicator();
		Pane indicatorNode = indicator.getNode();
		AnchorPane anchor = new AnchorPane(indicatorNode);
		Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> updateIndicatorY(indicatorNode)));
		
		updateIndicatorY(indicatorNode);
		
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		
		date.listenAndFire(it -> {
			anchor.setVisible(it.filter(LocalDate.now()::equals).isPresent());
		});
		
		anchor.setPickOnBounds(false);
		AnchorPane.setLeftAnchor(indicatorNode, 0D);
		AnchorPane.setRightAnchor(indicatorNode, 0D);
		
		node.getChildren().add(anchor);
	}
	
	private void updateIndicatorY(Node indicator) {
		AnchorPane.setTopAnchor(indicator, layouter.toPixelY(LocalTime.now()) - (indicator.getBoundsInParent().getHeight() / 2));
	}
	
	public void setWeekStart(LocalDate weekStart) {
		date.set(Option.of(weekStart.plusDays(dayOffset)));
	}
	
	public Observable<Option<LocalDate>> getDate() { return date; }

	@Override
	public Pane getNode() {
		return node;
	}
}
