package fwcd.timetable.view.utils;

import fwcd.fructose.Option;

import javafx.scene.Node;

public interface FxNavigableView {
	Node getContentNode();
	
	default Option<Node> getNavigatorNode() { return Option.empty(); }
}
