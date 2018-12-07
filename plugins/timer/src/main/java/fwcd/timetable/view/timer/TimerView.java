package fwcd.timetable.view.timer;

import fwcd.timetable.view.FxView;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * The Timer plugin view class.
 */
public class TimerView implements FxView {
	private final VBox node;
	
	public TimerView() {
		node = new VBox();
	}
	
	@Override
	public Node getNode() { return node; }
}
