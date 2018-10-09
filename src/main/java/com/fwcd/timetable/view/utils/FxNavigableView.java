package com.fwcd.timetable.view.utils;

import com.fwcd.fructose.Option;

import javafx.scene.Node;

public interface FxNavigableView {
	Node getContent();
	
	default Option<Node> getNavigationBar() { return Option.empty(); }
}
