package fwcd.timetable.view.calendar.weekview;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import fwcd.fructose.Observable;
import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarConstants;
import fwcd.timetable.view.calendar.popover.NewAppointmentView;
import fwcd.timetable.view.utils.FxUtils;
import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

import org.controlsfx.control.PopOver;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class WeekDayView implements FxView {
	private final StackPane node;
	private final WeekDayEntriesView appointments;
	private final WeekDayTimeLayouter layouter;
	private final CalendarCrateViewModel calendars;
	
	private final int dayOffset;
	private final Observable<Option<LocalDate>> date = new Observable<>(Option.empty());
	
	public WeekDayView(WeekDayTimeLayouter layouter, TimeTableAppContext context, CalendarCrateViewModel calendars, int dayOffset) {
		this.dayOffset = dayOffset;
		this.layouter = layouter;
		this.calendars = calendars;
		
		node = new StackPane();
		node.setMinWidth(0);
		node.getStyleClass().add("week-day");
		
		appointments = new WeekDayEntriesView(layouter, context, calendars);
		date.listenAndFire(it -> it.ifPresent(appointments::setDate));
		
		node.setOnMouseClicked(e -> {
			NewAppointmentView newAppointmentView = createNewAppointmentView(context, e);
			double yOffset = -30; // TODO: Dynamic calculation of the y-offset
			PopOver popOver = FxUtils.newPopOver(newAppointmentView.getNode());
			
			newAppointmentView.setOnDelete(popOver::hide);
			popOver.show(node.getScene().getWindow(), e.getScreenX(), e.getScreenY() + yOffset);
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
		return new NewAppointmentView(context.getLocalizer(), context.getFormatters(), calendars, dateTime);
	}
	
	private void addHourMarks() {
		Pane hourMarks = new Pane();
		
		for (int hour = 0; hour < CalendarConstants.HOURS_OF_DAY; hour++) {
			double y = layouter.toPixelY(LocalTime.of(hour, 0));
			Line mark = new Line();
			mark.setStartY(y);
			mark.setEndY(y);
			mark.endXProperty().bind(node.widthProperty());
			mark.getStyleClass().add("hour-mark");
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
		
		anchor.setPickOnBounds(false);
		AnchorPane.setLeftAnchor(indicatorNode, 0D);
		AnchorPane.setRightAnchor(indicatorNode, 0D);
		
		node.getChildren().add(anchor);
	}
	
	private void updateIndicatorY(Node indicator) {
		AnchorPane.setTopAnchor(indicator, layouter.toPixelY(LocalTime.now()) - (indicator.getBoundsInParent().getHeight() / 2));
		date.listenAndFire(it -> {
			indicator.setVisible(it.filter(LocalDate.now()::equals).isPresent());
		});
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
