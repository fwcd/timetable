package fwcd.timetable.view;

import fwcd.timetable.view.calendar.CalendarsView;
import fwcd.timetable.view.sidebar.SideBarView;
import fwcd.timetable.view.utils.FxParentView;
import fwcd.timetable.viewmodel.TimeTableAppApi;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.TimeTableAppViewModel;

import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

public class TimeTableAppView implements FxParentView {
	private final BorderPane node;
	
	public TimeTableAppView(TimeTableAppContext context, TimeTableAppViewModel viewModel, TimeTableAppApi api) {
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
		
		node.setTop(new MenuBarView(context, viewModel, api).getNode());
		node.setCenter(split);
	}
	
	@Override
	public Parent getNode() { return node; }
}
