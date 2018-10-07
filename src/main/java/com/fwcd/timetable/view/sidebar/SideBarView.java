package com.fwcd.timetable.view.sidebar;

import com.fwcd.timetable.model.TimeTableAppModel;
import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.sidebar.calendar.CalendarsSideView;
import com.fwcd.timetable.view.sidebar.task.TasksView;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.control.TabPane;

public class SideBarView implements FxView {
	private final TabPane node;
	
	public SideBarView(TimeTableAppContext context, TimeTableAppModel model) {
		node = new TabPane(
			FxUtils.tabOf(context.localized("calendars"), new CalendarsSideView(context, model)),
			FxUtils.tabOf(context.localized("tasks"), new TasksView(context, model))
		);
	}
	
	@Override
	public Node getNode() { return node; }
}
