package com.fwcd.timetable.view.sidebar;

import com.fwcd.timetable.view.TimeTableAppContext;
import com.fwcd.timetable.view.sidebar.calendar.CalendarsSideView;
import com.fwcd.timetable.view.sidebar.task.TasksView;
import com.fwcd.timetable.view.utils.FxUtils;
import com.fwcd.timetable.view.utils.FxView;
import com.fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.scene.Node;
import javafx.scene.control.TabPane;

public class SideBarView implements FxView {
	private final TabPane node;
	
	public SideBarView(TimeTableAppContext context, CalendarsViewModel viewModel) {
		node = new TabPane(
			FxUtils.tabOf(context.localized("calendars"), new CalendarsSideView(context, viewModel)),
			FxUtils.tabOf(context.localized("tasks"), new TasksView(context, viewModel))
		);
	}
	
	@Override
	public Node getNode() { return node; }
}
