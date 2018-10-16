package com.fwcd.timetable.view.calendar.weekview;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import com.fwcd.fructose.Observable;
import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.view.utils.FxHeaderedView;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

public class WeekContentView implements FxView, FxHeaderedView {
	private static final int MIN_DAY_WIDTH = 30;
	private final GridPane node;
	private final GridPane headerNode;
	private final WeekTimeAxisView timeAxis;
	private final List<WeekDayView> days = new ArrayList<>();
	private final Observable<LocalDate> weekStart;
	
	private final WeekDayTimeLayouter timeLayouter;
	private final CalendarsViewModel calendars;
	
	public WeekContentView(WeekDayTimeLayouter timeLayouter, TimeTableAppContext context, CalendarsViewModel calendars) {
		this.timeLayouter = timeLayouter;
		this.calendars = calendars;
		
		node = new GridPane();
		node.setMinWidth(Region.USE_PREF_SIZE);
		
		headerNode = new GridPane();
		
		weekStart = new Observable<>(currentWeekStart());
		weekStart.listen(this::updateWeekStart);
		
		timeAxis = new WeekTimeAxisView(context, timeLayouter);
		setupView(context);
	}
	
	private void setupView(TimeTableAppContext context) {
		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setVgrow(Priority.ALWAYS);
		node.getRowConstraints().add(rowConstraints);
		
		ColumnConstraints timeAxisColConstraints = new ColumnConstraints();
		timeAxisColConstraints.setHgrow(Priority.NEVER);
		
		Region cornerSpacer = new Region();
		timeAxis.getNode().widthProperty().addListener((obs, o, n) -> {
			Platform.runLater(() -> cornerSpacer.setMinWidth(n.doubleValue()));
		});
		headerNode.addColumn(0, cornerSpacer);
		headerNode.getColumnConstraints().add(new ColumnConstraints());
		
		node.addColumn(0, timeAxis.getNode());
		node.getColumnConstraints().add(timeAxisColConstraints);
		
		for (int i = 0; i < CalendarConstants.DAYS_OF_WEEK; i++) {
			ColumnConstraints colConstraints = new ColumnConstraints();
			colConstraints.setMinWidth(MIN_DAY_WIDTH);
			colConstraints.setPrefWidth(MIN_DAY_WIDTH);
			colConstraints.setHgrow(Priority.ALWAYS);
			node.getColumnConstraints().add(colConstraints);
			headerNode.getColumnConstraints().add(colConstraints);
			
			WeekDayView day = new WeekDayView(timeLayouter, context, calendars, i);
			day.setWeekStart(weekStart.get());
			days.add(day);
			node.addColumn(i + 1, day.getNode());
			
			WeekDayHeaderView dayHeader = new WeekDayHeaderView(context, calendars, i);
			dayHeader.setWeekStart(weekStart.get());
			headerNode.addColumn(i + 1, dayHeader.getNode());
		}
	}
	
	private void updateWeekStart(LocalDate start) {
		for (WeekDayView day : days) {
			day.setWeekStart(start);
		}
	}
	
	public Observable<LocalDate> getWeekStart() { return weekStart; }
	
	public void adjustWeekStart(TemporalAdjuster adjuster) { weekStart.set(weekStart.get().with(adjuster)); }
	
	public void showNextWeek() { adjustWeekStart(TemporalAdjusters.next(DayOfWeek.MONDAY)); }
	
	public void showCurrentWeek() { weekStart.set(currentWeekStart()); }
	
	public void showPreviousWeek() { adjustWeekStart(TemporalAdjusters.previous(DayOfWeek.MONDAY)); }

	private LocalDate currentWeekStart() { return LocalDate.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.getValue()); }
	
	public WeekTimeAxisView getTimeAxis() { return timeAxis; }
	
	@Override
	public GridPane getNode() { return node; }

	@Override
	public Node getHeaderNode() { return headerNode; }
}
