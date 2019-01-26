package fwcd.timetable.view.sidebar.cau.univis;

import fwcd.timetable.view.utils.FxView;
import fwcd.timetable.viewmodel.TimeTableAppContext;
import fwcd.timetable.viewmodel.calendar.CalendarsViewModel;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

public class UnivISView implements FxView {
	private final SplitPane node;
	private final UnivISNewQueryView input;
	private final UnivISQueryOutputView output;
	
	public UnivISView(TimeTableAppContext context, CalendarsViewModel calendars) {
		node = new SplitPane();
		input = new UnivISNewQueryView(context);
		output = new UnivISQueryOutputView(context, calendars.getModel());
		
		input.getQueryListeners().add(output::perform);
		
		node.setOrientation(Orientation.VERTICAL);
		node.getItems().addAll(
			input.getNode(),
			output.getNode()
		);
	}
	
	@Override
	public Node getNode() { return node; }
}
