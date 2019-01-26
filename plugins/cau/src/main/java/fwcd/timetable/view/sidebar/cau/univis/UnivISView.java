package fwcd.timetable.view.sidebar.cau.univis;

import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.TimeTableAppApi;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

public class UnivISView implements FxView {
	private final SplitPane node;
	private final UnivISNewQueryView input;
	private final UnivISQueryOutputView output;
	
	public UnivISView(TimeTableAppApi api) {
		node = new SplitPane();
		input = new UnivISNewQueryView(api);
		output = new UnivISQueryOutputView(api);
		
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
