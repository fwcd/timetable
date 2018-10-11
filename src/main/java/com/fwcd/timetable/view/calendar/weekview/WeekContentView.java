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
import com.fwcd.timetable.model.calendar.CalendarCrateModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

public class WeekContentView implements FxView {
	private static final int MIN_DAY_WIDTH = 30;
	private final GridPane node;
	private final List<WeekDayView> days = new ArrayList<>();
	private final Observable<LocalDate> weekStart;
	
	private final WeekDayTimeLayouter timeLayouter;
	private final CalendarCrateModel calendars;
	
	public WeekContentView(WeekDayTimeLayouter timeLayouter, TimeTableAppContext context, CalendarCrateModel calendars) {
		this.timeLayouter = timeLayouter;
		this.calendars = calendars;
		
		node = new GridPane();
		node.setMinWidth(Region.USE_PREF_SIZE);
		
		weekStart = new Observable<>(currentWeekStart());
		weekStart.listen(this::updateWeekStart);
		setupView(context);
	}
	
	private void setupView(TimeTableAppContext context) {
		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setVgrow(Priority.ALWAYS);
		node.getRowConstraints().add(rowConstraints);
		
		ColumnConstraints timeAxisColConstraints = new ColumnConstraints();
		timeAxisColConstraints.setHgrow(Priority.NEVER);
		node.addColumn(0, new WeekTimeAxisView(timeLayouter).getNode());
		node.getColumnConstraints().add(timeAxisColConstraints);
		
		for (int i = 0; i < CalendarConstants.DAYS_OF_WEEK; i++) {
			WeekDayView day = new WeekDayView(timeLayouter, context, calendars, i);
			day.setWeekStart(weekStart.get());
			
			ColumnConstraints colConstraints = new ColumnConstraints();
			colConstraints.setMinWidth(MIN_DAY_WIDTH);
			colConstraints.setPrefWidth(MIN_DAY_WIDTH);
			colConstraints.setHgrow(Priority.ALWAYS);
			node.getColumnConstraints().add(colConstraints);
			
			days.add(day);
			node.addColumn(i + 1, day.getNode());
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
	
	@Override
	public Node getNode() { return node; }
}
