package com.fwcd.timetable.view;

import com.fwcd.timetable.view.calendar.CalendarView;
import com.fwcd.timetable.view.sidebar.SideBarView;
import com.fwcd.timetable.view.utils.CloseableView;
import com.fwcd.timetable.view.utils.FxParentView;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class TimeTableAppView implements FxParentView {
	private final BorderPane node;
	private final TimeTableAppContext context = new TimeTableAppContext();
	
	public TimeTableAppView() {
		node = new BorderPane();
		node.setTop(new MenuBarView(context).getNode());
		node.setCenter(new CalendarView(context.getModel().getCalendar()).getNode());
		node.setRight(new CloseableView(new SideBarView()).getNode());
	}
	
	@Override
	public Parent getNode() { return node; }
}
