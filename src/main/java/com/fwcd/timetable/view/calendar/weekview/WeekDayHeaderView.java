package com.fwcd.timetable.view.calendar.weekview;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class WeekDayHeaderView implements FxView {
	private final Pane node;
	
	private final int dayOffset;
	private final Observable<Option<LocalDate>> date = new Observable<>(Option.empty());
	
	public WeekDayHeaderView(TimeTableAppContext context, CalendarsViewModel calendars, int dayOffset) {
		this.dayOffset = dayOffset;
		DayOfWeek weekDay = DayOfWeek.of(dayOffset + 1);
		
		Label titleLabel = FxUtils.labelOf(context.localized(weekDay.name().toLowerCase()));
		
		node = new VBox(titleLabel);
	}
	
	public void setWeekStart(LocalDate weekStart) {
		date.set(Option.of(weekStart.plusDays(dayOffset)));
	}
	
	public Observable<Option<LocalDate>> getDate() { return date; }
	
	@Override
	public Node getNode() { return node; }
}
