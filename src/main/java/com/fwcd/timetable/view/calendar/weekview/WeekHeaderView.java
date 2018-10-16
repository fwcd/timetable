package com.fwcd.timetable.view.calendar.weekview;

import java.time.DayOfWeek;

import com.fwcd.timetable.model.calendar.CalendarConstants;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.TimeTableAppContext;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class WeekHeaderView implements FxView {
	private final GridPane node;
	
	public WeekHeaderView(TimeTableAppContext context, WeekContentView content) {
		node = new GridPane();
		setupView(context, content.getTimeAxis().getNode().widthProperty(), content.getNode());
	}
	
	private void setupView(TimeTableAppContext context, ReadOnlyDoubleProperty leftPadding, GridPane contentGrid) {
		node.getColumnConstraints().setAll(contentGrid.getColumnConstraints());
		
		Region leftSpacer = new Region();
		leftPadding.addListener((obs, o, n) -> {
			leftSpacer.setPrefWidth(n.doubleValue());
			leftSpacer.requestLayout();
		});
		node.addColumn(0, leftSpacer);
		
		for (int i = 0; i < CalendarConstants.DAYS_OF_WEEK; i++) {
			node.addColumn(i + 1, FxUtils.labelOf(context.localized(DayOfWeek.of(i + 1).name().toLowerCase())));
		}
	}
	
	@Override
	public Node getNode() { return node; }
}
