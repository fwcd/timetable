package fwcd.timetable.view.sidebar.calendar;

import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.calendar.CalendarCrateViewModel;

import javafx.scene.Node;

public class CalendarsSideView implements FxView {
	private final Node node;
	
	public CalendarsSideView(TimeTableAppContext context, CalendarCrateViewModel viewModel) {
		node = new CalendarManagerView(context, viewModel).getNode();
	}
	
	@Override
	public Node getNode() { return node; }
}
