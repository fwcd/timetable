package com.fwcd.timetable.view.calendar.weekview;

import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class WeekNavigatorView implements FxView {
	private final Pane node;
	
	public WeekNavigatorView(TimeTableAppContext context, WeekContentView contents) {
		node = new HBox(
			FxUtils.buttonOf("<", () -> {}),
			FxUtils.buttonOf(context.localized("today"), () -> {}),
			FxUtils.buttonOf(">", () -> {})
		);
	}
	
	@Override
	public Node getNode() { return node; }
}
