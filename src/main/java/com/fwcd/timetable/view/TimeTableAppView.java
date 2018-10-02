package com.fwcd.timetable.view;

import com.fwcd.timetable.model.TimeTableAppModel;
import com.fwcd.timetable.view.calendar.CalendarView;
import com.fwcd.timetable.view.sidebar.SideBarView;
import com.fwcd.timetable.view.utils.FxParentView;

import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

public class TimeTableAppView implements FxParentView {
	private final BorderPane node;
	private final TimeTableAppContext context = new TimeTableAppContext();
	private final TimeTableAppModel model = new TimeTableAppModel();
	
	public TimeTableAppView() {
		node = new BorderPane();
		
		CalendarView calendar = new CalendarView(model.getCalendar());
		SideBarView sideBar = new SideBarView(context, model);
		SplitPane split = new SplitPane(
			calendar.getNode(),
			sideBar.getNode()
		);
		SplitPane.setResizableWithParent(sideBar.getNode(), false);
		split.setOrientation(Orientation.HORIZONTAL);
		split.setDividerPositions(0.7);
		
		node.setTop(new MenuBarView(context).getNode());
		node.setCenter(split);
	}
	
	@Override
	public Parent getNode() { return node; }
}
