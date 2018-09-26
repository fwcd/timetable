package com.fwcd.timetable.view;

import com.fwcd.timetable.view.utils.FxView;
import com.jibbow.fastis.WeekView;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class TimeTableAppView implements FxView {
	private final Pane node;
	
	public TimeTableAppView() {
		node = new WeekView();
	}
	
	@Override
	public Parent getNode() { return node; }
}
