package com.fwcd.timetable.view;

import com.fwcd.timetable.view.calendar.CalendarsView;
import com.fwcd.timetable.view.sidebar.SideBarView;
import com.fwcd.timetable.view.utils.FxParentView;
import com.fwcd.timetable.viewmodel.TimeTableAppViewModel;

import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

public class TimeTableAppView implements FxParentView {
	private final BorderPane node;
	private final TimeTableAppContext context = new TimeTableAppContext();
	private final TimeTableAppViewModel viewModel = new TimeTableAppViewModel();
	
	public TimeTableAppView() {
		node = new BorderPane();
		
		CalendarsView calendar = new CalendarsView(context, viewModel.getCalendars());
		SideBarView sideBar = new SideBarView(context, viewModel.getCalendars());
		SplitPane split = new SplitPane(
			calendar.getNode(),
			sideBar.getNode()
		);
		SplitPane.setResizableWithParent(sideBar.getNode(), false);
		split.setOrientation(Orientation.HORIZONTAL);
		split.setDividerPositions(0.7);
		
		node.setTop(new MenuBarView(context, viewModel).getNode());
		node.setCenter(split);
	}
	
	@Override
	public Parent getNode() { return node; }
}
