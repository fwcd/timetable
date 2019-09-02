package fwcd.timetable.view.ical;

import fwcd.timetable.view.FxView;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class ICalView implements FxView {
	private final Pane node;
	
	public ICalView() {
		node = new Pane();
		
	}
	
	@Override
	public Node getNode() { return node; }
}
