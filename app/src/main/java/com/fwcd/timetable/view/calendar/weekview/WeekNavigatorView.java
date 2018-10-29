package com.fwcd.timetable.view.calendar.weekview;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.api.view.FxView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class WeekNavigatorView implements FxView {
	private final HBox node;
	
	public WeekNavigatorView(TimeTableAppContext context, WeekContentView content) {
		Label weekLabel = new Label();
		weekLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
		content.getWeekStart().listenAndFire(it -> weekLabel.setText(describeWeek(it, context.getDateFormatter().get())));
		
		HBox.setMargin(weekLabel, new Insets(0, /* right */ 4, 0, 0));
		
		node = new HBox(
			weekLabel,
			FxUtils.buttonOf("<", content::showPreviousWeek),
			FxUtils.buttonOf(context.localized("today"), content::showCurrentWeek),
			FxUtils.buttonOf(">", content::showNextWeek)
		);
		node.setAlignment(Pos.CENTER);
	}
	
	private String describeWeek(LocalDate weekStart, DateTimeFormatter formatter) {
		return formatter.format(weekStart) + " - " + formatter.format(weekStart.plusDays(CalendarConstants.DAYS_OF_WEEK - 1));
	}
	
	@Override
	public Node getNode() { return node; }
}
